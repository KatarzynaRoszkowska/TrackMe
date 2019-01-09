package pl.roszkowska.track.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RouteEntity {
    @PrimaryKey(autoGenerate = true)
    long routeId;
    String name;

    public long getRouteId() {
        return routeId;
    }

    public String getName() {
        return name;
    }
}