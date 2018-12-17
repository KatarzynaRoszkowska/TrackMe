package pl.roszkowska.track.module;

import android.content.Context;

import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.follow.FollowActor;
import pl.roszkowska.track.follow.FollowEvent;
import pl.roszkowska.track.follow.FollowFeature;
import pl.roszkowska.track.follow.FollowReducer;
import pl.roszkowska.track.follow.FollowState;
import pl.roszkowska.track.location.GpsLocationStream;
import pl.roszkowska.track.location.LocationActor;
import pl.roszkowska.track.location.LocationEvent;
import pl.roszkowska.track.location.LocationFeature;
import pl.roszkowska.track.location.LocationReducer;
import pl.roszkowska.track.location.LocationState;
import pl.roszkowska.track.marker.MarkerActor;
import pl.roszkowska.track.marker.MarkerEvent;
import pl.roszkowska.track.marker.MarkerFeature;
import pl.roszkowska.track.marker.MarkerReducer;
import pl.roszkowska.track.marker.MarkerState;
import pl.roszkowska.track.statistics.StatisticsActor;
import pl.roszkowska.track.statistics.StatisticsEvent;
import pl.roszkowska.track.statistics.StatisticsFeature;
import pl.roszkowska.track.statistics.StatisticsReducer;
import pl.roszkowska.track.statistics.StatisticsState;
import pl.roszkowska.track.statistics.list.StatisticListActor;
import pl.roszkowska.track.statistics.list.StatisticListEvent;
import pl.roszkowska.track.statistics.list.StatisticListFeature;
import pl.roszkowska.track.statistics.list.StatisticListState;
import pl.roszkowska.track.statistics.list.StatisticsListReducer;

public class FeatureModule {
    private static FeatureModule sInstance;

    private final StatisticsFeature mStatisticsFeature;
    private final MarkerFeature mMarkerFeature;
    private final FollowFeature mFollowFeature;
    private final LocationFeature mLocationFeature;
    private final StatisticListFeature mStatisticListFeature;

    private FeatureModule(StatisticsFeature statisticsFeature,
                          MarkerFeature markerFeature,
                          FollowFeature followFeature,
                          LocationFeature locationFeature,
                          StatisticListFeature statisticListFeature) {
        mStatisticsFeature = statisticsFeature;
        mMarkerFeature = markerFeature;
        mFollowFeature = followFeature;
        mLocationFeature = locationFeature;
        mStatisticListFeature = statisticListFeature;
    }

    public static void setup(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Setup wolamy tylko raz!");
        }
        EventDispatcher eventDispatcher = EventDispatcherModule.getModule().getDispatcher();
        GpsLocationStream gpsLocationStream = new GpsLocationStream(context);
        sInstance = new FeatureModule(
                createHistogramFeature(eventDispatcher),
                createMarkerFeature(eventDispatcher, gpsLocationStream),
                createFollowFeature(eventDispatcher, gpsLocationStream),
                createLocationFeature(eventDispatcher, gpsLocationStream),
                createStatisticsListFeature(eventDispatcher));
    }

    private static StatisticsFeature createHistogramFeature(EventDispatcher eventDispatcher) {
        return new StatisticsFeature(
                new StatisticsState(),
                eventDispatcher.ofType(StatisticsEvent.class),
                new StatisticsActor(RepositoryModule.getModule().getRouteRepository()),
                new StatisticsReducer()
        );
    }

    private static FollowFeature createFollowFeature(EventDispatcher eventDispatcher,
                                                     GpsLocationStream locationStream) {
        return new FollowFeature(
                new FollowState(),
                eventDispatcher.ofType(FollowEvent.class),
                new FollowActor(RepositoryModule.getModule().getRouteRepository(), locationStream),
                new FollowReducer()
        );
    }

    private static MarkerFeature createMarkerFeature(EventDispatcher eventDispatcher,
                                                     GpsLocationStream locationStream) {
        return new MarkerFeature(
                new MarkerState(),
                eventDispatcher.ofType(MarkerEvent.class),
                new MarkerActor(
                        RepositoryModule.getModule().getMarkerRepository(),
                        locationStream.locationStream()
                ),
                new MarkerReducer()
        );
    }

    private static LocationFeature createLocationFeature(EventDispatcher eventDispatcher,
                                                         GpsLocationStream locationStream) {
        return new LocationFeature(
                new LocationState(),
                eventDispatcher.ofType(LocationEvent.class),
                new LocationActor(locationStream),
                new LocationReducer()
        );
    }

    private static StatisticListFeature createStatisticsListFeature(EventDispatcher eventDispatcher) {
        return new StatisticListFeature(
                new StatisticListState(),
                eventDispatcher.ofType(StatisticListEvent.class),
                new StatisticListActor(RepositoryModule.getModule().getRouteRepository()),
                new StatisticsListReducer()
        );
    }

    public StatisticsFeature getStatisticsFeature() {
        return mStatisticsFeature;
    }

    public StatisticListFeature getStatisticListFeature() {
        return mStatisticListFeature;
    }

    public MarkerFeature getMarkerFeature() {
        return mMarkerFeature;
    }

    public FollowFeature getFollowFeature() {
        return mFollowFeature;
    }

    public LocationFeature getLocationFeature() {
        return mLocationFeature;
    }

    public static FeatureModule getModule() {
        return sInstance;
    }
}
