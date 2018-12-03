package pl.roszkowska.track.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class GpsLocationStream implements LocationListener {

    private final LocationManager mLocationManager;
    private final Subject<LocationInfo> mLocationSubject = BehaviorSubject.create();

    public GpsLocationStream(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public Observable<LocationInfo> locationStream() {
        return mLocationSubject
                .distinctUntilChanged((info1, info2) -> info1.lon == info2.lon && info1.lat == info2.lat)
                .share();
    }

    @SuppressLint("MissingPermission")
    public void start() {
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1,
                this);
    }

    public void stop() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationSubject.onNext(new LocationInfo(location.getLatitude(), location.getLongitude()));
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
