package pl.roszkowska.track.marker;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.database.MarkerEntity;

public class MarkerReducer implements StateReducer<Effect, State> {
    @Override
    public State reduce(State state, Effect effect) {
        State outState = new State(state);

        if (effect instanceof Effect.MarkPoint) {
            Effect.MarkPoint markPointEffect = (Effect.MarkPoint) effect;
            MarkerEntity entity = new MarkerEntity();
            //TODO tutaj trzeba cos takiego:
            // entity.lat = markPointEffect.lat;
            // entity.lon = markPointEffect.lon;
            // entity.name = markPointEffect.name;
            outState.mMarkerOptionsList.add(entity);
        } else if (effect instanceof Effect.RemovePoint) {
            int removeId = ((Effect.RemovePoint) effect).id;
            for (MarkerEntity entity : outState.mMarkerOptionsList) {
                if (entity.id == removeId) { //TODO tutaj tez cos słabo
                    outState.mMarkerOptionsList.remove(entity);
                    break;
                }
            }
        }
        return outState;
    }
}
