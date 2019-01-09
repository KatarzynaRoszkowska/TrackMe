package pl.roszkowska.track.ui.marker;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

class HeaderViewHolder extends ViewHolder<MarkerUIHeader> {
    private final TextView headerView;

    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        headerView = (TextView) itemView;
    }

    @Override
    void bind(MarkerUIHeader model) {
        headerView.setText(model.headerText);
    }
}
