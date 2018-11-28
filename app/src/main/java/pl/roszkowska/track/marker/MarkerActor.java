package pl.roszkowska.track.marker;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Pair;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.location.LocationProvider;

public class MarkerActor implements Actor<Event, State, Effect> {

    private final Repository mRepository;
    private final LocationProvider mLocationProvider;

    public MarkerActor(Repository repository, LocationProvider locationProvider) {
        this.mRepository = repository;
        mLocationProvider = locationProvider;
    }

    @Override

    public Observable<Effect> act(State state, Event event) {
        if (event instanceof Event.MarkPoint) {
            return markPoint((Event.MarkPoint) event);
        } else if (event instanceof Event.RemovePoint) {
            return removePoint((Event.RemovePoint) event);
        }
        throw new IllegalStateException("Unknown event");
    }

    private Observable<Effect> removePoint(Event.RemovePoint event) {
        return Observable
                .create(emitter -> {
                    emitter.onNext(new Effect.RemovePoint(event.id));
                    emitter.onComplete();
                })
                .cast(Effect.class)
                .observeOn(Schedulers.io());
    }

   private Observable<Effect> markPoint(Event.MarkPoint event) {
        return Observable.zip(
                mLocationProvider.locationStream(),
                Observable.just(event),
                Pair::new
        ).flatMap(this::savePointToRepository);
    }

    @NonNull
    private Observable<Effect> savePointToRepository(Pair<Location, Event.MarkPoint> pair) {
        Location location = pair.first;
        Event.MarkPoint e = pair.second;
        return Observable.create(emitter -> {
            Effect effect = new Effect.MarkPoint(e.name, location.getLatitude(), location.getLongitude());
            emitter.onNext(effect);
            emitter.onComplete();
        })
                .cast(Effect.class)
                .subscribeOn(Schedulers.io());
    }
}
