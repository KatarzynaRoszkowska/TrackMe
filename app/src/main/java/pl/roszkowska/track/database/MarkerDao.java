package pl.roszkowska.track.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface MarkerDao {
    @Insert
    long savePoint(MarkerEntity markerEntity);
}
