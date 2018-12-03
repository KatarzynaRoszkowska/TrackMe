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
        public final long routeId;
        public final long stepId;
        public final double lat;
        public final double lon;
        public final long distance;
        public final long timestamp;

        public NewStep(long routeId, long stepId, double lat, double lon, long timestamp) {
            this(routeId, stepId, lat, lon, 0, timestamp);
        }

        public NewStep(long routeId, long stepId, double lat, double lon, long distance, long timestamp) {
            this.routeId = routeId;
            this.stepId = stepId;
            this.lat = lat;
            this.lon = lon;
            this.distance = distance;
            this.timestamp = timestamp;
        }
    }
}
