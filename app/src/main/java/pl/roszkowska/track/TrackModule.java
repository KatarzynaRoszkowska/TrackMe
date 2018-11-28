package pl.roszkowska.track;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import pl.roszkowska.track.database.DatabaseProvider;
import pl.roszkowska.track.database.FollowRepository;
import pl.roszkowska.track.database.MarkerRepository;
import pl.roszkowska.track.follow.Repository;

public class TrackModule {

    private static TrackModule sInstance;

    private final DatabaseProvider mDatabaseProvider;
    private final Repository mFollowRepository;
    private final pl.roszkowska.track.marker.Repository mMarkerRepository;

    private TrackModule(DatabaseProvider databaseProvider,
                        Repository followRepository,
                        pl.roszkowska.track.marker.Repository trackRepository) {
        mDatabaseProvider = databaseProvider;
        mFollowRepository = followRepository;
        mMarkerRepository = trackRepository;
    }

    public DatabaseProvider getDatabaseProvider() {
        return mDatabaseProvider;
    }

    public Repository getFollowRepository() {
        return mFollowRepository;
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
                /*new pl.roszkowska.track.marker.Repository() {
                    @Override
                    public Observable<Integer> savePoint(String name, double lat, double lon) {
                        return 0;
                    }
                })*/
                new MarkerRepository(databaseProvider.getDatabase()));
    }

    public pl.roszkowska.track.marker.Repository getMarkerRepository() {
        return mMarkerRepository;
    }

    public static TrackModule getModule() {
        return sInstance;
    }
}
