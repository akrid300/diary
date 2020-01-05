package com.example.agenda.ui.data;

import android.database.Cursor;

import com.example.agenda.ui.model.LocationModel;

import java.util.List;

public class LocationService implements LocationDAO {

    private final LocationDAO tableInstance; //add constructor and it automatically builds the one below

    public LocationService(LocationDAO tableInstance) {
        this.tableInstance = tableInstance;
    }

    @Override
    public Long addLocation(LocationModel item) {
        return tableInstance.addLocation(item);
    }

    @Override
    public List<LocationModel> getLocations() {
        return tableInstance.getLocations();
    }

    @Override
    public LocationModel getLocationById(Long id) {
        return tableInstance.getLocationById(id);
    }

    @Override
    public LocationModel getLocationByName(String name) {
        return tableInstance.getLocationByName(name);
    }

    @Override
    public void deleteLocation(LocationModel item) {
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