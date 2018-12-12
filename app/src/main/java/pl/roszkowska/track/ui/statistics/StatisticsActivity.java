package pl.roszkowska.track.ui.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.roszkowska.track.R;
import pl.roszkowska.track.database.RouteEntity;


public class StatisticsActivity extends AppCompatActivity{
    String startTimeStamp;
    String distance;
    String time;
    String routeId;
    String nameRoute;
    String maxSpeed;
    String avgSpeed;
    List<RouteEntity> routeEntityList;
    RecyclerView recyclerView;
    StatisticsAdapter statisticsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_recycler_view);

        routeEntityList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListOfRoutes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO tutaj trzeba pobierac z repozytorium
        RouteEntity e  = new RouteEntity(1L,"pierwsza");
        RouteEntity e2  = new RouteEntity(2L,"druga");
        RouteEntity e3  = new RouteEntity(3L,"trzecia");
        RouteEntity e4  = new RouteEntity(4L,"czwarta");
        RouteEntity e5  = new RouteEntity(5L,"piąta");
        RouteEntity e6  = new RouteEntity(6L,"szósta");
        routeEntityList.add(e);
        routeEntityList.add(e2);
        routeEntityList.add(e3);
        routeEntityList.add(e4);
        routeEntityList.add(e5);
        routeEntityList.add(e6);

        statisticsAdapter = new StatisticsAdapter(this, routeEntityList);

        recyclerView.setAdapter(statisticsAdapter);


        statisticsAdapter.setOnItemClickListener(new StatisticsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                //To będą parametry do przekazania do szczegółów statystyk
                routeId = statisticsAdapter.getRouteEntityList().get(position).getName();
                startTimeStamp = statisticsAdapter.getRouteEntityList().get(position).getName(); //TODO
                distance = statisticsAdapter.getRouteEntityList().get(position).getName(); //TODO
                time = statisticsAdapter.getRouteEntityList().get(position).getName();// TODO
                nameRoute = statisticsAdapter.getRouteEntityList().get(position).getName();// TODO
                avgSpeed = statisticsAdapter.getRouteEntityList().get(position).getName();// TODO
                maxSpeed = statisticsAdapter.getRouteEntityList().get(position).getName();// TODO

                statisticsAdapter.notifyItemChanged(position);

                Intent statDetails = new Intent(StatisticsActivity.this, StatDetails.class);
                statDetails.putExtra("routeID",routeId);
                statDetails.putExtra("startTimeStamp",startTimeStamp);
                statDetails.putExtra("distance",distance);
                statDetails.putExtra("time",time);
                statDetails.putExtra("nameRoute",nameRoute);
                statDetails.putExtra("avgSpeed",avgSpeed);
                statDetails.putExtra("maxSpeed",maxSpeed);

                startActivity(statDetails);
            }
        });

    }

}
