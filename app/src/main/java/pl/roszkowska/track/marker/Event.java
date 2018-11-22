package pl.roszkowska.track.marker;

public interface Event {
    class MarkPoint implements Event {
        final String name;

        public MarkPoint(String name) {
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

