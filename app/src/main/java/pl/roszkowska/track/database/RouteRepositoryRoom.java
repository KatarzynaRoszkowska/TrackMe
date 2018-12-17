package pl.roszkowska.track.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.roszkowska.track.follow.RouteRepository;

public class RouteRepositoryRoom implements RouteRepository {
    private final TrackDatabase mTrackDatabase;

    public RouteRepositoryRoom(TrackDatabase trackDatabase) {
        mTrackDatabase = trackDatabase;
    }

    @Override
    public Observable<Long> createNewRoute() {
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
        return Observable.create((ObservableEmitter<Long> emitter) -> {
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
        })
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
        return Observable.create((ObservableEmitter<List<StepInfo>> emitter) -> {
            List<StepEntity> steps = mTrackDatabase.daoStep().getRouteSteps(routeId);

            List<StepInfo> stepInfos = new ArrayList<>();
            for (StepEntity entity : steps) {
                stepInfos.add(new StepInfo(
                        entity.stepId,
                        entity.lat,
                        entity.lon,
                        entity.timestamp,
                        entity.distanceBetweenLastStep)
                );
            }
            emitter.onNext(stepInfos);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<RouteInfo>> getAllRoutes() {
        return Observable.create((ObservableEmitter<List<RouteInfo>> emitter) ->
                emitter.onNext(readRouteInfo())
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<RouteInfo> readRouteInfo() {
        List<RouteInfo> routeInfoList = new ArrayList<>();
        List<Long> routes = mTrackDatabase.daoRoute().getRouteIds();

        for (Long routeId : routes) {
            StepEntity firstStep = mTrackDatabase.daoStep().getFirstStep(routeId);
            StepEntity lastStep = mTrackDatabase.daoStep().getLastStep(routeId);

            if (firstStep == null || lastStep == null || firstStep.stepId == lastStep.stepId)
                continue;

            routeInfoList.add(new RouteInfo(
                    routeId,
                    lastStep.timestamp - firstStep.timestamp,
                    firstStep.timestamp
            ));
        }

        return routeInfoList;
    }
}
