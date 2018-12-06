package pl.roszkowska.track.ui;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import pl.roszkowska.track.statistics.StatisticsState;

class GraphBinder {
    private final GraphView mGraphView;

    GraphBinder(GraphView graphView) {
        mGraphView = graphView;
        mGraphView.getViewport().setScalable(true);
        mGraphView.getViewport().setScalableY(true);
        mGraphView.getViewport().setScrollable(true);
        mGraphView.getViewport().setScrollableY(true);

        GridLabelRenderer gridLabel = mGraphView.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("t [s]");
        gridLabel.setVerticalAxisTitle("s [m]");
    }

    public void bind(StatisticsState state) {
        if (state.steps.isEmpty()) return;

        LineGraphSeries series = new LineGraphSeries<>(convert(state.steps));
        series.setColor(Color.RED);
        mGraphView.removeAllSeries();
        mGraphView.addSeries(series);

        mGraphView.getViewport().scrollToEnd();
        mGraphView.getViewport().setMinX(mGraphView.getViewport().getMinX(true));
        mGraphView.getViewport().setMaxX(mGraphView.getViewport().getMaxX(true));
    }

    private DataPointInterface[] convert(List<StatisticsState.Step> steps) {
        DataPointInterface[] points = new DataPointInterface[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            StatisticsState.Step step = steps.get(i);
            points[i] = new DataPoint(
                    step.time,
                    step.distance);
        }
        return points;
    }
}
