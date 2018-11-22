package pl.roszkowska.track.follow;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

public class State {
    public long routeId;
    public boolean isFollowing;
    public LinkedList<LatLng> steps;

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

    class Step {
        LatLng latLng;
        long timestamp;
        int distance;
        int speed;
    }
}
