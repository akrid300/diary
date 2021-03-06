package com.example.agenda.ui.activity;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.agenda.R;
import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.EventDAO;
import com.example.agenda.ui.data.EventService;
import com.example.agenda.ui.data.LocationDAO;
import com.example.agenda.ui.data.LocationService;
import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.model.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) return;


        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Get the list of events and their location to be displayed on the map
        DatabaseInstance databaseInstance = DatabaseInstance.getInstance(getApplicationContext());

        EventDAO eventDAO = databaseInstance.eventDAO();
        EventService eventService = new EventService(eventDAO);
        List<Event> events = eventService.getEvents();
        List<Location> locations = new ArrayList<>();

        for (Event event : events) {
            if (event.getLocation() != null)
                locations.add(event.getLocation());
        }


        for(Location location : locations){
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title(location.getName()));
            builder.include(position);
        }


        if (locations.size() == 0) {
            LatLng oldTown = new LatLng(44.4330, 26.1024);
            LatLng romania = new LatLng(45.9432, 24.9668);
            builder.include(oldTown);
            builder.include(romania);
        }

        // User the bounds to move the camera in a way which includes all the markers added on the map
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
        //googleMap.animateCamera(cu);

    }


}
