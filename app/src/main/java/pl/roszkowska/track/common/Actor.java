package pl.roszkowska.track.common;

import io.reactivex.Observable;

public interface Actor<Event, Effect> {

    Observable<Effect> act(Event event);
}
