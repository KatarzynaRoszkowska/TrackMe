package pl.roszkowska.track.marker;

import java.util.ArrayList;
import java.util.List;

public class MarkerState {

    public final List<MarkerItem> mMarkerOptionsList;

    public MarkerState() {

        mMarkerOptionsList = new ArrayList<>();
    }

    public MarkerState(MarkerState other) {
        mMarkerOptionsList = new ArrayList<>(other.mMarkerOptionsList);

    }

    public static class MarkerItem {
        public final long id;
        public final String name;
        public final double lat;
        public final double lon;
        public final long timestamp;

        public MarkerItem(long id,
                          String name,
                          double lastKnowLat,
                          double lon,
                          long timestamp) {
            this.id = id;
            this.name = name;
            this.lat = lastKnowLat;
            this.lon = lon;
            this.timestamp = timestamp;
        }
    }
}
