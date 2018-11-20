package pl.roszkowska.track.follow;

public interface Effect {
    class StartedFollowing implements Effect {
        public final int id;

        public StartedFollowing(int id) {
            this.id = id;
        }
    }

    class StoppedFollowing implements Effect {
    }

    class NewStep implements Effect {
        public final double id;
        public final double lat;
        public final double lon;

        public NewStep(double id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }
    }
}
