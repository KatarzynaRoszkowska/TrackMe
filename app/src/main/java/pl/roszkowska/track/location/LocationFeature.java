package pl.roszkowska.track.location;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class LocationFeature extends Feature<LocationState, LocationEvent, LocationEffect> {
    public LocationFeature(LocationState initialState,
                           Observable<LocationEvent> events,
                           Actor<LocationEvent, LocationState, LocationEffect> actor,
                           StateReducer<LocationEffect, LocationState> reducer) {
        super(initialState, events, actor, reducer);
    }
}