package pl.roszkowska.track.statistics;

import java.util.ArrayList;
import java.util.List;

public class StatisticsState {
    public int trackLength;
    public float averageSpeed;
    public float maxSpeed;
    public long trackTime;
    public List<Step> steps = new ArrayList<>();

    public static class Step {
        public final long id;
        public final long time;
        public final long distance;

        public Step(long id, long time, long distance) {
            this.id = id;
            this.time = time;
            this.distance = distance;
        }
    }

    @Override
    public String toString() {
        return "StatisticsState{" +
                "trackLength=" + trackLength +
                ", averageSpeed=" + averageSpeed +
                ", maxSpeed=" + maxSpeed +
                ", trackTime=" + trackTime +
                ", steps=" + steps +
                '}';
    }
}
