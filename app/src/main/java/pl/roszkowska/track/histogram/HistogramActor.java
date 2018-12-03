package pl.roszkowska.track.histogram;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.follow.FollowRepository;

public class HistogramActor implements Actor<HistogramEvent, HistogramState, HistorgramEffect> {

    private final FollowRepository mRepository;

    public HistogramActor(FollowRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<HistorgramEffect> act(HistogramState state, HistogramEvent event) {
        if (event instanceof HistogramEvent.ReadRoute) {
            HistogramEvent.ReadRoute readRoute = (HistogramEvent.ReadRoute) event;
            return mRepository
                    .getAllSteps(readRoute.routeId)
                    .map(HistorgramEffect.StepsArrived::new)
                    .cast(HistorgramEffect.class);
        }
        throw new IllegalStateException();
    }
}
