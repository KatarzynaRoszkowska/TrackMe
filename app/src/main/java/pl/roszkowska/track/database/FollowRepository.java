package pl.roszkowska.track.database;

import android.util.Log;

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
    public Observable<Long> savePosition(long routeId,
                                         double lat,
                                         double lon,
                                         long timestamp,
                                         long distanceFromLastStep) {
        return Observable.create(emitter -> {
            Log.w("DB", "Saving " + routeId);
            StepEntity entity = new StepEntity();
            entity.routeId = routeId;
            entity.lat = lat;
            entity.lon = lon;
            entity.timestamp = timestamp;
            entity.distanceBetweenLastStep = distanceFromLastStep;
            long id = mTrackDatabase.daoStep().insertStep(entity);
            Log.w("DB", "Saved " + routeId + " STEP: " + id);
            emitter.onNext(id);
            emitter.onComplete();
        }).cast(Long.class)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<StepInfo> getLastStep(long routeId) {
        return Observable.create(emitter -> {
            Log.w("DB", "Reading last route step " + routeId);
            StepEntity lastStep = mTrackDatabase.daoStep().getLastStep(routeId);
            Log.w("DB", "Finish reading last route step " + routeId + " STEP: " + (lastStep == null ? "NULL" : lastStep.stepId));
            if (lastStep != null) {
                emitter.onNext(new StepInfo(lastStep.lat, lastStep.lon, lastStep.timestamp));
            } else {
                emitter.onNext(new StepInfo()); // blank
            }
            emitter.onComplete();
        }).cast(StepInfo.class)
                .subscribeOn(Schedulers.io());
    }
}
