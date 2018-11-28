package pl.roszkowska.track.histogram;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.follow.Repository;

public class HistogramActor implements Actor<Event, State, Effect> {

    private final Repository mRepository;

    public HistogramActor(Repository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<Effect> act(State state, Event event) {
        if (event instanceof Event.ReadRoute) {
            Event.ReadRoute readRoute = (Event.ReadRoute) event;
            return mRepository
                    .getAllSteps(readRoute.routeId)
                    .map(Effect.StepsArrived::new)
                    .cast(Effect.class);
        }
        throw new IllegalStateException();
    }
}
