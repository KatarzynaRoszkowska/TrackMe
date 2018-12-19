package pl.roszkowska.track.marker;

import java.util.ArrayList;
import java.util.List;

public class MarkerState {

    public final List<MarkerEntity> mMarkerOptionsList;

    public MarkerState() {

        mMarkerOptionsList = new ArrayList<>();
    }

    public MarkerState(MarkerState other) {
        mMarkerOptionsList = new ArrayList<>(other.mMarkerOptionsList);

    }

    public static class MarkerEntity {
        public final long id;
        public final String name;
        public final double lat;
        public final double lon;

        public MarkerEntity(long id,
                            String name,
                            double lastKnowLat,
                            double lon) {
            this.id = id;
            this.name = name;
            this.lat = lastKnowLat;
            this.lon = lon;
        }
    }
}
