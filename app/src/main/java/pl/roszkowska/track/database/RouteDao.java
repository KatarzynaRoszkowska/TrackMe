package pl.roszkowska.track.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface RouteDao {
    @Insert
    long createRoute(RouteEntity entity);
}
