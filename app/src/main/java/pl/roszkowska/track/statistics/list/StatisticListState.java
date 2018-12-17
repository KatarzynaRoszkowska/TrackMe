package pl.roszkowska.track.statistics.list;

import java.util.ArrayList;
import java.util.List;

public class StatisticListState {

    public boolean isLoading;
    public List<Item> itemList = new ArrayList<>();

    public static class Item {
        public final long routeId;
        public final long routeDuration;
        public final long timestamp;

        public Item(long routeId, long routeDuration, long timestamp) {
            this.routeId = routeId;
            this.routeDuration = routeDuration;
            this.timestamp = timestamp;
        }
    }
}
