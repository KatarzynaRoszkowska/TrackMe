package pl.roszkowska.track.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.histogram.HistogramEvent;
import pl.roszkowska.track.histogram.HistogramState;
import pl.roszkowska.track.module.TrackModule;


public class RealTimeGraph extends AppCompatActivity {

    public static final String ROUTE_ID = "routeId";


    private EventDispatcher mEventDispatcher = TrackModule.getEventDispatcher();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private GraphView mGraph;
    private long mLastStepId = -1;
    private LineGraphSeries<DataPoint> mSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        long routeId = getIntent().getExtras().getLong(ROUTE_ID);
        mGraph = findViewById(R.id.graph);
//        mGraph.getViewport().setMinX(0);
        mGraph.getViewport().setScalable(true);
        mGraph.getViewport().setScalableY(true);
        mGraph.getViewport().setScrollable(true);
        mGraph.getViewport().setScrollableY(true);
        GridLabelRenderer gridLabel = mGraph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("t [s]");
        gridLabel.setVerticalAxisTitle("s [m]");

        mDisposable.add(TrackModule.histogramStateStream().subscribe(this::updateUi));
        mDisposable.add(Observable
                .interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(aLong -> mEventDispatcher.sendEvent(new HistogramEvent.ReadRoute(routeId)))
        );
    }

    private void updateUi(HistogramState state) {
        if (state.steps.isEmpty()) return;
        if (mLastStepId == -1) {
            mSeries = new LineGraphSeries<>();
            mSeries.setColor(Color.RED);
            mGraph.addSeries(mSeries);
        }
        for (HistogramState.Step step : state.steps) {
            if (mLastStepId < step.id)
                mSeries.appendData(new DataPoint(
                                step.time,
                                step.distance),
                        false,
                        100
                );
        }
        mLastStepId = state.steps.get(state.steps.size() - 1).id;
        mGraph.getViewport().scrollToEnd();
        mGraph.getViewport().setMinX(mGraph.getViewport().getMinX(true));
        mGraph.getViewport().setMaxX(mGraph.getViewport().getMaxX(true));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

}


