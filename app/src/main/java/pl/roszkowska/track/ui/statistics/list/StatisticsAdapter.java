package pl.roszkowska.track.ui.statistics.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import pl.roszkowska.track.R;
import pl.roszkowska.track.statistics.list.StatisticListState;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static pl.roszkowska.track.ui.statistics.StatDetailsActivity.IntentCreator.createIntent;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {

    private List<StatisticListState.Item> mModels;

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.statistics_list_item, viewGroup, false);
        return new StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder statisticsViewHolder, int i) {
        statisticsViewHolder.bind(mModels.get(i));
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    static class StatisticsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        StatisticListState.Item lastModel;

        final DateFormat dateFormat;
        final DateFormat timeFormat;
        TextView startTimeStamp;
        TextView distance;
        TextView time;

        public StatisticsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            dateFormat = android.text.format.DateFormat.getLongDateFormat(itemView.getContext());
            timeFormat = android.text.format.DateFormat.getTimeFormat(itemView.getContext());

            startTimeStamp = itemView.findViewById(R.id.startTimeStamp);
            distance = itemView.findViewById(R.id.distance);
            time = itemView.findViewById(R.id.time);
        }

        public void bind(StatisticListState.Item model) {
            lastModel = model;
            distance.setText(String.valueOf(model.routeId));
            startTimeStamp.setText(String.format(
                    "%s, %s",
                    dateFormat.format(model.timestamp),
                    timeFormat.format(model.timestamp)
            ));
            time.setText(formatDuration(model.routeDuration));
        }

        private String formatDuration(long millis) {
            return String.format(Locale.getDefault(),
                    "%02d:%02d:%02d",
                    MILLISECONDS.toHours(millis),
                    MILLISECONDS.toMinutes(millis) - HOURS.toMinutes(MILLISECONDS.toHours(millis)),
                    MILLISECONDS.toSeconds(millis) - MINUTES.toSeconds(MILLISECONDS.toMinutes(millis)));
        }

        @Override
        public void onClick(View v) {
            v.getContext().startActivity(createIntent(v.getContext(), lastModel.routeId));
        }
    }

    void setModels(List<StatisticListState.Item> models) {
        mModels = models;

        // refresh RecyclerView
        notifyDataSetChanged();
    }
}
