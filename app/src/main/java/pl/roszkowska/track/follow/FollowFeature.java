package pl.roszkowska.track.follow;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;
import pl.roszkowska.track.common.Feature;
import pl.roszkowska.track.common.StateReducer;

public class FollowFeature extends Feature<FollowState, FollowEvent, FollowEffect> {

    public FollowFeature(FollowState initialState,
                         Observable<FollowEvent> events,
                         Actor<FollowEvent, FollowState, FollowEffect> actor,
                         StateReducer<FollowEffect, FollowState> reducer) {
        super(initialState, events, actor, reducer);
    }
}
