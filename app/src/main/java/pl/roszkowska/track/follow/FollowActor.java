package pl.roszkowska.track.follow;

import android.location.Location;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.location.GpsLocationStream;

public class FollowActor implements Actor<FollowEvent, FollowState, FollowEffect> {

    private final FollowRepository mRepository;
    private final GpsLocationStream mLocationStream;

    public FollowActor(FollowRepository repository, GpsLocationStream locationStream) {
        this.mRepository = repository;
        mLocationStream = locationStream;
    }

    @Override
    public Observable<FollowEffect> act(FollowState state, FollowEvent event) {
        if (event instanceof FollowEvent.StartFollowing) {
            return Observable.merge(
                    startEffect(),
                    listenToStepsEffect(state.routeId));
        } else if (event instanceof FollowEvent.StopFollowing) {
            return Observable.just(new FollowEffect.StoppedFollowing());
        }
        throw new IllegalStateException("Unknown event");
    }

    private Observable<FollowEffect> listenToStepsEffect(long routeId) {
        return mLocationStream
                .locationStream()
                .flatMap(locationInfo -> createStep(routeId, locationInfo.lat, locationInfo.lon))
                .flatMap(stepEffect -> mRepository.savePosition(
                        routeId,
                        stepEffect.lat,
                        stepEffect.lon,
                        stepEffect.timestamp,
                        stepEffect.distance)
                        .map(aLong -> stepEffect)
                )
                .doOnSubscribe(disposable -> mLocationStream.start())
                .cast(FollowEffect.class);
    }

    private Observable<FollowEffect.StartedFollowing> startEffect() {
        return mRepository
                .createNewFollowRoute()
                .map(FollowEffect.StartedFollowing::new);
    }

    private Observable<FollowEffect.NewStep> createStep(long routeId, double lat, double lon) {
        return mRepository
                .getLastStep(routeId)
                .map(stepInfo -> {
                    if (stepInfo.timestamp == -1) return 0L;
                    Location location = new Location("");
                    location.setLatitude(lat);
                    location.setLongitude(lon);

                    Location location2 = new Location("");
                    location2.setLatitude(stepInfo.lat);
                    location2.setLongitude(stepInfo.lon);

                    return (long) location.distanceTo(location2);
                })
                .map(distance -> new FollowEffect.NewStep(lat, lon, distance, System.currentTimeMillis()));
    }
}

