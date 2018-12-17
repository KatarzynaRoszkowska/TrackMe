package pl.roszkowska.track.statistics.list;

import pl.roszkowska.track.common.StateReducer;

public class StatisticsListReducer implements StateReducer<StatisticListEffect, StatisticListState> {
    @Override
    public StatisticListState reduce(StatisticListState state, StatisticListEffect effect) {
        StatisticListState outState = new StatisticListState();
        if (effect instanceof StatisticListEffect.ListLoaded) {
            outState.isLoading = false;
            outState.itemList = ((StatisticListEffect.ListLoaded) effect).items;
        } else if (effect instanceof StatisticListEffect.StartedLoading) {
            outState.isLoading = true;
            outState.itemList.clear();
        }
        return outState;
    }
}
