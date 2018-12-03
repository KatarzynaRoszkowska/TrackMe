package pl.roszkowska.track.histogram;

import java.util.List;

import pl.roszkowska.track.follow.FollowRepository;

public interface HistorgramEffect {
    class StepsArrived implements HistorgramEffect {
        public final List<FollowRepository.StepInfo> steps;

        public StepsArrived(List<FollowRepository.StepInfo> steps) {
            this.steps = steps;
        }
    }
}
