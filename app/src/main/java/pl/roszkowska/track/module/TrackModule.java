package pl.roszkowska.track.module;

import android.content.Context;

import io.reactivex.Observable;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.follow.FollowState;
import pl.roszkowska.track.marker.MarkerState;
import pl.roszkowska.track.statistics.StatisticsState;
import pl.roszkowska.track.statistics.list.StatisticListState;

public class TrackModule {

    public static void setup(Context context) {
        EventDispatcherModule.setup();
        RepositoryModule.setup(context);
        FeatureModule.setup(context);
    }

    public static EventDispatcher getEventDispatcher() {
        return EventDispatcherModule.getModule().getDispatcher();
    }

    public static Observable<FollowState> followStateStream() {
        return FeatureModule.getModule().getFollowFeature().states;
    }

    public static Observable<MarkerState> markerStateStream() {
        return FeatureModule.getModule().getMarkerFeature().states;
    }

    public static Observable<StatisticsState.RouteStatistics> histogramStateStream(long routeId) {
        return FeatureModule
                .getModule()
                .getStatisticsFeature()
                .states
                .flatMapIterable(s -> s.statistics.values()) // konwertuje List<RouteState> do RouteState stream
                .filter(s -> s.routeId == routeId); // filtruje liste i zwracamy tylko RouteState ktory jest nasz
    }

    public static Observable<StatisticListState> statisticListStateStream() {
        return FeatureModule.getModule().getStatisticListFeature().states;
    }
}
