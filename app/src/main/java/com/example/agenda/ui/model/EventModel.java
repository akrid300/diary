package com.example.agenda.ui.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity(tableName = "Event")
public class EventModel {

    @PrimaryKey
    @ColumnInfo(name = "event_id")
    private Long id;

    @NonNull
    private String title;

    private String date;
    @ColumnInfo(name = "added_date")
    private String addedDate;
    private String type;
    private String description;

    @Embedded private LocationModel location;

    public EventModel() {}

    public EventModel(String title, String date, String addedDate, String type, String details, LocationModel location) {
        this.title = title;
        this.date = date;
        this.addedDate = addedDate;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }
}
