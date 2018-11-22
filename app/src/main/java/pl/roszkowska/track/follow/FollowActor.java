package pl.roszkowska.track.follow;

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
            return mRepository
                    .savePosition(state.routeId, e.lat, e.lon)
                    .map(integer -> new Effect.NewStep(e.lat, e.lon))
                    .cast(Effect.class);
        } else if (event instanceof Event.StopFollowing) {
            return Observable.just(new Effect.StoppedFollowing());
        }
        throw new IllegalStateException("Unknown event");
    }
}

