package pl.roszkowska.track.ui;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.roszkowska.track.marker.State;

public class MyMapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private GoogleMap mMap; // TODO fix loading map async
    private Polyline mPolyline;
    private Map<Integer, MarkerOptions> mMarkers = new HashMap<>();
    private List<LatLng> mSteps = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void addNewStep(LatLng loc) {
        if (mPolyline == null) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(loc);
            mPolyline = mMap.addPolyline(polylineOptions);
            mPolyline.setColor(Color.RED);
            mPolyline.setWidth(5);
            mPolyline.setStartCap(new RoundCap());

        } else {
            List<LatLng> points = mPolyline.getPoints();
            points.add(loc);
            mPolyline.setPoints(points);
        }

        mSteps.add(loc);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getMaxZoomLevel() - 5));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    public void clearRoute() {
        mMap.clear();
        mPolyline = null;
        mSteps.clear();
        for (MarkerOptions marker : mMarkers.values()) {
            mMap.addMarker(marker);
        }
    }

    public void addMarker(List<State.MarkerEntity> markers) {
        for (State.MarkerEntity marker : markers) {
            if (mMarkers.containsKey(marker.id)) continue;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(marker.name);
            markerOptions.position(new LatLng(marker.lat, marker.lon));
            mMarkers.put(marker.id, markerOptions);
            mMap.addMarker(markerOptions);
        }
    }

    public void clearMarker() {
        mMap.clear();
        mMarkers.clear();
        PolylineOptions options = new PolylineOptions();
        for (LatLng latLng : mSteps) {
            options.add(latLng);
        }
        mPolyline = mMap.addPolyline(options);
    }

    public void clearMap() {
        mMap.clear();
        mMarkers.clear();
        mSteps.clear();
        mPolyline = null;
    }
}
