package pl.roszkowska.track.marker;

public interface Repository {

    int savePoint(String name, double lat, double lon);
}
