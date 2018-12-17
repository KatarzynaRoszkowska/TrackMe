package pl.roszkowska.track.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StepDao {
    @Insert
    long insertStep(StepEntity stepEntity);

    @Query("SELECT * FROM StepEntity WHERE routeId = :routeId ORDER BY stepId ASC LIMIT 1")
    StepEntity getFirstStep(long routeId);

    @Query("SELECT * FROM StepEntity WHERE routeId = :routeId ORDER BY stepId DESC LIMIT 1")
    StepEntity getLastStep(long routeId);

    @Query("SELECT * FROM StepEntity WHERE routeId = :routeId ORDER BY stepId")
    List<StepEntity> getRouteSteps(long routeId);
}
