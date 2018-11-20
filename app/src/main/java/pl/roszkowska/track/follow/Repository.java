package pl.roszkowska.track.follow;

public interface Repository {
    int createNewFollowRoute();

    void savePosition(int routeId, double lat, double lon);
}
