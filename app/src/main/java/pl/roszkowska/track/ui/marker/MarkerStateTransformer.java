package pl.roszkowska.track.ui.marker;

import android.content.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.roszkowska.track.marker.MarkerState;

class MarkerStateTransformer {
    private final DateFormat mDateFormat;
    private final DateFormat mTimeFormat;

    MarkerStateTransformer(Context context) {
        mDateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());
        mTimeFormat = android.text.format.DateFormat.getTimeFormat(context.getApplicationContext());
    }

    List<BaseMarkerUIModel> transform(List<MarkerState.MarkerItem> markerItems) {
        List<BaseMarkerUIModel> listOut = new ArrayList<>();

        Calendar lastHeaderDate = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        lastHeaderDate.clear();
        currentDate.clear();
        int headerId = -1;
        for (int i = 0; i < markerItems.size(); i++) {
            MarkerState.MarkerItem item = markerItems.get(i);
            currentDate.setTimeInMillis(item.timestamp);
            if (i == 0 || lastHeaderDate.get(Calendar.DAY_OF_YEAR) != currentDate.get(Calendar.DAY_OF_YEAR)) {
                lastHeaderDate.setTimeInMillis(item.timestamp);
                listOut.add(new MarkerUIHeader(headerId, mDateFormat.format(item.timestamp)));
                headerId--;
            }
            listOut.add(new MarkerUIModel(item.id, item.name, mTimeFormat.format(item.timestamp)));
        }
        return listOut;
    }
}
