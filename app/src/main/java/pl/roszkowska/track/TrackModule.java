package pl.roszkowska.track;

import android.content.Context;

import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.common.RxEventDispatcher;
import pl.roszkowska.track.database.DatabaseProvider;
import pl.roszkowska.track.database.FollowRepository;
import pl.roszkowska.track.database.MarkerRepository;
import pl.roszkowska.track.follow.Repository;

public class TrackModule {

    private static TrackModule sInstance;

    private final DatabaseProvider mDatabaseProvider;
    private final Repository mFollowRepository;
    private final pl.roszkowska.track.marker.Repository mMarkerRepository;
    private final EventDispatcher mEventDispatcher;

    private TrackModule(DatabaseProvider databaseProvider,
                        Repository followRepository,
                        pl.roszkowska.track.marker.Repository trackRepository,
                        EventDispatcher eventDispatcher) {
        mDatabaseProvider = databaseProvider;
        mFollowRepository = followRepository;
        mMarkerRepository = trackRepository;
        mEventDispatcher = eventDispatcher;
    }

    public DatabaseProvider getDatabaseProvider() {
        return mDatabaseProvider;
    }

    public Repository getFollowRepository() {
        return mFollowRepository;
    }

    public EventDispatcher getEventDispatcher() {
        return mEventDispatcher;
    }

    public pl.roszkowska.track.marker.Repository getTrackRepository() {
        return mMarkerRepository;
    }

    public static void setup(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Setup wolamy tylko raz!");
        }
        DatabaseProvider databaseProvider = new DatabaseProvider(context);
        sInstance = new TrackModule(databaseProvider,
                new FollowRepository(databaseProvider.getDatabase()),
                new MarkerRepository(databaseProvider.getDatabase()),
                new RxEventDispatcher());
    }

    public pl.roszkowska.track.marker.Repository getMarkerRepository() {
        return mMarkerRepository;
    }

    public static TrackModule getModule() {
        return sInstance;
    }
}
