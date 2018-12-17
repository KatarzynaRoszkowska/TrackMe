package pl.roszkowska.track.statistics.list;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class StatisticListFeature extends Feature<StatisticListState, StatisticListEvent, StatisticListEffect> {

    public StatisticListFeature(StatisticListState initialState,
                                Observable<StatisticListEvent> events,
                                Actor<StatisticListEvent, StatisticListState, StatisticListEffect> actor,
                                StateReducer<StatisticListEffect, StatisticListState> reducer) {
        super(initialState, events, actor, reducer);
    }
}
