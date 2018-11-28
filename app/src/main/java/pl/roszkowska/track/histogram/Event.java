package pl.roszkowska.track.histogram;

public interface Event {

    class ReadRoute implements Event {
        final long routeId;

        public ReadRoute(long routeId) {
            this.routeId = routeId;
        }
    }
}
