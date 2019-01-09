package pl.roszkowska.track.marker;

public interface MarkerEvent {
    class LoadMarkers implements MarkerEvent {

    }

    class MarkPoint implements MarkerEvent {
        final String name;

        public MarkPoint(String name) {
            this.name = name;
        }
    }

    class RemovePoint implements MarkerEvent {
        final long id;

        public RemovePoint(long id) {
            this.id = id;
        }
    }
}

