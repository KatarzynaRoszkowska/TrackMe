package pl.roszkowska.track.histogram;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class HistogramFeature extends Feature<State, Event, Effect> {
    public HistogramFeature(State initialState,
                            Observable<Event> events,
                            Actor<Event, State, Effect> actor,
                            StateReducer<Effect, State> reducer) {
        super(initialState, events, actor, reducer);
    }
}

