package pl.roszkowska.track.module;

import android.content.Context;

import pl.roszkowska.track.database.DatabaseProvider;
import pl.roszkowska.track.database.MarkerRepositoryRoom;
import pl.roszkowska.track.database.RouteRepositoryRoom;
import pl.roszkowska.track.follow.RouteRepository;
import pl.roszkowska.track.marker.MarkerRepository;

public class RepositoryModule {
    private static RepositoryModule sInstance;

    private final DatabaseProvider mDatabaseProvider;
    private final RouteRepository mRouteRepository;
    private final MarkerRepository mMarkerRepository;


    private RepositoryModule(DatabaseProvider databaseProvider,
                             RouteRepository routeRepository,
                             MarkerRepository markerRepository) {
        mDatabaseProvider = databaseProvider;
        mRouteRepository = routeRepository;
        mMarkerRepository = markerRepository;
    }

    public static void setup(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Setup wolamy tylko raz!");
        }
        DatabaseProvider databaseProvider = new DatabaseProvider(context);
        sInstance = new RepositoryModule(databaseProvider,
                new RouteRepositoryRoom(databaseProvider.getDatabase()),
                new MarkerRepositoryRoom(databaseProvider.getDatabase()));
    }

    public static RepositoryModule getModule() {
        return sInstance;
    }

    public RouteRepository getRouteRepository() {
        return mRouteRepository;
    }

    public MarkerRepository getMarkerRepository() {
        return mMarkerRepository;
    }
}
