package pl.roszkowska.track;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.follow.FollowEvent;
import pl.roszkowska.track.follow.FollowState;
import pl.roszkowska.track.marker.MarkerEvent;
import pl.roszkowska.track.marker.MarkerState;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.ui.MyMapFragment;
import pl.roszkowska.track.ui.RealTimeGraph;
import pl.roszkowska.track.ui.statistics.list.StatisticsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mTrackMe;
    private FloatingActionButton mSetMarker;

    private MyMapFragment mMyMapFragment;
    private CompositeDisposable subscribe = new CompositeDisposable();
    private boolean isMapReady;
    private boolean isGraphItemActive;
    private boolean areWeFollowing;
    private long lastRouteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTrackMe = findViewById(R.id.trackMe);
        mTrackMe.setOnClickListener(this::onTrackMeClicked);
        mSetMarker = findViewById(R.id.setMarker);
        mSetMarker.setOnClickListener(this::onSetMarkerClicked);

        mMyMapFragment = (MyMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMyMapFragment.getMapAsync(googleMap -> {
            isMapReady = true;
            subscribeFeatures();
        });
        subscribeFeatures();

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


        //startActivity(new Intent(this, LearningRx.class));

//        subscribe.add(
//                TrackModule
//                        .histogramStateStream()
//                        .subscribe(statisticsState -> {
//                            Log.i("TAGTAG", statisticsState.toString());
//                        })
//        );
    }

    private void onTrackMeClicked(View v) {
        FollowEvent event;
        if (!areWeFollowing) event = new FollowEvent.StartFollowing();
        else event = new FollowEvent.StopFollowing();
        TrackModule.getEventDispatcher().sendEvent(event);
    }

    private void onSetMarkerClicked(View v) {
        new AlertDialog.Builder(this)
                .setView(R.layout.dialog_marker_detials)
                .setTitle("Podaj nazwe lokalizacji")
                .setPositiveButton("OK", (dialog, which) -> {
                    EditText textView = ((AlertDialog) dialog).findViewById(R.id.dialogMarkerDetails_markerName);
                    TrackModule
                            .getEventDispatcher()
                            .sendEvent(new MarkerEvent.MarkPoint(textView.getText().toString()));
                })
                .show();
    }

    private void subscribeFeatures() {
        if (!isMapReady) return;

        subscribe.add(TrackModule
                .followStateStream()
                .subscribe(this::updateFollowUi, this::onError));
        subscribe.add(TrackModule
                .markerStateStream()
                .subscribe(this::updateMarkerUi, this::onError));

        TrackModule.getEventDispatcher().sendEvent(new MarkerEvent.LoadMarkers());
    }

    private void onError(Throwable throwable) {
        Log.e("ERR", "", throwable);
    }

    private void updateFollowUi(FollowState followState) {
        isGraphItemActive = followState.canShowHistogram;
        areWeFollowing = followState.isFollowing;
        lastRouteId = followState.routeId;
        if (areWeFollowing) {
            mTrackMe.setImageResource(R.drawable.ic_baseline_stop_24px);
            mSetMarker.show();
        } else {
            mTrackMe.setImageResource(R.drawable.ic_baseline_play_arrow_24px);
            mSetMarker.hide();
        }
        mMyMapFragment.setSteps(MyMapFragment.convertFromFollowStateStep(followState.steps));
        invalidateOptionsMenu();
    }

    private void updateMarkerUi(MarkerState markerState) {
        mMyMapFragment.setMarkers(markerState.mMarkerOptionsList);
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
            Intent intent = new Intent(this, RealTimeGraph.class);
            intent.putExtra(RealTimeGraph.ROUTE_ID, lastRouteId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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