package pl.roszkowska.track.database;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.follow.Repository;

public class FollowRepository implements Repository {
    private final TrackDatabase mTrackDatabase;

    public FollowRepository(TrackDatabase trackDatabase) {
        mTrackDatabase = trackDatabase;
    }

    @Override
    public Observable<Long> createNewFollowRoute() {
        return Observable.create(emitter -> {
            RouteEntity entity = new RouteEntity();
            entity.name = "Route 1";
            long id = mTrackDatabase.daoRoute().createRoute(entity);

            emitter.onNext(id);
            emitter.onComplete();
        }).cast(Long.class)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Long> savePosition(long routeId, double lat, double lon) {
        return Observable.create(emitter -> {

            StepEntity entity = new StepEntity();
            entity.routeId = routeId;
            entity.lat = lat;
            entity.lon = lon;
            long id = mTrackDatabase.daoStep().insertStep(entity);

            emitter.onNext(id);
            emitter.onComplete();
        }).cast(Long.class)
                .subscribeOn(Schedulers.io());
    }
}
