package pl.roszkowska.track.statistics.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticListState {

    public boolean isLoading = true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticListState that = (StatisticListState) o;
        return isLoading == that.isLoading &&
                Objects.equals(itemList, that.itemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isLoading, itemList);
    }
}
