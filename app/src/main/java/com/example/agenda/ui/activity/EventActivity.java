package com.example.agenda.ui.activity;

import android.os.Bundle;

import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.EventDAO;
import com.example.agenda.ui.data.EventService;
import com.example.agenda.ui.data.LocationDAO;
import com.example.agenda.ui.data.LocationService;
import com.example.agenda.ui.fragment.DatePickerDialogFragment;
import com.example.agenda.ui.listener.DateListener;
import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.model.Location;
import com.example.agenda.ui.utils.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.agenda.ui.utils.Utils.isStringNullOrEmpty;

public class EventActivity extends AppCompatActivity implements View.OnClickListener, DateListener {

    private EditText titleEditText;
    private EditText dateEditText;
    private Spinner locationSpinner;
    private EditText descriptionEditText;
    private Button doneButton;

    ArrayAdapter<String> spinnerAdapter;

    private DatabaseInstance databaseInstance;
    private LocationDAO locationDAO;
    private LocationService locationService;

    private EventDAO eventDAO;
    private EventService eventService;

    private Event event;

    DatePickerDialogFragment mDatePickerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        locationSpinner = findViewById(R.id.locationSpinner);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        doneButton = findViewById(R.id.doneButton);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseInstance = DatabaseInstance.getInstance(this);

        //LocationService
        locationDAO = databaseInstance.locationDAO();
        locationService = new LocationService(locationDAO);

        //EventService
        eventDAO = databaseInstance.eventDAO();
        eventService = new EventService(eventDAO);


        mDatePickerDialogFragment = new DatePickerDialogFragment(this);
        dateEditText.setOnClickListener(this);

        setLocations();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
            }
        });

    }

    private void saveEvent() {
        String title = titleEditText.getText() != null ? titleEditText.getText().toString() : "";
        String description = descriptionEditText.getText() != null ? descriptionEditText.getText().toString() : "";
        String location = locationSpinner.getSelectedItem() != null ? locationSpinner.getSelectedItem().toString() : "";
        String date = dateEditText.getText().toString();

        if (isStringNullOrEmpty(title)) {
            Toast.makeText(this, "Unable to save event. Please add title.", Toast.LENGTH_SHORT).show();
        }

        event = new Event();
        event.setTitle(title);

        if (!isStringNullOrEmpty(description))
            event.setDescription(description);

        if (!isStringNullOrEmpty(date))
            event.setDate(date);

        if (!isStringNullOrEmpty(location) && databaseInstance != null && locationDAO != null && locationService != null) {
            Location locationModel = locationService.getLocationByName(location);
            event.setLocation(locationModel);
        }


        if (databaseInstance != null && eventDAO != null && eventService != null) {
            Calendar calendar = Calendar.getInstance();
            String currentDate = Utils.stringFromDate(calendar);
            event.setAddedDate(currentDate);

            eventService.addEvent(event);
        }

        finish();

    }


    @Override
    public void onClick(Calendar calendar) {
        updateLabel(calendar);
    }

    @Override
    public void onClick(View v) {
        mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void updateLabel(Calendar calendar) {
        String selectedate = Utils.stringFromDate(calendar);
        dateEditText.setText(selectedate);
    }

    private void setLocations() {

        if (databaseInstance == null || locationDAO == null || locationService == null) return;

        ArrayList<String> spinnerItems = new ArrayList<>();

        List<Location> locations = locationService.getLocations();
        for (Location location : locations) {
            String locationNameAndType = location.getName() + " - " + location.getType();
            spinnerItems.add(locationNameAndType);
        }

        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, spinnerItems) {

            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                return v;
            }
        };


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(spinnerAdapter);

        locationSpinner.setSelection(0);

    }


}
