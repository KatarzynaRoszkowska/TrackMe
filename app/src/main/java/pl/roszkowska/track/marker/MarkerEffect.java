package pl.roszkowska.track.marker;

import java.util.List;

public interface MarkerEffect {
    class MarkersLoaded implements MarkerEffect {
        final List<MarkerState.MarkerItem> markPoints;

        public MarkersLoaded(List<MarkerState.MarkerItem> markPoints) {
            this.markPoints = markPoints;
        }
    }

    class MarkPoint implements MarkerEffect {
        final long id;
        final String name;
        final double lat;
        final double lon;
        final long timestamp;

        public MarkPoint(long id, String name, double lat, double lon, long timestamp) {
            this.id = id;
            this.name = name;
            this.lat = lat;
            this.lon = lon;
            this.timestamp = timestamp;
        }
    }

    class RemovePoint implements MarkerEffect {
        final int id;

        public RemovePoint(int id) {
            this.id = id;
        }
    }
}

