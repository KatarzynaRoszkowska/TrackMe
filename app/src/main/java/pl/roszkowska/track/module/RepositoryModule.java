package pl.roszkowska.track.module;

import android.content.Context;

import pl.roszkowska.track.database.DatabaseProvider;
import pl.roszkowska.track.database.FollowRepositoryRoom;
import pl.roszkowska.track.database.MarkerRepositoryRoom;
import pl.roszkowska.track.follow.FollowRepository;
import pl.roszkowska.track.marker.MarkerRepository;

public class RepositoryModule {
    private static RepositoryModule sInstance;

    private final DatabaseProvider mDatabaseProvider;
    private final FollowRepository mFollowRepository;
    private final MarkerRepository mMarkerRepository;


    private RepositoryModule(DatabaseProvider databaseProvider,
                             FollowRepository followRepository,
                             MarkerRepository markerRepository) {
        mDatabaseProvider = databaseProvider;
        mFollowRepository = followRepository;
        mMarkerRepository = markerRepository;
    }

    public static void setup(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Setup wolamy tylko raz!");
        }
        DatabaseProvider databaseProvider = new DatabaseProvider(context);
        sInstance = new RepositoryModule(databaseProvider,
                new FollowRepositoryRoom(databaseProvider.getDatabase()),
                new MarkerRepositoryRoom(databaseProvider.getDatabase()));
    }

    public static RepositoryModule getModule() {
        return sInstance;
    }

    public FollowRepository getFollowRepository() {
        return mFollowRepository;
    }

    public MarkerRepository getMarkerRepository() {
        return mMarkerRepository;
    }
}
