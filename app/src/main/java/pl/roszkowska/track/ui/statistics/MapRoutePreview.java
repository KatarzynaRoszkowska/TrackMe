package pl.roszkowska.track.ui.statistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.statistics.StatisticsEvent;
import pl.roszkowska.track.statistics.StatisticsState;
import pl.roszkowska.track.ui.MyMapFragment;

public class MapRoutePreview extends AppCompatActivity {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MyMapFragment mFragment;
    private boolean isMapReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_route_preview);

        mFragment = (MyMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRoutePreview_fragment);
        mFragment.getMapAsync(googleMap -> {
            isMapReady = true;
            subscribeFeatures();
        });
        subscribeFeatures();
    }

    private void subscribeFeatures() {
        if (!isMapReady) return;
        long routeId = IntentHelper.getRouteId(getIntent());
        mDisposable.add(TrackModule
                .histogramStateStream(routeId)
                .subscribe(this::update));
        TrackModule.getEventDispatcher().sendEvent(new StatisticsEvent.ReadRoute(routeId));
    }

    private void update(StatisticsState.RouteStatistics state) {
        mFragment.setSteps(MyMapFragment.convertFromStatisticsState(state.steps));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    public static class IntentHelper {
        private static final String ROUTE_ID = IntentHelper.class.getName() + "ROUTE_ID";

        public static Intent create(Context context, long routeId) {
            Intent intent = new Intent(context, MapRoutePreview.class);
            intent.putExtra(ROUTE_ID, routeId);
            return intent;
        }

        private static long getRouteId(Intent intent) {
            return intent.getLongExtra(ROUTE_ID, -1);
        }
    }

}
