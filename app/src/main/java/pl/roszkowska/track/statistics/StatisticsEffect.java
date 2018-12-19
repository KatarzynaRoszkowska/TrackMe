package pl.roszkowska.track.statistics;

import java.util.List;

import pl.roszkowska.track.follow.RouteRepository;

public interface StatisticsEffect {
    long getRouteId();

    class StepsArrived implements StatisticsEffect {
        public final long routeId;
        public final List<RouteRepository.StepInfo> steps;

        public StepsArrived(long routeId, List<RouteRepository.StepInfo> steps) {
            this.routeId = routeId;
            this.steps = steps;
        }

        @Override
        public long getRouteId() {
            return routeId;
        }
    }
}
