package pl.roszkowska.track.follow;

import android.location.Location;
import android.util.Pair;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.location.GpsLocationStream;
import pl.roszkowska.track.location.LocationInfo;

public class FollowActor implements Actor<FollowEvent, FollowState, FollowEffect> {

    private final RouteRepository mRepository;
    private final GpsLocationStream mLocationStream;
    private Subject<Long> mRouteId = BehaviorSubject.create();
    private Subject<Boolean> mStopSignal = PublishSubject.create();

    public FollowActor(RouteRepository repository, GpsLocationStream locationStream) {
        this.mRepository = repository;
        mLocationStream = locationStream;
    }

    @Override
    public Observable<FollowEffect> act(FollowState state, FollowEvent event) {
        if (event instanceof FollowEvent.StartFollowing) {
            return startFollowing();
        } else if (event instanceof FollowEvent.StopFollowing) {
            return stopFollowing();
        }
        throw new IllegalStateException("Unknown event");
    }

    private Observable<FollowEffect> stopFollowing() {
        return automaticallyFollowSteps()
                .firstOrError()
                .toObservable()
                .startWith(Observable
                        .just(new FollowEffect.StoppedFollowing())
                        .cast(FollowEffect.class)
                        .doOnSubscribe(disposable -> {
                            mLocationStream.stop();
                            mStopSignal.onNext(true);
                        })
                );
    }

    private Observable<FollowEffect> startFollowing() {
        return automaticallyFollowSteps()
                .startWith(createNewRoute())
                .takeUntil(mStopSignal);
    }

    private Observable<FollowEffect> createNewRoute() {
        return mRepository
                .createNewRoute()
                .map(FollowEffect.StartedFollowing::new)
                .doOnNext(startedFollowing -> mRouteId.onNext(startedFollowing.id))
                .doOnSubscribe(disposable -> mLocationStream.start())
                .cast(FollowEffect.class);
    }

    private Observable<FollowEffect> automaticallyFollowSteps() {
        return mRouteId
                .firstOrError()
                .toObservable()
                .flatMap(routeId ->
                        mLocationStream
                                .locationStream()
                                .map(locationInfo -> new Pair<>(routeId, locationInfo))

                )
                .flatMap(pair -> createStepAndCalculateDistance(pair.first, pair.second));
    }

    private Observable<FollowEffect> createStepAndCalculateDistance(long routeId,
                                                                    LocationInfo locationInfo) {
        return calculateDistance(routeId, locationInfo)
                .flatMap(distance -> saveStepToRepo(routeId, locationInfo, distance));
    }

    private Observable<Long> calculateDistance(long routeId, LocationInfo locationInfo) {
        return mRepository
                .getLastStep(routeId)
                .map(stepInfo -> {
                    if (stepInfo.timestamp == -1) return 0L;
                    Location location = new Location("");
                    location.setLatitude(locationInfo.lat);
                    location.setLongitude(locationInfo.lon);

                    Location location2 = new Location("");
                    location2.setLatitude(stepInfo.lat);
                    location2.setLongitude(stepInfo.lon);

                    return (long) location.distanceTo(location2);
                });
    }

    private ObservableSource<FollowEffect> saveStepToRepo(long routeId,
                                                          LocationInfo locationInfo,
                                                          Long distance) {
        long timestamp = System.currentTimeMillis();
        return mRepository
                .savePosition(routeId,
                        locationInfo.lat,
                        locationInfo.lon,
                        timestamp,
                        distance)
                .map(stepId -> new FollowEffect.NewStep(
                        routeId,
                        stepId,
                        locationInfo.lat,
                        locationInfo.lon,
                        distance,
                        0L
                ));
    }
}