package pl.roszkowska.track.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.histogram.HistogramActor;
import pl.roszkowska.track.histogram.HistogramEvent;
import pl.roszkowska.track.histogram.HistogramFeature;
import pl.roszkowska.track.histogram.HistogramReducer;
import pl.roszkowska.track.histogram.HistogramState;
import pl.roszkowska.track.module.RepositoryModule;
import pl.roszkowska.track.module.TrackModule;


public class RealTimeGraph extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;

    private EventDispatcher mEventDispatcher = TrackModule.getEventDispatcher();
    private HistogramFeature mHistogramFeature;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        long routeId = getIntent().getExtras().getLong("routeId");
        GraphView graph = findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1)

        });
        series.setColor(Color.RED);
        graph.addSeries(series);

        graph.getViewport().setMinX(0);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().scrollToEnd();
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("t [s]");
        gridLabel.setVerticalAxisTitle("s [m]");

        mHistogramFeature = new HistogramFeature(new HistogramState(),
                mEventDispatcher.ofType(HistogramEvent.class),
                new HistogramActor(RepositoryModule.getModule().getFollowRepository()),
                new HistogramReducer()
        );

        mCompositeDisposable.add(
                mHistogramFeature.states.subscribe(state -> {
                    for (HistogramState.Step step : state.steps) {
                        //Log.w("HIST", "Distance: " + step.distance + " time: " + step.time);
                        series.appendData(new DataPoint(step.time, step.distance), false, 100);
                    }
                })
        );

        //mEventDispatcher.ofType(pl.roszkowska.track.follow.LocationEvent.NewStep)
        mEventDispatcher.sendEvent(new HistogramEvent.ReadRoute(routeId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

}


