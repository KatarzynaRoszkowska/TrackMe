package pl.roszkowska.track.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(
        entities = {
                RouteEntity.class,
                StepEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class TrackDatabase extends RoomDatabase {
    public abstract StepDao daoStep();

    public abstract RouteDao daoRoute();
}
