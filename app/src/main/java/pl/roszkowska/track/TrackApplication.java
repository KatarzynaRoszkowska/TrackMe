package pl.roszkowska.track;

import android.app.Application;

public class TrackApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TrackModule.setup(this);
    }
}