package pl.roszkowska.track.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class GpsLocationProvider implements LocationProvider, LocationListener {

    private final LocationManager mLocationManager;
    private final Subject<Location> mLocationSubject = PublishSubject.create();

    public GpsLocationProvider(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public Observable<Location> locationStream() {
        return mLocationSubject;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void start() {
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1,
                this);
    }

    @Override
    public void stop() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationSubject.onNext(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
