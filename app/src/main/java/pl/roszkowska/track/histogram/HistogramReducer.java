package pl.roszkowska.track.histogram;

import java.util.ArrayList;
import java.util.List;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.follow.FollowRepository;

public class HistogramReducer implements StateReducer<HistorgramEffect, HistogramState> {
    @Override
    public HistogramState reduce(HistogramState state, HistorgramEffect effect) {
        if (effect instanceof HistorgramEffect.StepsArrived) {
            List<FollowRepository.StepInfo> steps = ((HistorgramEffect.StepsArrived) effect).steps;
            if (steps.size() <= 1) return state;

            List<HistogramState.Step> outSteps = new ArrayList<>();
            long lastTimeStamp = steps.get(0).timestamp;
            long progressDistance = 0;
            long progressTime = 0;
            for (FollowRepository.StepInfo info : steps) {
                progressDistance += info.distance;
                progressTime += info.timestamp - lastTimeStamp;
                lastTimeStamp = info.timestamp;

                outSteps.add(new HistogramState.Step(info.id, progressTime / 1000, progressDistance));
            }
            HistogramState outState = new HistogramState();
            outState.steps = outSteps;
            outState.averageSpeedForRoute = (int) (progressDistance / 1000 / progressTime / 1000 / 3600);
            return outState;
        }
        throw new IllegalStateException();
    }
}
