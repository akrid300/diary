package com.example.agenda.ui.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.model.Location;

import java.util.List;

@Dao
public interface EventDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addEvent(Event item);

    @Query("SELECT * FROM Event order by date desc")
    List<Event> getEvents();

    @Query("SELECT event_id FROM Event order by event_id desc")
    Long getLatestEventById();

    @Query("SELECT * FROM Event WHERE event_id=:id")
    Event getEventById(Long id);

    @Delete
    void deleteEvent(Event item);

    @Query("DELETE FROM Event WHERE event_id=:id")
    void deleteEventById(Long id);

    @Query("UPDATE Event SET title=:title, date=:date, description=:description WHERE event_id=:id")
    void updateEvent(String title, String date, String description, Long id);

    @Query("UPDATE Event SET title=:title, date=:date, description=:description, " +
            "location_location_id=:loc_id, location_name=:loc_name, location_location_type=:lov_type, location_latitude=:loc_lat, location_longitude=:loc_long WHERE event_id=:id")
    void updateEventWithLocation(String title, String date, String description, Long loc_id, String loc_name, String lov_type, Double loc_lat, Double loc_long, Long id);
}