package pl.roszkowska.track.statistics;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class StatisticsFeature extends Feature<StatisticsState, StatisticsEvent, StatisticsEffect> {
    public StatisticsFeature(StatisticsState initialState,
                             Observable<StatisticsEvent> events,
                             Actor<StatisticsEvent, StatisticsState, StatisticsEffect> actor,
                             StateReducer<StatisticsEffect, StatisticsState> reducer) {
        super(initialState, events, actor, reducer);
    }
}

