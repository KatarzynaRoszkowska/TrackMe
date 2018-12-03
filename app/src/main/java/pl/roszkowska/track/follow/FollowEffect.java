package pl.roszkowska.track.follow;

public interface FollowEffect {
    class StartedFollowing implements FollowEffect {
        public final long id;

        public StartedFollowing(long id) {
            this.id = id;
        }
    }

    class StoppedFollowing implements FollowEffect {
    }

    class NewStep implements FollowEffect {

        public final double lat;
        public final double lon;
        public final long distance;
        public final long timestamp;

        public NewStep(double lat, double lon, long timestamp) {
            this(lat, lon, 0, timestamp);
        }

        public NewStep(double lat, double lon, long distance, long timestamp) {
            this.lat = lat;
            this.lon = lon;
            this.distance = distance;
            this.timestamp = timestamp;
        }
    }
}
