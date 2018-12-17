package pl.roszkowska.track.statistics.list;

import java.util.List;

public interface StatisticListEffect {
    class StartedLoading implements StatisticListEffect {

    }

    class ListLoaded implements StatisticListEffect {
        final List<StatisticListState.Item> items;

        public ListLoaded(List<StatisticListState.Item> items) {
            this.items = items;
        }
    }
}
