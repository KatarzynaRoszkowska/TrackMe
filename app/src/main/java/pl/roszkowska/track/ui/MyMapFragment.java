package pl.roszkowska.track.ui;

import android.graphics.Color;
import android.os.Bundle;

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

import pl.roszkowska.track.follow.FollowState;

import static pl.roszkowska.track.marker.MarkerState.MarkerEntity;

public class MyMapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private GoogleMap mMap; // TODO fix loading map async
    private Map<Long, MarkerOptions> mMarkers = new HashMap<>();
    private List<LatLng> mSteps = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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

    public void setSteps(List<FollowState.Step> steps) {
        clearRoute();
        mSteps = convert(steps);
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

    private List<LatLng> convert(List<FollowState.Step> in) {
        List<LatLng> latLng = new ArrayList<>();
        for (FollowState.Step step : in) {
            latLng.add(new LatLng(step.lat, step.lon));
        }
        return latLng;
    }

    public void setMarkers(List<MarkerEntity> markers) {
        for (MarkerEntity marker : markers) {
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
}
