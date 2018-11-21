package pl.roszkowska.track.marker;

import java.util.LinkedList;

public class State {
    final LinkedList<MarkerEntity> mMarkerOptionsList;

    public State() {
        mMarkerOptionsList = new LinkedList<>();
    }

    public State(State other) {
        mMarkerOptionsList = new LinkedList<>(other.mMarkerOptionsList);
    }

    public static class MarkerEntity {
        final int id;
        final String name;

        public MarkerEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
