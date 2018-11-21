package pl.roszkowska.track.marker;

public interface Effect {
    class MarkPoint implements Effect {
        final int id;
        final String name;

        public MarkPoint(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    class RemovePoint implements Effect {
        final int id;

        public RemovePoint(int id) {
            this.id = id;
        }
    }
}

