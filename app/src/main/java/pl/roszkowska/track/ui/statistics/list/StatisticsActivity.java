package pl.roszkowska.track.ui.statistics.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ViewSwitcher;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.statistics.list.StatisticListEvent;
import pl.roszkowska.track.statistics.list.StatisticListState;

public class StatisticsActivity extends AppCompatActivity {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_LIST = 1;

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private StatisticsAdapter mAdapter;
    private ViewSwitcher mViewSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_recycler_view);

        mAdapter = new StatisticsAdapter();

        mViewSwitcher = findViewById(R.id.viewSwitcherListOfRoutes);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewListOfRoutes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mDisposable.add(TrackModule
                .statisticListStateStream()
                .distinctUntilChanged()
                .subscribe(this::bind)
        );
    }

    private void bind(StatisticListState state) {
        if (state.isLoading) {
            mViewSwitcher.setDisplayedChild(VIEW_LOADING);
        } else {
            mViewSwitcher.setDisplayedChild(VIEW_LIST);
            mAdapter.setModels(state.itemList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackModule.getEventDispatcher().sendEvent(new StatisticListEvent.Load());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}
