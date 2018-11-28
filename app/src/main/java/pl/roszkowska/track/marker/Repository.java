package pl.roszkowska.track.marker;

import io.reactivex.Observable;

public interface Repository {

    Observable<Integer> savePoint(String name, double lat, double lon);
}
