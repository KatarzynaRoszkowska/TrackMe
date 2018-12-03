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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.follow.FollowEvent;
import pl.roszkowska.track.follow.FollowState;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.statistics.StatisticsActivity;
import pl.roszkowska.track.ui.MyMapFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mTrackMe;
    private FloatingActionButton mSetMarker;

    private MyMapFragment mMyMapFragment;
    private CompositeDisposable subscribe = new CompositeDisposable();
    private boolean isMapReady;
    private boolean isGraphItemActive;
    private boolean areWeFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTrackMe = findViewById(R.id.trackMe);
        mTrackMe.setOnClickListener(this::setupTrackMe);
        mSetMarker = findViewById(R.id.setMarker);

        mMyMapFragment = (MyMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMyMapFragment.getMapAsync(googleMap -> {
            isMapReady = true;
            subscribeFeatures();
        });
        subscribeFeatures();

//        FloatingActionButton newMarker = findViewById(R.id.setMarker);
//        newMarker.setOnClickListener(view -> TrackModule
//                .getEventDispatcher()
//                .sendEvent(new MarkerEvent.MarkPoint("Test")));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupTrackMe(View v) {
        FollowEvent event;
        if (!areWeFollowing) event = new FollowEvent.StartFollowing();
        else event = new FollowEvent.StopFollowing();
        TrackModule.getEventDispatcher().sendEvent(event);
    }

    private void subscribeFeatures() {
        if (!isMapReady) return;

        subscribe.add(TrackModule
                .followStateStream()
                .subscribe(this::updateUi));
    }

    private void updateUi(FollowState followState) {
        isGraphItemActive = followState.canShowHistogram;
        areWeFollowing = followState.isFollowing;
        if (areWeFollowing) {
            mTrackMe.setImageResource(R.drawable.ic_baseline_stop_24px);
            mSetMarker.show();
        } else {
            mTrackMe.setImageResource(R.drawable.ic_baseline_play_arrow_24px);
            mSetMarker.hide();
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.graphItem).setEnabled(isGraphItemActive);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.graphItem) {
//            subscribe.add(mFollowFeature
//                    .states
//                    .firstOrError()
//                    .subscribe(state -> {
//                        Intent intent = new Intent(this, RealTimeGraph.class);
//                        intent.putExtra("routeId", state.routeId);
//                        this.startActivity(intent);
//                    }));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    private void followDebugCode() {
//        FollowActor followActor = new FollowActor(TrackModule.getModule().getFollowRepository());
//
//        mFollowFeature = new FollowFeature(new FollowState(),
//                eventDispatcher.ofType(FollowEvent.class),
//                followActor,
//                new FollowReducer());
//
//        subscribe.add(mFollowFeature.states.subscribe(state -> {
//            Log.w("RX", state.toString());
//            if (state.steps.isEmpty()) return;
//            FollowState.Step last = state.steps.getLast();
//
//
//            Log.w("STEP", "Last step distance: " + last.distance);
//
//            mMyMapFragment.addNewStep(last.lat, last.lon);
//        }, error -> Log.e("RX", "", error)));
//
//        eventDispatcher.sendEvent(new FollowEvent.StartFollowing());
//
//        subscribe.add(mLocationProvider.locationStream().subscribe(location -> {
//            eventDispatcher.sendEvent(new FollowEvent.NewStep(
//                    location.getLatitude(),
//                    location.getLongitude()));
//        }, error -> Log.e("RX", "", error)));
//    }
//
//    private void markerDebugCode() {
//        MarkerActor markerActor = new MarkerActor(TrackModule.getModule().getMarkerRepository(), mLocationProvider);
//
//        mMarkerFeature = new MarkerFeature(new MarkerState(),
//                eventDispatcher.ofType(MarkerEvent.class),
//                markerActor,
//                new MarkerReducer());
//
//        subscribe.add(mMarkerFeature.states.subscribe(state -> {
//            if (state.mMarkerOptionsList.isEmpty()) return;
//            mMyMapFragment.addMarker(state.mMarkerOptionsList);
//        }, error -> Log.e("RX", "", error)));
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TrackModule.getEventDispatcher().sendEvent(new FollowEvent.StopFollowing());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.dispose();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.statistics) {
            Intent statIntent = new Intent(this, StatisticsActivity.class);
            this.startActivity(statIntent);

        } else if (id == R.id.myMarkers) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}