package pl.roszkowska.track.statistics;

import java.util.ArrayList;
import java.util.List;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.follow.FollowRepository;

public class StatisticsReducer implements StateReducer<StatisticsEffect, StatisticsState> {
    @Override
    public StatisticsState reduce(StatisticsState state, StatisticsEffect effect) {
        if (effect instanceof StatisticsEffect.StepsArrived) {
            List<FollowRepository.StepInfo> steps = ((StatisticsEffect.StepsArrived) effect).steps;
            if (steps.size() <= 1) return state;

            List<StatisticsState.Step> outSteps = new ArrayList<>();
            long lastTimeStamp = steps.get(0).timestamp;
            long progressDistance = 0;
            long progressTime = 0;
            for (FollowRepository.StepInfo info : steps) {
                progressDistance += info.distance;
                progressTime += info.timestamp - lastTimeStamp;
                lastTimeStamp = info.timestamp;

                outSteps.add(new StatisticsState.Step(info.id, progressTime / 1000, progressDistance));
            }
            StatisticsState outState = new StatisticsState();
            outState.steps = outSteps;
            //outState.averageSpeedForRoute = (int) (progressDistance / 1000 / progressTime / 1000 / 3600);
            return outState;
        }
        throw new IllegalStateException();
    }
}
