package pl.roszkowska.track.marker;

import java.util.List;

public interface MarkerEffect {
    class MarkersLoaded implements MarkerEffect {
        final List<MarkerState.MarkerEntity> markPoints;

        public MarkersLoaded(List<MarkerState.MarkerEntity> markPoints) {
            this.markPoints = markPoints;
        }
    }

    class MarkPoint implements MarkerEffect {
        final long id;
        final String name;
        final double lat;
        final double lon;

        public MarkPoint(long id, String name, double lat, double lon) {
            this.id = id;
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }
    }

    class RemovePoint implements MarkerEffect {
        final int id;

        public RemovePoint(int id) {
            this.id = id;
        }
    }
}

