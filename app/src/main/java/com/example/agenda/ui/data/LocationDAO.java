package com.example.agenda.ui.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.agenda.ui.model.Location;

import java.util.List;

@Dao
public interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addLocation(Location item);

    @Query("SELECT * FROM Location")
    List<Location> getLocations();

    @Query("SELECT * FROM Location WHERE location_id=:id")
    Location getLocationById(Long id);

    @Query("SELECT * FROM Location WHERE name=:name limit 1")
    Location getLocationByName(String name);

    @Delete
    void deleteLocation(Location item);

    @Query("DELETE FROM Location WHERE location_id=:id")
    void deleteLocationById(Long id);

    @Query("UPDATE Location SET name=:name WHERE location_id=:id")
    void updateLocation(String name, Long id);
}
