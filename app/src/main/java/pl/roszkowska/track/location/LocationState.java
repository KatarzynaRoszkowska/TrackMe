package pl.roszkowska.track.location;

import java.util.ArrayList;
import java.util.List;

public class LocationState {
    boolean hasStarted;
    List<LocationInfo> recordedSteps = new ArrayList<>();

    public LocationState() {
        hasStarted = false;
    }

    public LocationState(LocationState other) {
        hasStarted = other.hasStarted;
        recordedSteps = other.recordedSteps;
    }
}
