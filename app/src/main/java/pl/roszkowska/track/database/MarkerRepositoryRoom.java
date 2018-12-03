package pl.roszkowska.track.database;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.marker.MarkerRepository;

public class MarkerRepositoryRoom implements MarkerRepository {
    private final TrackDatabase mTrackDatabase;

    public MarkerRepositoryRoom(TrackDatabase trackDatabase) {
        this.mTrackDatabase = trackDatabase;
    }

    @Override
    public Observable<Long> savePoint(String name, double lat, double lon) {
        return Observable.create(emitter -> {
            MarkerEntity entity = new MarkerEntity();
            entity.name = name;
            entity.lat = lat;
            entity.lon = lon;

            long id = mTrackDatabase.doaMarker().savePoint(entity);
            emitter.onNext(id);
            emitter.onComplete();
        }).cast(Long.class)
                .subscribeOn(Schedulers.io());
    }
}
