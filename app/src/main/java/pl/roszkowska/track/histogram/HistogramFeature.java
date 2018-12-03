package pl.roszkowska.track.histogram;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class HistogramFeature extends Feature<HistogramState, HistogramEvent, HistorgramEffect> {
    public HistogramFeature(HistogramState initialState,
                            Observable<HistogramEvent> events,
                            Actor<HistogramEvent, HistogramState, HistorgramEffect> actor,
                            StateReducer<HistorgramEffect, HistogramState> reducer) {
        super(initialState, events, actor, reducer);
    }
}

