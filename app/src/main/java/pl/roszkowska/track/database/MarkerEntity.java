package pl.roszkowska.track.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MarkerEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    double lat;
    double lon;
    long timestamp;

}
