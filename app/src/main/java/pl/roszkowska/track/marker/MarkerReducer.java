package pl.roszkowska.track.marker;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.marker.State.MarkerEntity;

public class MarkerReducer implements StateReducer<Effect, State> {
    @Override
    public State reduce(State state, Effect effect) {
        State outState = new State(state);
        if (effect instanceof Effect.MarkPoint) {
            Effect.MarkPoint markPointEffect = (Effect.MarkPoint) effect;
            MarkerEntity marker = new MarkerEntity(
                    markPointEffect.id,
                    markPointEffect.name,
                    markPointEffect.lat,
                    markPointEffect.lon
            );
            outState.mMarkerOptionsList.add(marker);
        } else if (effect instanceof Effect.RemovePoint) {
            int removeId = ((Effect.RemovePoint) effect).id;
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
