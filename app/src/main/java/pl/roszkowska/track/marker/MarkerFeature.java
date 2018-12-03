package pl.roszkowska.track.marker;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class MarkerFeature extends Feature<MarkerState, MarkerEvent, MarkerEffect> {

    public MarkerFeature(MarkerState initialState,
                         Observable<MarkerEvent> events,
                         Actor<MarkerEvent, MarkerState, MarkerEffect> actor,
                         StateReducer<MarkerEffect, MarkerState> reducer) {
        super(initialState, events, actor, reducer);
    }
}
