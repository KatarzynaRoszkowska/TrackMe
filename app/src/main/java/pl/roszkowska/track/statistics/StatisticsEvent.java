package pl.roszkowska.track.statistics;

public interface StatisticsEvent {

    class ReadRoute implements StatisticsEvent {
        final long routeId;

        public ReadRoute(long routeId) {
            this.routeId = routeId;
        }
    }
}
