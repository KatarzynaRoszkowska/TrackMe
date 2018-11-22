package pl.roszkowska.track.marker;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class MarkerFeature extends Feature<State, Event, Effect> {

    public MarkerFeature(State initialState,
                         Observable<Event> events,
                         Actor<Event, State, Effect> actor,
                         StateReducer<Effect, State> reducer) {
        super(initialState, events, actor, reducer);
    }
}
