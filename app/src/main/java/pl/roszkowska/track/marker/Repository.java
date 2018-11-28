package pl.roszkowska.track.marker;

import io.reactivex.Observable;

public interface Repository {

    Observable<Long> savePoint(String name, double lat, double lon);
}
