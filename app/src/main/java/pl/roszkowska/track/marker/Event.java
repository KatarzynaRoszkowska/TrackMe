package pl.roszkowska.track.marker;

public interface Event {
    class MarkPoint implements Event {
        final int id;
        final String name;

        public MarkPoint(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    class RemovePoint implements Event {
        final int id;

        public RemovePoint(int id) {
            this.id = id;
        }
    }
}

