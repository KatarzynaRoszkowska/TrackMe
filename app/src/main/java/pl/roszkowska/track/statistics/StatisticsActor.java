package pl.roszkowska.track.statistics;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.follow.RouteRepository;

public class StatisticsActor implements Actor<StatisticsEvent, StatisticsState, StatisticsEffect> {

    private final RouteRepository mRepository;

    public StatisticsActor(RouteRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<StatisticsEffect> act(StatisticsState state, StatisticsEvent event) {
        if (event instanceof StatisticsEvent.ReadRoute) {
            StatisticsEvent.ReadRoute readRoute = (StatisticsEvent.ReadRoute) event;
            return mRepository
                    .getAllSteps(readRoute.routeId)
                    .map(StatisticsEffect.StepsArrived::new)
                    .cast(StatisticsEffect.class);
        }
        throw new IllegalStateException();
    }
}
