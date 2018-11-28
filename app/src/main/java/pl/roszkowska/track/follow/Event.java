package pl.roszkowska.track.follow;

public interface Event {

    class StartFollowing implements Event {

    }

    class StopFollowing implements Event {

    }

    class NewStep implements Event {
        public final double lat;
        public final double lon;
        public final long timestamp = System.currentTimeMillis();

        public NewStep(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}

