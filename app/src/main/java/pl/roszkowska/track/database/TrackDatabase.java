package pl.roszkowska.track.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(
        entities = {
                RouteEntity.class,
                StepEntity.class,
                MarkerEntity.class
        },
        version = 3,
        exportSchema = false
)
public abstract class TrackDatabase extends RoomDatabase {
    public abstract StepDao daoStep();

    public abstract RouteDao daoRoute();

    public abstract MarkerDao doaMarker();
}
