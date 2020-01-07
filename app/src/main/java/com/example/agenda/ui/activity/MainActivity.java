package com.example.agenda.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.agenda.R;
import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.LocationDAO;
import com.example.agenda.ui.data.LocationService;
import com.example.agenda.ui.data.UserPrefs;
import com.example.agenda.ui.model.HolidayModel;
import com.example.agenda.ui.model.Location;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //Get the Shared Preferences..
        UserPrefs.setUpUserPrefs(getSharedPreferences(getPackageName() + ".default", Activity.MODE_PRIVATE));

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_holidays, R.id.navigation_holidays)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        setLocations();

    }

    private void setLocations() {


        ArrayList<Location> locations = new ArrayList<>();

        try {
            InputStream is = getApplicationContext().getAssets().open("locations.json");
            Gson gson = new GsonBuilder().create();

            JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            reader.beginArray();
            while (reader.hasNext()) {
                Location location = gson.fromJson(reader, Location.class);
                if (location != null) {
                    locations.add(location);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //LocationService
        DatabaseInstance databaseInstance = DatabaseInstance.getInstance(getApplicationContext());
        LocationDAO locationDAO = databaseInstance.locationDAO();
        LocationService locationService = new LocationService(locationDAO);

        for(Location item : locations){
            locationService.addLocation(item);
        }

        //locationService.updateLocation("Park",4L);
        //locationService.deleteLocationById(3L);
    }

}
