package pl.roszkowska.track.statistics;

import java.util.List;

import pl.roszkowska.track.follow.FollowRepository;

public interface StatisticsEffect {
    class StepsArrived implements StatisticsEffect {
        public final List<FollowRepository.StepInfo> steps;

        public StepsArrived(List<FollowRepository.StepInfo> steps) {
            this.steps = steps;
        }
    }
}
