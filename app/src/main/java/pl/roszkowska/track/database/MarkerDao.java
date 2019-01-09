package pl.roszkowska.track.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MarkerDao {
    @Insert
    long savePoint(MarkerEntity markerEntity);

    @Query("DELETE FROM markerentity WHERE id = :id")
    void removePoint(long id);

    @Query("SELECT * FROM MarkerEntity ORDER BY timestamp ASC")
    List<MarkerEntity> getMarkers();
}
