package pl.roszkowska.track.ui.marker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import pl.roszkowska.track.R;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_MODEL = 1;
    private List<BaseMarkerUIModel> mModelList;
    private int mLastRemovedItemPosition;
    private BaseMarkerUIModel mLastRemovedItem;

    public Adapter() {
        setHasStableIds(true);
    }

    public void setModelList(List<BaseMarkerUIModel> modelList) {
        mModelList = modelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(
                        inflater.inflate(
                                R.layout.markerlist_listitemheader,
                                viewGroup,
                                false)
                );
            case VIEW_TYPE_MODEL:
                return new ItemViewHolder(
                        inflater.inflate(
                                R.layout.markerlist_listitem,
                                viewGroup,
                                false)
                );
        }
        throw new IllegalStateException("Unknown view type: " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //noinspection unchecked
        viewHolder.bind(mModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return mModelList.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        BaseMarkerUIModel model = mModelList.get(position);
        if (model instanceof MarkerUIHeader) return VIEW_TYPE_HEADER;
        else if (model instanceof MarkerUIModel) return VIEW_TYPE_MODEL;
        throw new IllegalStateException("Unknown item type for position: " + position);
    }

    public long deleteItem(int position) {
        mLastRemovedItemPosition = position;
        mLastRemovedItem = mModelList.remove(position);
        notifyItemRemoved(position);
        return mLastRemovedItem.id;
    }
}
