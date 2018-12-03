package pl.roszkowska.track.histogram;

public interface HistogramEvent {

    class ReadRoute implements HistogramEvent {
        final long routeId;

        public ReadRoute(long routeId) {
            this.routeId = routeId;
        }
    }
}
