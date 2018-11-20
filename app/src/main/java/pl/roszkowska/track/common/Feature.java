package pl.roszkowska.track.common;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Feature<State, Event, Effect> {
    public Observable<State> states;

    public Feature(State initialState,
                   Observable<Event> events,
                   Actor<Event, Effect> actor,
                   StateReducer<Effect, State> reducer) {
        states = events
                .flatMap(event -> {
                    return actor.act(event);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .scan(initialState, (state, event1) -> {
                    return reducer.reduce(state, event1);
                })
                .replay(1)
                .autoConnect(0);
    }
}
