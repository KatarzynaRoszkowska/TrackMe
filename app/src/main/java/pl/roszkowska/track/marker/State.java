package pl.roszkowska.track.marker;

import java.util.LinkedList;

import pl.roszkowska.track.database.MarkerEntity;

public class State {

    public final LinkedList<MarkerEntity> mMarkerOptionsList;

    public State() {

        mMarkerOptionsList = new LinkedList<>();
    }

    public State(State other) {
        mMarkerOptionsList = new LinkedList<>(other.mMarkerOptionsList);

    }

   /* public static class MarkerEntity {
        public final int id;
        public final String name;
        public final double lat;
        public final double lon;

        public MarkerEntity(int id,
                            String name,
                            double lastKnowLat,
                            double lon) {
            this.id = id;
            this.name = name;
            this.lat = lastKnowLat;
            this.lon = lon;
        }
    }*/
}
