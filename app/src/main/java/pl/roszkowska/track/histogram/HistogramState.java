package pl.roszkowska.track.histogram;

import java.util.ArrayList;
import java.util.List;

public class HistogramState {
    public int averageSpeedForRoute;
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
}
