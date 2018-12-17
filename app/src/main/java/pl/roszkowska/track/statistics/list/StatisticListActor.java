package pl.roszkowska.track.statistics.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.follow.RouteRepository;

public class StatisticListActor implements Actor<StatisticListEvent, StatisticListState, StatisticListEffect> {

    private final RouteRepository mRepository;

    public StatisticListActor(RouteRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<StatisticListEffect> act(StatisticListState state, StatisticListEvent event) {
        return Observable.<StatisticListEffect>just(new StatisticListEffect.StartedLoading())
                .mergeWith(mRepository
                        .getAllRoutes()
                        .map(this::convert)
                        .map(StatisticListEffect.ListLoaded::new)
                );

    }

    private List<StatisticListState.Item> convert(List<RouteRepository.RouteInfo> routeInfos) {
        List<StatisticListState.Item> itemList = new ArrayList<>();
        for (RouteRepository.RouteInfo info : routeInfos) {
            itemList.add(new StatisticListState.Item(
                    info.routeId,
                    info.routeDuration,
                    info.timestamp)
            );
        }
        Collections.sort(itemList, (o1, o2) -> Long.compare(o2.routeId, o1.routeId));
        return itemList;
    }
}
