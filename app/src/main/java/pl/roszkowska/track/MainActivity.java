package pl.roszkowska.track;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.reactivex.disposables.Disposable;
import pl.roszkowska.track.common.EventDispatcher;
import pl.roszkowska.track.common.RxEventDispatcher;
import pl.roszkowska.track.follow.Event;
import pl.roszkowska.track.follow.FollowActor;
import pl.roszkowska.track.follow.FollowFeature;
import pl.roszkowska.track.follow.FollowReducer;
import pl.roszkowska.track.follow.Repository;
import pl.roszkowska.track.follow.State;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private FollowFeature feature;
    private EventDispatcher eventDispatcher;
    private Disposable subscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.setMarker);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //DrawMyRoute drawMyRoute = new DrawMyRoute();
                startActivity(new Intent(getApplicationContext(), DrawMyRoute.class));
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
        FollowActor actor = new FollowActor(new Repository() {
            int id = 0;

            @Override
            public int createNewFollowRoute() {
                return id++;
            }

            @Override
            public void savePosition(int routeId, double lat, double lon) {

            }
        });
        feature = new FollowFeature(new State(),
                eventDispatcher.ofType(Event.class),
                actor,
                new FollowReducer());

        subscribe = feature.states.subscribe(state -> {
            Log.w("RX", state.toString());
        });

        eventDispatcher.sendEvent(new Event.StartFollowing());
        eventDispatcher.sendEvent(new Event.NewStep(0, 100, 100));
        eventDispatcher.sendEvent(new Event.NewStep(0, 102, 102));
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.statistics) {

        } else if (id == R.id.myMarkers) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void xxx() {
        eventDispatcher.sendEvent(new pl.roszkowska.track.marker.Event.MarkPoint());
        eventDispatcher.sendEvent(new Event.StartFollowing());
    }

//    private void onNewModelArrived(ViewModel viewModel) {
    // lambda
//        helloKasia(name -> Log.i("Test", ""));
//    }
//
//    private void helloKasia(Kasia kasia) {
//        kasia.hello("test");
//    }
}
//
//interface Kasia {
//    void hello(String name);
//}