package pl.roszkowska.track.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseProvider {
    private static final String DATABASE_NAME = "movies_db";
    private TrackDatabase mTrackDatabase;

    public DatabaseProvider(Context context) {
        mTrackDatabase = Room.databaseBuilder(context.getApplicationContext(),
                TrackDatabase.class,
                DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public TrackDatabase getDatabase() {
        return mTrackDatabase;
    }
}
