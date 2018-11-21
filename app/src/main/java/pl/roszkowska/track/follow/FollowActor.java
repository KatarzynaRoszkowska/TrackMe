package pl.roszkowska.track.follow;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.common.Actor;

public class FollowActor implements Actor<Event, Effect> {

    private final Repository mRepository;

    public FollowActor(Repository repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<Effect> act(Event event) {
        if (event instanceof Event.StartFollowing) {
            return Observable
                    .create((ObservableEmitter<Integer> emitter) -> {
                        emitter.onNext(mRepository.createNewFollowRoute());
                        emitter.onComplete();
                    })
                    .map(Effect.StartedFollowing::new)
                    .cast(Effect.class)
                    .subscribeOn(Schedulers.io());
        } else if (event instanceof Event.NewStep) {
            Event.NewStep e = (Event.NewStep) event;

            return Observable
                    .create(emitter -> {
                        emitter.onNext(new Effect.NewStep(e.routeId, e.lat, e.lon));
                        mRepository.savePosition(e.routeId, e.lat, e.lon);
                        emitter.onComplete();
                    })
                    .cast(Effect.class)
                    .observeOn(Schedulers.io());
        } else if (event instanceof Event.StopFollowing) {
            return Observable.just(new Effect.StoppedFollowing());
        }
        throw new IllegalStateException("Unknown event");
    }
}

