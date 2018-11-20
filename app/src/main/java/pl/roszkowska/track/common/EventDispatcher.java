package pl.roszkowska.track.common;

import io.reactivex.Observable;

public interface EventDispatcher {

    void sendEvent(Object event);

    Observable<Object> observable();

    <T> Observable<T> ofType(Class<T> classType);
}