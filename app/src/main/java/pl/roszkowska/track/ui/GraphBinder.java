package pl.roszkowska.track.ui;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import pl.roszkowska.track.statistics.StatisticsState.RouteStatistics;
import pl.roszkowska.track.statistics.StatisticsState.RouteStatistics.Step;

public class GraphBinder {
    private final GraphView mGraphView;

    public GraphBinder(GraphView graphView) {
        mGraphView = graphView;
        mGraphView.getViewport().setScalable(true);
        mGraphView.getViewport().setScalableY(true);
        mGraphView.getViewport().setScrollable(true);
        mGraphView.getViewport().setScrollableY(true);

        GridLabelRenderer gridLabel = mGraphView.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("t [h]");
        gridLabel.setVerticalAxisTitle("s [km]");
    }

    public void bind(RouteStatistics state) {
        if (state.steps.isEmpty()) return;

        LineGraphSeries series = new LineGraphSeries<>(convert(state.steps));
        series.setColor(Color.RED);
        mGraphView.removeAllSeries();
        mGraphView.addSeries(series);

        mGraphView.getViewport().scrollToEnd();
        mGraphView.getViewport().setMinX(mGraphView.getViewport().getMinX(true));
        mGraphView.getViewport().setMaxX(mGraphView.getViewport().getMaxX(true));
    }

    private DataPointInterface[] convert(List<Step> steps) {
        DataPointInterface[] points = new DataPointInterface[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            points[i] = new DataPoint(
                    (double) step.time/60/60,
                    (double)step.distance/1000);
        }

        return points;
    }
}
