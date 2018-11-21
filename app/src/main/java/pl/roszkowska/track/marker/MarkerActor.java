package pl.roszkowska.track.marker;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.common.Actor;

public class MarkerActor implements Actor<Event, Effect> {

    private final Repository mRepository;

    public MarkerActor(Repository repository) {
        this.mRepository = repository;
    }

    @Override

    public Observable<Effect> act(Event event) {
        if(event instanceof Event.MarkPoint){
            Event.MarkPoint e = (Event.MarkPoint) event;
            return Observable
                    .create((ObservableEmitter<Integer> emitter) -> {
                        emitter.onNext(mRepository.createNewPoint());
                        mRepository.savePoint(e.id,e.name);
                        emitter.onComplete();
                    })
                    .cast(Effect.class)
                    .subscribeOn(Schedulers.io());
        } else if (event instanceof Event.RemovePoint) {
            Event.RemovePoint e = (Event.RemovePoint) event;

            return Observable
                    .create(emitter -> {
                        emitter.onNext(new Effect.RemovePoint(e.id));
                        emitter.onComplete();
                    })
                    .cast(Effect.class)
                    .observeOn(Schedulers.io());
        }
        throw new IllegalStateException("Unknown event");
    }
}
