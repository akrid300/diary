package com.example.agenda.ui.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.agenda.ui.model.Event;

import java.util.List;

@Dao
public interface EventDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addEvent(Event item);

    @Query("SELECT * FROM Event order by event_id desc")
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
}