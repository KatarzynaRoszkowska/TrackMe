package pl.roszkowska.track.location;

import pl.roszkowska.track.common.StateReducer;

public class LocationReducer implements StateReducer<LocationEffect, LocationState> {

    @Override
    public LocationState reduce(LocationState state, LocationEffect effect) {
        LocationState out = new LocationState(state);
        if (effect instanceof LocationEffect.Start) {
            out.hasStarted = true;
        } else if (effect instanceof LocationEffect.Stop) {
            out.hasStarted = false;
            out.recordedSteps.clear();
        } else if (effect instanceof LocationEffect.AddStep) {
            LocationEffect.AddStep addStep = (LocationEffect.AddStep) effect;
            out.recordedSteps.add(addStep.locationInfo);
        }
        return out;
    }
}
