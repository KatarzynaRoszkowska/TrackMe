package pl.roszkowska.track;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DrawMyRoute extends FragmentActivity implements OnMapReadyCallback {
    private static int REQUEST_PERMISSION = 100;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        startListeningToLocationUpdates();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSION);
    }

    private void startListeningToLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestPermission();
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                new AlertDialog.Builder(this)
                        .setTitle("Uwaga")
                        .setMessage("Potrzebujemy GPS do poprawnej funkcjonalnosci tej aplikacji")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        })
                        .show();
            }
        } else {
            startListeningToLocationUpdates();
        }
    }

    class MyLocationListener implements LocationListener {
        private Polyline options;

        @Override
        public void onLocationChanged(Location loc) {

            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            if (options == null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(latLng);
                options = mMap.addPolyline(polylineOptions);
                options.setColor(Color.RED);
                options.setWidth(5);
                options.setStartCap(new RoundCap());

            } else {
                List<LatLng> points = options.getPoints();
                points.add(latLng);
                options.setPoints(points);
            }

            mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getMaxZoomLevel() - 5));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
