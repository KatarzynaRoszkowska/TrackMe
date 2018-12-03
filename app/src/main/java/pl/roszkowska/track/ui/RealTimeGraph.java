package pl.roszkowska.track.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.TrackModule;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.histogram.Event;
import pl.roszkowska.track.histogram.HistogramActor;
import pl.roszkowska.track.histogram.HistogramFeature;
import pl.roszkowska.track.histogram.HistogramReducer;
import pl.roszkowska.track.histogram.State;


public class RealTimeGraph extends AppCompatActivity {

    private Handler mHandler = new Handler();
    LineGraphSeries<DataPoint> series;
    private double lastXPoint = 2;
    private Random random = new Random();

    private EventDispatcher mEventDispatcher = TrackModule.getModule().getEventDispatcher();
    private HistogramFeature mHistogramFeature;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        long routeId = getIntent().getExtras().getLong("routeId");
        GraphView graph = (GraphView) findViewById(R.id.graph);
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

        addMyPoint();

//        mHistogramFeature = new HistogramFeature(new State(),
//                mEventDispatcher.ofType(Event.class),
//                new HistogramActor(TrackModule.getModule().getFollowRepository()),
//                new HistogramReducer()
//        );
//
//        mCompositeDisposable.add(
//                mHistogramFeature.states.subscribe(state -> {
//                    for (State.Step step : state.steps) {
//                        Log.w("HIST", "Distance: " + step.distance + " time: " + step.time);
//                    }
//                })
//        );

        mEventDispatcher.sendEvent(new Event.ReadRoute(routeId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private void addMyPoint() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHistogramFeature = new HistogramFeature(new State(),
                        mEventDispatcher.ofType(Event.class),
                        new HistogramActor(TrackModule.getModule().getFollowRepository()),
                        new HistogramReducer()
                );

                mCompositeDisposable.add(
                        mHistogramFeature.states.subscribe(state -> {
                            for (State.Step step : state.steps) {
                                //Log.w("HIST", "Distance: " + step.distance + " time: " + step.time);
                                series.appendData(new DataPoint(step.time, step.distance), false, 100);
                            }
                        })
                );

                //lastXPoint++;
                //series.appendData(new DataPoint(lastXPoint, random.nextInt(10)), false, 100);

                addMyPoint();
            }
        }, 1000);
    }
}


