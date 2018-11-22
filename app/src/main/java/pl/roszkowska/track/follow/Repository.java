package pl.roszkowska.track.follow;

import io.reactivex.Observable;

public interface Repository {
    Observable<Long> createNewFollowRoute();

    Observable<Long> savePosition(long routeId, double lat, double lon);
}
