package pl.roszkowska.track.statistics;

import java.util.ArrayList;
import java.util.List;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.follow.RouteRepository.StepInfo;
import pl.roszkowska.track.statistics.StatisticsEffect.StepsArrived;
import pl.roszkowska.track.statistics.StatisticsState.RouteStatistics;
import pl.roszkowska.track.statistics.StatisticsState.RouteStatistics.Step;

public class StatisticsReducer implements StateReducer<StatisticsEffect, StatisticsState> {
    @Override
    public StatisticsState reduce(StatisticsState state, StatisticsEffect effect) {
        StatisticsState outState = new StatisticsState(state);
        if (effect instanceof StepsArrived) handleStepsArrived(effect, outState);
        return outState;
    }

    private void handleStepsArrived(StatisticsEffect effect, StatisticsState outState) {
        List<StepInfo> steps = ((StepsArrived) effect).steps;
        if (steps.size() <= 1) return;

        List<Step> outSteps = new ArrayList<>();
        long lastTimeStamp = steps.get(0).timestamp;
        int progressDistance = 0;
        long progressTime = 0;
        float maxSpeed = 0;
        for (StepInfo info : steps) {
            long deltaTime = info.timestamp - lastTimeStamp;
            if (deltaTime > 0) {
                maxSpeed = Math.max(maxSpeed, calculateSpeed(deltaTime, info.distance));
            }

            progressDistance += info.distance;
            progressTime += info.timestamp - lastTimeStamp;
            lastTimeStamp = info.timestamp;

            outSteps.add(new Step(
                    info.id,
                    progressTime / 1000,
                    progressDistance,
                    info.lat,
                    info.lon
            ));
        }
        RouteStatistics routeState = getOrCreateState(outState, effect.getRouteId());
        routeState.steps = outSteps;
        routeState.averageSpeed = calculateSpeed(progressTime, progressDistance);
        routeState.maxSpeed = maxSpeed;
        routeState.trackLength = progressDistance;
        routeState.trackTime = progressTime;
    }

    private RouteStatistics getOrCreateState(StatisticsState state, long routeId) {
        RouteStatistics routeState = state.statistics.get(routeId);
        if (routeState == null) {
            routeState = new RouteStatistics();
            routeState.routeId = routeId;
            state.statistics.put(routeId, routeState);
        }
        return routeState;
    }

    private float calculateSpeed(long timeInMillis, long distanceInMeters) {
        return distanceInMeters / 1000F / timeInMillis * 1000F * 3600F;
    }
}
