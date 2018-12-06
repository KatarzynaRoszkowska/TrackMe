package pl.roszkowska.track.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import pl.roszkowska.track.R;

public class StatDetails extends AppCompatActivity {
    private TextView startTimeStamp, distance, time, avgSpeed, maxSpeed;
    private String routeId;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic_view_details);

        GraphView graph = findViewById(R.id.deailsGraph);

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

        routeId = getIntent().getExtras().getString("routeID");

        //TODO tutaj trzeba na podstawie przekazane RouteId pobrac wszystkie kroki
        series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1,2),
                new DataPoint(2,3)

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

        graph.addSeries(series);


    }
}
