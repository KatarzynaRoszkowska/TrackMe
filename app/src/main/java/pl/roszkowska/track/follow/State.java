package pl.roszkowska.track.follow;

import java.util.LinkedList;

public class State {
    public long routeId;
    public boolean isFollowing;
    public LinkedList<Step> steps;

    public State() {
        steps = new LinkedList<>();
    }

    public State(State other) {
        routeId = other.routeId;
        isFollowing = other.isFollowing;
        steps = new LinkedList<>(other.steps);
    }

    @Override
    public String toString() {
        return "State{" +
                "routeId=" + routeId +
                ", isFollowing=" + isFollowing +
                ", steps=" + steps +
                '}';
    }

    public static class Step {
        public final double lat;
        public final double lon;
        public final long distance;
        public final long timestamp;

        public Step(double lat, double lon, long timestamp, long distance) {
            this.lat = lat;
            this.lon = lon;
            this.timestamp = timestamp;
            this.distance = distance;
        }
    }
}
