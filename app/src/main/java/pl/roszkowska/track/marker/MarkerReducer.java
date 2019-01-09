package pl.roszkowska.track.marker;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.marker.MarkerState.MarkerItem;

public class MarkerReducer implements StateReducer<MarkerEffect, MarkerState> {
    @Override
    public MarkerState reduce(MarkerState state, MarkerEffect effect) {
        MarkerState outState = new MarkerState(state);

        if (effect instanceof MarkerEffect.MarkersLoaded) {
            outState.mMarkerOptionsList.clear();
            outState.mMarkerOptionsList.addAll(((MarkerEffect.MarkersLoaded) effect).markPoints);
        } else if (effect instanceof MarkerEffect.MarkPoint) {
            MarkerEffect.MarkPoint markerEffect = (MarkerEffect.MarkPoint) effect;
            MarkerState.MarkerItem item = new MarkerState.MarkerItem(
                    markerEffect.id,
                    markerEffect.name,
                    markerEffect.lat,
                    markerEffect.lon,
                    markerEffect.timestamp);
            outState.mMarkerOptionsList.add(item);
        } else if (effect instanceof MarkerEffect.RemovePoint) {
            long removeId = ((MarkerEffect.RemovePoint) effect).id;
            for (MarkerItem entity : outState.mMarkerOptionsList) {
                if (entity.id == removeId) {
                    outState.mMarkerOptionsList.remove(entity);
                    break;
                }
            }
        }
        return outState;
    }
}
