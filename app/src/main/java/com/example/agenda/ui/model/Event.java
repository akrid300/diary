package com.example.agenda.ui.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.agenda.ui.utils.Utils;

@Entity(tableName = "Event")
public class Event {

    @PrimaryKey
    @ColumnInfo(name = "event_id")
    private Long id;

    @NonNull
    private String title;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "added_date")
    private String addedDate;

    private String description;

    @Embedded(prefix = "location_")
    private Location location;

    public Event() {}

    public Event(String title, String date, String addedDate, String details, Location location) {
        this.title = title;
        this.date = date;
        if (Utils.isStringNullOrEmpty(addedDate))
            this.addedDate = addedDate;
        this.description = details;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
