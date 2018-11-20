package pl.roszkowska.track.follow;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class FollowFeature extends Feature<State, Event, Effect> {

    public FollowFeature(State initialState,
                         Observable<Event> events,
                         Actor<Event, Effect> actor,
                         StateReducer<Effect, State> reducer) {
        super(initialState, events, actor, reducer);
    }
}
