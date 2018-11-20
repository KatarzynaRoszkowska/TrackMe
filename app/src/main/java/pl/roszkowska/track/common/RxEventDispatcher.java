package pl.roszkowska.track.common;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxEventDispatcher implements EventDispatcher {

    private Subject<Object> eventObservable = PublishSubject.create();

    @Override
    public void sendEvent(Object event) {
        eventObservable.onNext(event);
    }

    @Override
    public Observable<Object> observable() {
        return eventObservable.share();
    }

    @Override
    public <T> Observable<T> ofType(Class<T> classType) {
        return eventObservable.ofType(classType);
    }
}
