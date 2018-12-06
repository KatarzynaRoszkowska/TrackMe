package pl.roszkowska.track.module;

import android.content.Context;

import io.reactivex.Observable;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.follow.FollowState;
import pl.roszkowska.track.marker.MarkerState;
import pl.roszkowska.track.statistics.StatisticsState;

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

    public static Observable<StatisticsState> histogramStateStream() {
        return FeatureModule.getModule().getStatisticsFeature().states;
    }
}
