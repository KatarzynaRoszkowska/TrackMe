package pl.roszkowska.track.ui.marker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.marker.MarkerState;
import pl.roszkowska.track.module.TrackModule;

public class MarkerListActivity extends AppCompatActivity {

    private Adapter mAdapter;
    private MarkerStateTransformer mTransformer;
    private CompositeDisposable subscribe = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_list);

        mTransformer = new MarkerStateTransformer(this);
        mAdapter = new Adapter();

        RecyclerView recyclerView = findViewById(R.id.markerList_recyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        subscribe.add(TrackModule
                .markerStateStream()
                .subscribe(this::updateMarkerUi, this::onError));
    }

    private void onError(Throwable throwable) {
        Log.e("ERR", "", throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.clear();
    }

    private void updateMarkerUi(MarkerState markerState) {
        mAdapter.setModelList(
                mTransformer.transform(markerState.mMarkerOptionsList)
        );
    }

    public static class IntentCreator {

        public static Intent createIntent(Context context) {
            return new Intent(context, MarkerListActivity.class);
        }
    }
}