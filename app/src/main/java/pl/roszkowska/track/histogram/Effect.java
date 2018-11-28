package pl.roszkowska.track.histogram;

import java.util.List;

import pl.roszkowska.track.follow.Repository;

public interface Effect {
    class StepsArrived implements Effect {
        public final List<Repository.StepInfo> steps;

        public StepsArrived(List<Repository.StepInfo> steps) {
            this.steps = steps;
        }
    }
}
