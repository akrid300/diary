package com.example.agenda.ui.data;

import com.example.agenda.ui.model.Event;

import java.util.List;

public class EventService implements EventDAO {

    private final EventDAO tableInstance; //add constructor and it automatically builds the one below

    public EventService(EventDAO tableInstance) {
        this.tableInstance = tableInstance;
    }

    @Override
    public Long addEvent(Event item) {
        return tableInstance.addEvent(item);
    }

    @Override
    public List<Event> getEvents() {
        return tableInstance.getEvents();
    }

    @Override
    public Event getEventById(Long id) {
        return tableInstance.getEventById(id);
    }

    @Override
    public Long getLatestEventById() {
        return tableInstance.getLatestEventById();
    }

    @Override
    public void deleteEvent(Event item) {
        tableInstance.deleteEvent(item);
    }

    @Override
    public void deleteEventById(Long id) {
        tableInstance.deleteEventById(id);
    }

    @Override
    public void updateEvent(String title, String date, String description, Long id) {
        tableInstance.updateEvent(title, date, description, id);
    }
}