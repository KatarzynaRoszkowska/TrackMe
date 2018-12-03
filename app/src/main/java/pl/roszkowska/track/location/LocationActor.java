package pl.roszkowska.track.location;

import io.reactivex.Observable;
import pl.roszkowska.track.common.Actor;

public class LocationActor implements Actor<LocationEvent, LocationState, LocationEffect> {

    private final GpsLocationStream mLocationStream;

    public LocationActor(GpsLocationStream locationStream) {
        mLocationStream = locationStream;
    }

    @Override
    public Observable<LocationEffect> act(LocationState state, LocationEvent event) {
        if (event instanceof LocationEvent.Start) {
            return Observable.merge(
                    Observable.just(new LocationEffect.Start()),
                    mLocationStream
                            .locationStream()
                            .map(LocationEffect.AddStep::new)
                            .doOnSubscribe(disposable -> mLocationStream.start())
            ).cast(LocationEffect.class);
        } else if (event instanceof LocationEvent.Stop) {
            return Observable
                    .just(new LocationEffect.Stop())
                    .doOnSubscribe(disposable -> mLocationStream.stop())
                    .cast(LocationEffect.class);
        }
        throw new IllegalStateException();
    }
}

