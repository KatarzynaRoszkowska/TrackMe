package pl.roszkowska.track.ui.statistics.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.reactivex.disposables.CompositeDisposable;
import pl.roszkowska.track.R;
import pl.roszkowska.track.module.TrackModule;
import pl.roszkowska.track.statistics.list.StatisticListEvent;

public class StatisticsActivity extends AppCompatActivity {

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewListOfRoutes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StatisticsAdapter adapter = new StatisticsAdapter();
        recyclerView.setAdapter(adapter);

        mDisposable.add(TrackModule
                .statisticListStateStream()
                .subscribe(state -> adapter.setModels(state.itemList))
        );
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
