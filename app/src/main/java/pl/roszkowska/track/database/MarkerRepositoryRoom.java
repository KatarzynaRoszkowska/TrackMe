package pl.roszkowska.track.database;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<MarkerInfo>> getAllMarkers() {
        return Observable.create((ObservableEmitter<List<MarkerInfo>> emitter) -> {
            List<MarkerEntity> markerInfo = mTrackDatabase.doaMarker().getMarkers();
            List<MarkerInfo> infos = new ArrayList<>();
            for (MarkerEntity entity : markerInfo) {
                infos.add(new MarkerInfo(
                        entity.name,
                        entity.id,
                        entity.lat,
                        entity.lon
                ));
            }

            emitter.onNext(infos);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
