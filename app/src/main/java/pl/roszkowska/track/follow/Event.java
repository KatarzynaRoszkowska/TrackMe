package pl.roszkowska.track.follow;

public interface Event {

    class StartFollowing implements Event {

    }

    class StopFollowing implements Event {

    }

    class NewStep implements Event {
        public final int routeId;
        public final double lat;
        public final double lon;

        public NewStep(int routeId, double lat, double lon) {
            this.routeId = routeId;
            this.lat = lat;
            this.lon = lon;
        }
    }
}

