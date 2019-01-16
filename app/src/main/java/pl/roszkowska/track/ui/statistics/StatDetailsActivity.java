package pl.roszkowska.track.ui.statistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.statistics.StatisticsEvent;
import pl.roszkowska.track.statistics.StatisticsState.RouteStatistics;
import pl.roszkowska.track.ui.GraphBinder;

public class StatDetailsActivity extends AppCompatActivity {
    private TextView  distance, time, avgSpeed, maxSpeed;

    private GraphBinder mGraphBinder;
    private EventDispatcher mEventDispatcher = TrackModule.getEventDispatcher();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Disposable mUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_view_details);

        mGraphBinder = new GraphBinder(findViewById(R.id.deailsGraph));

        distance = findViewById(R.id.detailsDistance);
        time = findViewById(R.id.detailsTime);
        avgSpeed = findViewById(R.id.deatilsAvgSpeed);
        maxSpeed = findViewById(R.id.detailsMaxSpeed);


        long routeId = IntentCreator.readRouteId(getIntent());

        mDisposable.add(TrackModule
                .histogramStateStream(routeId)
                .subscribe(this::update)
        );


        findViewById(R.id.detailsShowOnMap).setOnClickListener(v -> {
            startActivity(MapRoutePreview.IntentHelper.create(this, routeId));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        long routeId = IntentCreator.readRouteId(getIntent());
        if (mUpdater != null) mUpdater.dispose();
        mUpdater = Observable
                .interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(aLong -> mEventDispatcher.sendEvent(new StatisticsEvent.ReadRoute(routeId)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mUpdater != null)
            mUpdater.dispose(); // musimy zatrzymac kod aby nie updatewoal nam wiecznie jak odpalimy inne aktiwity z tego aktiwity
    }

    private void update(RouteStatistics state) {
        mGraphBinder.bind(state);
        double dist = (double) state.trackLength / 1000; //km
        distance.setText(String.valueOf(dist));
        avgSpeed.setText(String.valueOf(state.averageSpeed));
        time.setText(String.valueOf(state.trackTime / 1000));
        maxSpeed.setText(String.valueOf(state.maxSpeed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDisposable.clear();
        mUpdater.dispose();
    }

    public static class IntentCreator {
        private static String ROUTE_ID = IntentCreator.class.getName() + "ROUTE_ID";

        public static Intent createIntent(Context context, long routeId) {
            Intent intent = new Intent(context, StatDetailsActivity.class);
            intent.putExtra(ROUTE_ID, routeId);
            return intent;
        }

        private static long readRouteId(Intent intent) {
            return intent.getLongExtra(ROUTE_ID, -1);
        }
    }
}
