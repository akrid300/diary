package com.example.agenda.ui.data;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.agenda.ui.model.LocationModel;

import java.util.List;

@Dao
public interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addLocation(LocationModel item);

    @Query("SELECT * FROM Location")
    List<LocationModel> getLocations();

    @Query("SELECT * FROM Location WHERE location_id=:id")
    LocationModel getLocationById(Long id);

    @Query("SELECT * FROM Location WHERE name=:name limit 1")
    LocationModel getLocationByName(String name);

    @Delete
    void deleteLocation(LocationModel item);

    @Query("DELETE FROM Location WHERE location_id=:id")
    void deleteLocationById(Long id);

    @Query("UPDATE Event SET name=:name WHERE location_id=:id")
    void updateLocation(String name, Long id);
}
