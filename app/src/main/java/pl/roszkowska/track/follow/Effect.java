package pl.roszkowska.track.follow;

public interface Effect {
    class StartedFollowing implements Effect {
        public final long id;

        public StartedFollowing(long id) {
            this.id = id;
        }
    }

    class StoppedFollowing implements Effect {
    }

    class NewStep implements Effect {
        public final double lat;
        public final double lon;

        public NewStep(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}
