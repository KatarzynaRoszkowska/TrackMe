package pl.roszkowska.track.follow;

import java.util.LinkedList;

public class FollowState {
    public long routeId;
    public boolean isFollowing;
    public boolean canShowHistogram;
    public LinkedList<Step> steps;

    public FollowState() {
        steps = new LinkedList<>();
    }

    public FollowState(FollowState other) {
        routeId = other.routeId;
        isFollowing = other.isFollowing;
        steps = new LinkedList<>(other.steps);
    }

    @Override
    public String toString() {
        return "HistogramState{" +
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
