package pl.roszkowska.track.ui.marker;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import pl.roszkowska.track.R;

class ItemViewHolder extends ViewHolder<MarkerUIModel> {

    private final TextView nameView;
    private final TextView timeView;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.markerList_listItem_name);
        timeView = itemView.findViewById(R.id.markerList_listItem_time);
    }

    @Override
    void bind(MarkerUIModel model) {
        nameView.setText(model.markerName);
        timeView.setText(model.markerTime);
    }
}
