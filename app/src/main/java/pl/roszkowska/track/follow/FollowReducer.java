package pl.roszkowska.track.follow;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.follow.State.Step;

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
                outState.steps.add(new Step(stepEvent.lat,
                        stepEvent.lon,
                        stepEvent.timestamp,
                        stepEvent.distance)
                );
            }
        }
        return outState;
    }
}
