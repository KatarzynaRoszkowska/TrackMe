package pl.roszkowska.track;

import android.app.Application;

import pl.roszkowska.track.module.TrackModule;

public class TrackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TrackModule.setup(this);
    }
}