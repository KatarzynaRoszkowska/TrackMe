package pl.roszkowska.track.follow;

import android.location.Location;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;

public class FollowActor implements Actor<Event, State, Effect> {

    private final Repository mRepository;

    public FollowActor(Repository repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<Effect> act(State state, Event event) {
        if (event instanceof Event.StartFollowing) {
            return mRepository
                    .createNewFollowRoute()
                    .map(Effect.StartedFollowing::new)
                    .cast(Effect.class);
        } else if (event instanceof Event.NewStep) {
            Event.NewStep e = (Event.NewStep) event;

            return calculateDistance(state.routeId, e.lat, e.lon)
                    .flatMap(distance -> savePosition(state.routeId, e, distance));
        } else if (event instanceof Event.StopFollowing) {
            return Observable.just(new Effect.StoppedFollowing());
        }
        throw new IllegalStateException("Unknown event");
    }

    private Observable<Long> calculateDistance(long routeId, double lat, double lon) {
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
                }).cast(Long.class);
    }

    private Observable<Effect> savePosition(long routeId, Event.NewStep e, long distanceFromLastStep) {
        return mRepository
                .savePosition(routeId,
                        e.lat,
                        e.lon,
                        e.timestamp,
                        distanceFromLastStep)
                .map(integer -> new Effect.NewStep(e.lat,
                        e.lon,
                        distanceFromLastStep,
                        e.timestamp));
    }
}

