package pl.roszkowska.track.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pl.roszkowska.track.follow.FollowState;
import pl.roszkowska.track.marker.MarkerState;
import pl.roszkowska.track.statistics.StatisticsState;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MyMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 455;
    private static boolean showRationaleOnce;

    private GoogleMap mMap; // TODO fix loading map async
    private Map<Long, MarkerOptions> mMarkers = new HashMap<>();
    private List<LatLng> mSteps = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        checkLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals(ACCESS_FINE_LOCATION) &&
                    grantResults[0] != PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                    if (!showRationaleOnce) {
                        showRationaleOnce = true;
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Musisz recznie dac mi pozwolenie na lokalizacje, wejdz u stawienia")
                                .show();
                    }
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Musze wiedziec gdzie idziesz aby ustalic trase!")
                            .setOnDismissListener(dialog -> checkLocationPermission())
                            .show();
                }
            }
        }
    }

    public void checkLocationPermission() {
        FragmentActivity activity = Objects.requireNonNull(this.getActivity());
        if (checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) return;
        requestPermissions(
                new String[]{ACCESS_FINE_LOCATION},
                MY_LOCATION_REQUEST_CODE);

    }

    @Override
    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        super.getMapAsync(googleMap -> {
            MyMapFragment.this.onMapReady(googleMap);
            onMapReadyCallback.onMapReady(googleMap);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void setSteps(List<LatLng> steps) {
        clearRoute();
        mSteps = steps;
        if (steps.isEmpty()) return;

        updateMapViewSteps();
    }

    private void updateMapViewSteps() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(mSteps);
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        mMap.addPolyline(polylineOptions);

        mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getMaxZoomLevel() - 5));
        LatLng lastLatLon = mSteps.get(mSteps.size() - 1);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLatLon));
    }

    public void setMarkers(List<MarkerState.MarkerItem> markers) {
        for (MarkerState.MarkerItem marker : markers) {
            if (mMarkers.containsKey(marker.id)) continue;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(marker.name);
            markerOptions.position(new LatLng(marker.lat, marker.lon));
            mMarkers.put(marker.id, markerOptions);
            mMap.addMarker(markerOptions);
        }
    }

    public void clearRoute() {
        mMap.clear();
        mSteps.clear();
        for (MarkerOptions marker : mMarkers.values()) {
            mMap.addMarker(marker);
        }
    }

    public void clearMarker() {
        mMap.clear();
        mMarkers.clear();
        updateMapViewSteps();
    }

    public void clearMap() {
        mMap.clear();
        mMarkers.clear();
        mSteps.clear();
    }

    public static List<LatLng> convertFromFollowStateStep(List<FollowState.Step> in) {
        List<LatLng> latLng = new ArrayList<>();
        for (FollowState.Step step : in) {
            latLng.add(new LatLng(step.lat, step.lon));
        }
        return latLng;
    }

    public static List<LatLng> convertFromStatisticsState(List<StatisticsState.RouteStatistics.Step> in) {
        List<LatLng> latLng = new ArrayList<>();
        for (StatisticsState.RouteStatistics.Step step : in) {
            latLng.add(new LatLng(step.lat, step.lon));
        }
        return latLng;
    }
}