package pl.roszkowska.track.marker;

import java.util.List;

import io.reactivex.Observable;

public interface MarkerRepository {

    Observable<List<MarkerInfo>> getAllMarkers();

    Observable<Long> savePoint(String name, double lat, double lon);

    class MarkerInfo {
        public final String name;
        public final long id;
        public final double lat;
        public final double lon;

        public MarkerInfo(String name, long id, double lat, double lon) {
            this.name = name;
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }
    }
}
