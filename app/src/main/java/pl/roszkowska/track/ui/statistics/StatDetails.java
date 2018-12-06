package pl.roszkowska.track.ui.statistics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.statistics.StatisticsEvent;
import pl.roszkowska.track.statistics.StatisticsState;
import pl.roszkowska.track.ui.GraphBinder;

public class StatDetails extends AppCompatActivity {
    private TextView startTimeStamp, distance, time, avgSpeed, maxSpeed;
    private Long routeId;

    private GraphBinder mGraphBinder;
    private EventDispatcher mEventDispatcher = TrackModule.getEventDispatcher();
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_view_details);

        mGraphBinder = new GraphBinder(findViewById(R.id.deailsGraph));

        startTimeStamp = findViewById(R.id.detailsStartTimeStamp);
        distance = findViewById(R.id.detailsDistance);
        time = findViewById(R.id.detailsTime);
        avgSpeed = findViewById(R.id.deatilsAvgSpeed);
        maxSpeed = findViewById(R.id.detailsMaxSpeed);

        startTimeStamp.setText(getIntent().getExtras().getString("startTimeStamp"));
        distance.setText(getIntent().getExtras().getString("distance"));
        time.setText(getIntent().getExtras().getString("time"));
        avgSpeed.setText(getIntent().getExtras().getString("avgSpeed"));
        maxSpeed.setText(getIntent().getExtras().getString("maxSpeed"));

        routeId = getIntent().getExtras().getLong("routeID");
        routeId = 27L;

        mDisposable.add(TrackModule
                .histogramStateStream()
                .subscribe(this::update)
        );
        mDisposable.add(Observable
                .interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(aLong -> mEventDispatcher.sendEvent(new StatisticsEvent.ReadRoute(routeId)))
        );
    }

    private void update(StatisticsState state) {
        mGraphBinder.bind(state);

        avgSpeed.setText(String.valueOf(state.averageSpeed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDisposable.clear();
    }
}
