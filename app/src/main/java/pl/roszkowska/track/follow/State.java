package pl.roszkowska.track.follow;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class State {
    public int routeId;
    public boolean isFollowing;
    public List<LatLng> steps;

    public State() {
        steps = new ArrayList<>();
    }

    public State(State other) {
        routeId = other.routeId;
        isFollowing = other.isFollowing;
        steps = new ArrayList<>(other.steps);
    }

    @Override
    public String toString() {
        return "State{" +
                "routeId=" + routeId +
                ", isFollowing=" + isFollowing +
                ", steps=" + steps +
                '}';
    }
}
