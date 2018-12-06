package pl.roszkowska.track.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.statistics.StatisticsEvent;


public class RealTimeGraph extends AppCompatActivity {

    public static final String ROUTE_ID = "routeId";

    private EventDispatcher mEventDispatcher = TrackModule.getEventDispatcher();
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        long routeId = getIntent().getExtras().getLong(ROUTE_ID);

        GraphBinder graphBinder = new GraphBinder(findViewById(R.id.graph));
        mDisposable.add(TrackModule
                .histogramStateStream()
                .subscribe(graphBinder::bind)
        );
        mDisposable.add(Observable
                .interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(aLong -> mEventDispatcher.sendEvent(new StatisticsEvent.ReadRoute(routeId)))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

}


