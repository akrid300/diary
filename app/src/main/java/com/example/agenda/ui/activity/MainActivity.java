package com.example.agenda.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.agenda.R;
import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.LocationDAO;
import com.example.agenda.ui.data.LocationService;
import com.example.agenda.ui.data.UserPrefs;
import com.example.agenda.ui.model.LocationModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

        LocationModel[] values = new LocationModel[]{
                new LocationModel(1L, "Home"),
                new LocationModel(2L, "Office"),
                new LocationModel(3L, "Bar"),
                new LocationModel(4L, "Restaurant"),
                new LocationModel(5L, "Hospital"),
                new LocationModel(6L, "Club"),
                new LocationModel(7L, "Gym"),
                new LocationModel(8L, "School"),
                new LocationModel(9L, "Mall"),
                new LocationModel(10L, "Supermarket")};


        //LocationService
        DatabaseInstance databaseInstance = DatabaseInstance.getInstance(getApplicationContext());
        LocationDAO locationDAO = databaseInstance.locationDAO();
        LocationService locationService = new LocationService(locationDAO);

        for(LocationModel item : values){
            locationService.addLocation(item);
        }

        locationService.updateLocation("Park",4L);
        locationService.deleteLocationById(3L);
    }

}
