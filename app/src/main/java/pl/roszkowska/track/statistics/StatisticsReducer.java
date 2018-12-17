package pl.roszkowska.track.statistics;

import java.util.ArrayList;
import java.util.List;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.follow.RouteRepository;

public class StatisticsReducer implements StateReducer<StatisticsEffect, StatisticsState> {
    @Override
    public StatisticsState reduce(StatisticsState state, StatisticsEffect effect) {
        if (effect instanceof StatisticsEffect.StepsArrived) {
            List<RouteRepository.StepInfo> steps = ((StatisticsEffect.StepsArrived) effect).steps;
            if (steps.size() <= 1) return state;

            List<StatisticsState.Step> outSteps = new ArrayList<>();
            long lastTimeStamp = steps.get(0).timestamp;
            int progressDistance = 0;
            long progressTime = 0;
            float maxSpeed = 0;
            for (RouteRepository.StepInfo info : steps) {
                long deltaTime = info.timestamp - lastTimeStamp;
                if (deltaTime > 0) {
                    maxSpeed = Math.max(maxSpeed, calculateSpeed(deltaTime, info.distance));
                }

                progressDistance += info.distance;
                progressTime += info.timestamp - lastTimeStamp;
                lastTimeStamp = info.timestamp;

                outSteps.add(new StatisticsState.Step(info.id, progressTime / 1000, progressDistance));
            }
            StatisticsState outState = new StatisticsState();
            outState.steps = outSteps;
            outState.averageSpeed = calculateSpeed(progressTime, progressDistance);
            outState.maxSpeed = maxSpeed;
            outState.trackLength = progressDistance;
            outState.trackTime = progressTime;
            return outState;
        }
        throw new IllegalStateException();
    }

    private float calculateSpeed(long timeInMillis, long distanceInMeters) {
        return distanceInMeters / 1000F / timeInMillis * 1000F * 3600F;
    }
}
