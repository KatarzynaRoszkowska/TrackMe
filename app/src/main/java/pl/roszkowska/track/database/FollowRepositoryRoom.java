package pl.roszkowska.track.database;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.follow.FollowRepository;

public class FollowRepositoryRoom implements FollowRepository {
    private final TrackDatabase mTrackDatabase;

    public FollowRepositoryRoom(TrackDatabase trackDatabase) {
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Long> savePosition(long routeId,
                                         double lat,
                                         double lon,
                                         long timestamp,
                                         long distanceFromLastStep) {
        return Observable.create(emitter -> {
            Log.w("DB", "Saving route id:" + routeId);
            StepEntity entity = new StepEntity();
            entity.routeId = routeId;
            entity.lat = lat;
            entity.lon = lon;
            entity.timestamp = timestamp;
            entity.distanceBetweenLastStep = distanceFromLastStep;
            long id = mTrackDatabase.daoStep().insertStep(entity);
            Log.w("DB", "Saved " + routeId + " STEP: " + id + " lat: " + lat + " lon: " + lon + " timestamp: " + timestamp);
            emitter.onNext(id);
            emitter.onComplete();
        }).cast(Long.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<StepInfo> getLastStep(long routeId) {
        return Observable.create(emitter -> {
            StepEntity lastStep = mTrackDatabase.daoStep().getLastStep(routeId);
            if (lastStep != null) {
                emitter.onNext(new StepInfo(
                        lastStep.stepId,
                        lastStep.lat,
                        lastStep.lon,
                        lastStep.timestamp,
                        lastStep.distanceBetweenLastStep)
                );
            } else {
                emitter.onNext(new StepInfo()); // blank
            }
            emitter.onComplete();
        }).cast(StepInfo.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<StepInfo>> getAllSteps(long routeId) {
        return Observable.create(emitter -> {
            List<StepEntity> steps = mTrackDatabase.daoStep().getRouteSteps(routeId);

            for (StepEntity entity : steps) {
                emitter.onNext(new StepInfo(
                        entity.stepId,
                        entity.lat,
                        entity.lon,
                        entity.timestamp,
                        entity.distanceBetweenLastStep)
                );
            }

            emitter.onComplete();
        }).cast(StepInfo.class)
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
