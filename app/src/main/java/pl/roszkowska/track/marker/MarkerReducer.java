package pl.roszkowska.track.marker;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.marker.MarkerState.MarkerEntity;

public class MarkerReducer implements StateReducer<MarkerEffect, MarkerState> {
    @Override
    public MarkerState reduce(MarkerState state, MarkerEffect effect) {
        MarkerState outState = new MarkerState(state);

        if (effect instanceof MarkerEffect.MarkPoint) {
            MarkerEffect.MarkPoint markerEffect = (MarkerEffect.MarkPoint) effect;
            MarkerEntity entity = new MarkerEntity(
                    markerEffect.id,
                    markerEffect.name,
                    markerEffect.lat,
                    markerEffect.lon);
            outState.mMarkerOptionsList.add(entity);
        } else if (effect instanceof MarkerEffect.RemovePoint) {
            int removeId = ((MarkerEffect.RemovePoint) effect).id;
            for (MarkerEntity entity : outState.mMarkerOptionsList) {
                if (entity.id == removeId) {
                    outState.mMarkerOptionsList.remove(entity);
                    break;
                }
            }
        }
        return outState;
    }
}
