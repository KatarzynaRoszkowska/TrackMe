package pl.roszkowska.track.ui.marker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import pl.roszkowska.track.marker.MarkerEvent;
import pl.roszkowska.track.module.EventDispatcherModule;

class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private Adapter mAdapter;

    public SwipeToDeleteCallback(Adapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof HeaderViewHolder) return 0;
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int position = viewHolder.getAdapterPosition();
        long id = mAdapter.deleteItem(position);
        EventDispatcherModule
                .getModule()
                .getDispatcher()
                .sendEvent(new MarkerEvent.RemovePoint(id));
    }
}
