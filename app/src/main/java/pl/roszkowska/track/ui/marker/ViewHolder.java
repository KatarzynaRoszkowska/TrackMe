package pl.roszkowska.track.ui.marker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ViewHolder<T extends BaseMarkerUIModel> extends RecyclerView.ViewHolder {
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract void bind(T model);
}