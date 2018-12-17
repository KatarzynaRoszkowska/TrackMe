package pl.roszkowska.track.statistics;

import java.util.List;

import pl.roszkowska.track.follow.RouteRepository;

public interface StatisticsEffect {
    class StepsArrived implements StatisticsEffect {
        public final List<RouteRepository.StepInfo> steps;

        public StepsArrived(List<RouteRepository.StepInfo> steps) {
            this.steps = steps;
        }
    }
}
