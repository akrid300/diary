package com.example.agenda.ui.data;

import com.example.agenda.ui.model.Location;

import java.util.List;

public class LocationService implements LocationDAO {

    private final LocationDAO tableInstance; //add constructor and it automatically builds the one below

    public LocationService(LocationDAO tableInstance) {
        this.tableInstance = tableInstance;
    }

    @Override
    public Long addLocation(Location item) {
        return tableInstance.addLocation(item);
    }

    @Override
    public List<Location> getLocations() {
        return tableInstance.getLocations();
    }

    @Override
    public Location getLocationById(Long id) {
        return tableInstance.getLocationById(id);
    }

    @Override
    public Location getLocationByName(String name) {
        return tableInstance.getLocationByName(name);
    }

    @Override
    public void deleteLocation(Location item) {
        tableInstance.deleteLocation(item);
    }

    @Override
    public void deleteLocationById(Long id) {
        tableInstance.deleteLocationById(id);
    }

    @Override
    public void updateLocation(String name, Long id) {
        tableInstance.updateLocation(name, id);
    }
}