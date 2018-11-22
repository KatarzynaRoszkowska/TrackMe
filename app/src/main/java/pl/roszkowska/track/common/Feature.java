package pl.roszkowska.track.common;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Feature<State, Event, Effect> {
    public Observable<State> states;

    private State lastState;

    public Feature(State initialState,
                   Observable<Event> events,
                   Actor<Event, State, Effect> actor,
                   StateReducer<Effect, State> reducer) {
        lastState = initialState;
        states = events
                .flatMap(event -> {
                    return actor.act(lastState, event);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .scan(initialState, (state, effect) -> {
                    return lastState = reducer.reduce(state, effect);
                })
                .replay(1)
                .autoConnect(0);
    }
}
