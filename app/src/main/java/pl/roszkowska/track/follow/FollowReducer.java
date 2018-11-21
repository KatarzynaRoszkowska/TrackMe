package pl.roszkowska.track.follow;

import com.google.android.gms.maps.model.LatLng;

import pl.roszkowska.track.common.StateReducer;

public class FollowReducer implements StateReducer<Effect, State> {

    @Override
    public State reduce(State state, Effect effect) {
        State outState = new State(state);
        if (effect instanceof Effect.StartedFollowing) {
            outState.routeId = ((Effect.StartedFollowing) effect).id;
            outState.isFollowing = true;
        } else if (effect instanceof Effect.StoppedFollowing) {
            outState.isFollowing = false;
        } else if (effect instanceof Effect.NewStep) {
            if (outState.isFollowing) {
                Effect.NewStep stepEvent = (Effect.NewStep) effect;
                outState.steps.add(new LatLng(stepEvent.lat, stepEvent.lon));
            }
        }
        return outState;
    }
}
