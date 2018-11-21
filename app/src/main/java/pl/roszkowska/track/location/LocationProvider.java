package pl.roszkowska.track.location;

import android.location.Location;

import io.reactivex.Observable;

public interface LocationProvider {
    Observable<Location> locationStream();

    void start();

    void stop();
}
