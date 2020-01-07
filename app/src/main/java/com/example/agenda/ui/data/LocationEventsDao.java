package com.example.agenda.ui.data;


import androidx.room.Dao;
import androidx.room.Query;

import com.example.agenda.ui.model.LocationAllEvents;

@Dao
public interface LocationEventsDao {

    @Query("SELECT * FROM Location WHERE location_id = :locationId")
    LocationAllEvents loadLocationAllEvents(long locationId);
}