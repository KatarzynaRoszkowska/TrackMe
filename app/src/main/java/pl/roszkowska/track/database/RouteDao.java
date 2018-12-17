package pl.roszkowska.track.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RouteDao {
    @Insert
    long createRoute(RouteEntity entity);

    @Query("SELECT routeId FROM RouteEntity ORDER BY routeId")
    List<Long> getRouteIds();
}
