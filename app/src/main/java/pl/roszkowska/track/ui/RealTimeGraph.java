package pl.roszkowska.track.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import pl.roszkowska.track.R;


public class RealTimeGraph extends AppCompatActivity {

    private Handler mHandler = new Handler();
    LineGraphSeries<DataPoint> series;
    private double lastXPoint =2;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1)

        });
        series.setColor(Color.RED);
        graph.addSeries(series);

        graph.getViewport().setMinX(0);
        //graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().scrollToEnd();


        addMyPoint();
    }

    private void addMyPoint(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastXPoint++;
                series.appendData(new DataPoint(lastXPoint, random.nextInt(10)),false, 100);
                addMyPoint();
            }
        },1000);
    }
}


