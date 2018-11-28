package pl.roszkowska.track.histogram;

import java.util.ArrayList;
import java.util.List;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.follow.Repository;

public class HistogramReducer implements StateReducer<Effect, State> {
    @Override
    public State reduce(State state, Effect effect) {
        if (effect instanceof Effect.StepsArrived) {
            List<Repository.StepInfo> steps = ((Effect.StepsArrived) effect).steps;
            if (steps.isEmpty()) return state;

            List<State.Step> outSteps = new ArrayList<>();
            long lastTimeStamp = steps.get(0).timestamp;
            long progressDistance = 0;
            long progressTime = 0;
            for (Repository.StepInfo info : steps) {
                progressDistance += info.distance;
                progressTime += info.timestamp - lastTimeStamp;
                lastTimeStamp = info.timestamp;

                outSteps.add(new State.Step(progressTime / 1000, progressDistance));
            }
            State outState = new State();
            outState.steps = outSteps;
            outState.averageSpeedForRoute = (int) (progressDistance / 1000 / progressTime / 1000 / 3600);
            return outState;
        }
        throw new IllegalStateException();
    }
}
