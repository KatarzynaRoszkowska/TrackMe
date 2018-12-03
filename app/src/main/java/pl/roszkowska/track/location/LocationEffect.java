package pl.roszkowska.track.location;

public interface LocationEffect {
    class Start {
    }

    class Stop {
    }

    class AddStep {
        public final LocationInfo locationInfo;

        public AddStep(LocationInfo locationInfo) {
            this.locationInfo = locationInfo;
        }
    }
}
