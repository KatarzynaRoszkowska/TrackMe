package pl.roszkowska.track.statistics;

import java.util.ArrayList;
import java.util.List;

public class StatisticsState {
    int dlugoscTrasy;
    float sredniaPredkosc;
    float maksymalnaPredkosc;
    long czasRuchu;
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
