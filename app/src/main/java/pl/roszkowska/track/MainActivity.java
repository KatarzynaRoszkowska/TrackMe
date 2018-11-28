package pl.roszkowska.track;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.common.RxEventDispatcher;
import pl.roszkowska.track.follow.Event;
import pl.roszkowska.track.follow.FollowActor;
import pl.roszkowska.track.follow.FollowFeature;
import pl.roszkowska.track.follow.FollowReducer;
import pl.roszkowska.track.follow.State;
import pl.roszkowska.track.location.GpsLocationProvider;
import pl.roszkowska.track.location.LocationProvider;
import pl.roszkowska.track.marker.MarkerActor;
import pl.roszkowska.track.marker.MarkerFeature;
import pl.roszkowska.track.marker.MarkerReducer;
import pl.roszkowska.track.ui.MyMapFragment;
import pl.roszkowska.track.ui.RealTimeGraph;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MyMapFragment mMyMapFragment;
    private FollowFeature mFollowFeature;
    private MarkerFeature mMarkerFeature;
    private EventDispatcher eventDispatcher;
    private CompositeDisposable subscribe = new CompositeDisposable();
    private LocationProvider mLocationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMyMapFragment = (MyMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        FloatingActionButton newMarker = (FloatingActionButton) findViewById(R.id.setMarker);
        newMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDispatcher.sendEvent(new pl.roszkowska.track.marker.Event.MarkPoint("Test"));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        eventDispatcher = new RxEventDispatcher();
        mLocationProvider = new GpsLocationProvider(this);

        followDebugCode();
        markerDebugCode();
    }


    private void followDebugCode() {
        FollowActor followActor = new FollowActor(TrackModule.getModule().getFollowRepository());

        mFollowFeature = new FollowFeature(new State(),
                eventDispatcher.ofType(Event.class),
                followActor,
                new FollowReducer());

        subscribe.add(mFollowFeature.states.subscribe(state -> {
            Log.w("RX", state.toString());
            if (state.steps.isEmpty()) return;
            State.Step last = state.steps.getLast();


            Log.w("STEP", "Last step distance: " + last.distance);

            mMyMapFragment.addNewStep(last.lat, last.lon);
        }, error -> Log.e("RX", "", error)));

        eventDispatcher.sendEvent(new Event.StartFollowing());

        subscribe.add(mLocationProvider.locationStream().subscribe(location -> {
            eventDispatcher.sendEvent(new Event.NewStep(
                    location.getLatitude(),
                    location.getLongitude()));
        }, error -> Log.e("RX", "", error)));


//        subscribe.add(mMarkerFeature.states.subscribe(state -> {
//            if (state.mMarkerOptionsList.isEmpty()) return;
//            mMyMapFragment.addMarker(state.mMarkerOptionsList);
//        }));
    }

    private void markerDebugCode() {
        MarkerActor markerActor = new MarkerActor(TrackModule.getModule().getMarkerRepository(), mLocationProvider);

        mMarkerFeature = new MarkerFeature(new pl.roszkowska.track.marker.State(),
                eventDispatcher.ofType(pl.roszkowska.track.marker.Event.class),
                markerActor,
                new MarkerReducer());

        subscribe.add(mMarkerFeature.states.subscribe(state -> {
            if (state.mMarkerOptionsList.isEmpty()) return;
            mMyMapFragment.addMarker(state.mMarkerOptionsList);
        }, error -> Log.e("RX", "", error)));


     /*  MarkerActor markerActor = new MarkerActor(new pl.roszkowska.track.marker.Repository() {
             int id = 0;

            @Override
            public int savePoint(String name, double lat, double lon) {
                return id++;
            }
        }, mLocationProvider);

        mMarkerFeature = new MarkerFeature(new pl.roszkowska.track.marker.State(),
                eventDispatcher.ofType(pl.roszkowska.track.marker.Event.class),
                markerActor,
                new MarkerReducer());

        eventDispatcher.sendEvent(new pl.roszkowska.track.marker.Event.MarkPoint("My new point"));

        subscribe.add(Observable.just("My 2 Marker")
                .delay(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    eventDispatcher.sendEvent(new pl.roszkowska.track.marker.Event.MarkPoint(s));
                }));

        subscribe.add(Observable.just("My 3 Marker")
                .delay(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    eventDispatcher.sendEvent(new pl.roszkowska.track.marker.Event.MarkPoint(s));
                }));
                */

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationProvider.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.dispose();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.graphItem) {
            Intent intent1 = new Intent(this, RealTimeGraph.class);
            this.startActivity(intent1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.statistics) {

        } else if (id == R.id.myMarkers) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}