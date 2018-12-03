package pl.roszkowska.track.follow;

import pl.roszkowska.track.common.StateReducer;
import pl.roszkowska.track.follow.FollowState.Step;

public class FollowReducer implements StateReducer<FollowEffect, FollowState> {

    @Override
    public FollowState reduce(FollowState state, FollowEffect effect) {
        FollowState outState = new FollowState(state);
        if (effect instanceof FollowEffect.StartedFollowing) {
            outState.routeId = ((FollowEffect.StartedFollowing) effect).id;
            outState.isFollowing = true;
        } else if (effect instanceof FollowEffect.StoppedFollowing) {
            outState.isFollowing = false;
        } else if (effect instanceof FollowEffect.NewStep) {
            if (outState.isFollowing) {
                FollowEffect.NewStep stepEvent = (FollowEffect.NewStep) effect;
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
