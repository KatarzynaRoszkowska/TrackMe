package pl.roszkowska.track.marker;

public interface Repository {
    int createNewPoint();

    void savePoint(int markId, String name);
}
