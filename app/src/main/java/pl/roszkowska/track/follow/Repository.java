package pl.roszkowska.track.follow;

import io.reactivex.Observable;

public interface Repository {
    Observable<Long> createNewFollowRoute();

    Observable<Long> savePosition(long routeId,
                                  double lat,
                                  double lon,
                                  long timestamp,
                                  long distanceFromLastStep);

    Observable<StepInfo> getLastStep(long routeId);

    class StepInfo {
        public final double lat;
        public final double lon;
        public final double timestamp;

        public StepInfo(double lat, double lon, double timestamp) {
            this.lat = lat;
            this.lon = lon;
            this.timestamp = timestamp;
        }

        public StepInfo() {
            this(-1, -1, -1);
        }
    }
}
