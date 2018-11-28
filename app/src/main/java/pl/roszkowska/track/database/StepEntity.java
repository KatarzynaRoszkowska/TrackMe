package pl.roszkowska.track.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class StepEntity {
    @PrimaryKey(autoGenerate = true)
    long stepId;
    long routeId;
    double lat;
    double lon;
    long timestamp;
    double distanceBetweenLastStep;
}