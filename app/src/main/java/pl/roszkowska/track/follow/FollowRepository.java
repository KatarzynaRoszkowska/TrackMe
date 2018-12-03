package pl.roszkowska.track.follow;

import java.util.List;

import io.reactivex.Observable;

public interface FollowRepository {
    Observable<Long> createNewFollowRoute();

    Observable<Long> savePosition(long routeId,
                                  double lat,
                                  double lon,
                                  long timestamp,
                                  long distanceFromLastStep);

    Observable<StepInfo> getLastStep(long routeId);

    Observable<List<StepInfo>> getAllSteps(long routeId);

    class StepInfo {
        public final double lat;
        public final double lon;
        public final long timestamp;
        public final double distance;

        public StepInfo(double lat, double lon, long timestamp, double distance) {
            this.lat = lat;
            this.lon = lon;
            this.timestamp = timestamp;
            this.distance = distance;
        }

        public StepInfo() {
            this(-1, -1, -1, -1);
        }
    }
}
