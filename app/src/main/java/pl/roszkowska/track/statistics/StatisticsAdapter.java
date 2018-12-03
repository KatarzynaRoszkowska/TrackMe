package pl.roszkowska.track.statistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.roszkowska.track.R;
import pl.roszkowska.track.database.RouteEntity;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>{

    private Context contex;
    private List<RouteEntity> routeEntityList;
    public OnItemClickListener mListener;
    private RouteEntity routeEntity;

    public StatisticsAdapter(Context contex, List<RouteEntity> routeEntityListList) {
        this.contex = contex;
        this.routeEntityList = routeEntityListList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        mListener = listner;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(contex);
        View view = layoutInflater.inflate(R.layout.statistics_activity, null);
        StatisticsViewHolder holder = new StatisticsViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder statisticsViewHolder, int i) {
        routeEntity = routeEntityList.get(i);

        statisticsViewHolder.distance.setText(String.valueOf(routeEntity.getRouteId()));
        statisticsViewHolder.startTimeStamp.setText("12:34.123 14.12.2018"); //TODO
        statisticsViewHolder.time.setText("02:12.000"); //TODO
    }

    @Override
    public int getItemCount() {
        return routeEntityList.size();
    }

    class StatisticsViewHolder extends RecyclerView.ViewHolder{

        TextView startTimeStamp;
        TextView distance;
        TextView time;

        public StatisticsViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            startTimeStamp = itemView.findViewById(R.id.startTimeStamp);
            distance = itemView.findViewById(R.id.distance);
            time = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }
    public void setRouteEntityList(List<RouteEntity> routeEntityList) {
        this.routeEntityList = routeEntityList;

        /**
         *  refresh RecyclerView
         */

        notifyDataSetChanged();
    }

    /**
     *
     * @return the list od Medicines
     */
    public List<RouteEntity> getRouteEntityList() {
        return routeEntityList;
    }
}
