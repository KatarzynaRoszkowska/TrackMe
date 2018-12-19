package pl.roszkowska.track.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsState {
    public Map<Long, RouteStatistics> statistics = new HashMap<>();

    public StatisticsState() {
    }

    public StatisticsState(StatisticsState other) {
        this.statistics = other.statistics;
    }

    public static class RouteStatistics {

        public long routeId;
        public int trackLength;
        public float averageSpeed;
        public float maxSpeed;
        public long trackTime;
        public List<Step> steps = new ArrayList<>();

        public static class Step {
            public final long id;
            public final long time;
            public final long distance;
            public final double lat;
            public final double lon;

            public Step(long id, long time, long distance, double lat, double lon) {
                this.id = id;
                this.time = time;
                this.distance = distance;
                this.lat = lat;
                this.lon = lon;
            }
        }

        @Override
        public String toString() {
            return "RouteStatistics{" +
                    "trackLength=" + trackLength +
                    ", averageSpeed=" + averageSpeed +
                    ", maxSpeed=" + maxSpeed +
                    ", trackTime=" + trackTime +
                    ", steps=" + steps +
                    '}';
        }
    }
}
