package pl.roszkowska.track.marker;

public interface Effect {
    class MarkPoint implements Effect {
        final int id;
        final String name;
        final double lat;
        final double lon;

        public MarkPoint(int id, String name, double lat, double lon) {
            this.id = id;
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }
    }

    class RemovePoint implements Effect {
        final int id;

        public RemovePoint(int id) {
            this.id = id;
        }
    }
}

