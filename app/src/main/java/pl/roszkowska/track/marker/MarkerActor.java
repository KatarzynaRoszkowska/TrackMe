package pl.roszkowska.track.marker;

import android.support.annotation.NonNull;
import android.util.Pair;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.location.LocationInfo;

public class MarkerActor implements Actor<MarkerEvent, MarkerState, MarkerEffect> {

    private final MarkerRepository mRepository;
    private final Observable<LocationInfo> mLocationStream;

    public MarkerActor(MarkerRepository repository, Observable<LocationInfo> locationStream) {
        this.mRepository = repository;
        mLocationStream = locationStream;
    }

    @Override

    public Observable<MarkerEffect> act(MarkerState state, MarkerEvent event) {
        if (event instanceof MarkerEvent.MarkPoint) {
            return markPoint((MarkerEvent.MarkPoint) event);
        } else if (event instanceof MarkerEvent.RemovePoint) {
            return removePoint((MarkerEvent.RemovePoint) event);
        }
        throw new IllegalStateException("Unknown event");
    }

    private Observable<MarkerEffect> removePoint(MarkerEvent.RemovePoint event) {
        return Observable
                .create(emitter -> {
                    emitter.onNext(new MarkerEffect.RemovePoint(event.id));
                    emitter.onComplete();
                })
                .cast(MarkerEffect.class)
                .observeOn(Schedulers.io());
    }

    private Observable<MarkerEffect> markPoint(MarkerEvent.MarkPoint event) {
        return Observable.zip(
                mLocationStream,
                Observable.just(event),
                Pair::new
        ).flatMap(this::savePointToRepository);
    }

    @NonNull
    private Observable<MarkerEffect> savePointToRepository(Pair<LocationInfo, MarkerEvent.MarkPoint> pair) {
        LocationInfo location = pair.first;
        MarkerEvent.MarkPoint e = pair.second;
        return mRepository
                .savePoint(e.name, location.lat, location.lon)
                .map(id -> new MarkerEffect.MarkPoint(id, e.name, location.lat, location.lon))
                .cast(MarkerEffect.class);
    }
}
