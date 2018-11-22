package pl.roszkowska.track.common;

import io.reactivex.Observable;

public interface Actor<Event, State, Effect> {

    Observable<Effect> act(State state, Event event);
}
