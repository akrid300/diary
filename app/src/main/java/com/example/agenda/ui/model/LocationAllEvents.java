package com.example.agenda.ui.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class LocationAllEvents {

    @Embedded
    public Location location;

    @Relation(parentColumn = "id", entityColumn = "location_iid", entity = Event.class)
    public List<Event> events;
}
