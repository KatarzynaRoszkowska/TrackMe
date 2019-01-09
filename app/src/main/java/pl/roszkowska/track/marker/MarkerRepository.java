package pl.roszkowska.track.marker;

import java.util.List;

import io.reactivex.Observable;

public interface MarkerRepository {

    Observable<List<MarkerInfo>> getAllMarkers();

    Observable<Long> savePoint(MarkerInfo markerInfo);

    class MarkerInfo {
        public final String name;
        public final long id;
        public final double lat;
        public final double lon;
        public final long timestamp;

        public MarkerInfo(String name, double lat, double lon, long timestamp) {
            this(-1, name, lat, lon, timestamp);
        }

        public MarkerInfo(long id, String name, double lat, double lon, long timestamp) {
            this.name = name;
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.timestamp = timestamp;
        }
    }
}
