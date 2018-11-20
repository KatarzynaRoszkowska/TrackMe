package pl.roszkowska.track.follow;

import com.google.android.gms.maps.model.LatLng;

import pl.roszkowska.track.common.StateReducer;

public class FollowReducer implements StateReducer<Effect, State> {

    @Override
    public State reduce(State state, Effect event) {
        State outState = new State(state);
        if (event instanceof Effect.StartedFollowing) {
            outState.routeId = ((Effect.StartedFollowing) event).id;
            outState.isFollowing = true;
        } else if (event instanceof Effect.StoppedFollowing) {
            outState.isFollowing = false;
        } else if (event instanceof Effect.NewStep) {
            if (state.isFollowing) {
                Effect.NewStep stepEvent = (Effect.NewStep) event;
                outState.steps.add(new LatLng(stepEvent.lat, stepEvent.lon));
            }
        }
        return outState;
    }
}
