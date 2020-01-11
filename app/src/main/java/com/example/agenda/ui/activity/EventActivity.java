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
import java.util.HashMap;
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

    ArrayList<String> spinnerItems = new ArrayList<>();
    HashMap<String, Long> locationsList = new HashMap<>();

    DatePickerDialogFragment mDatePickerDialogFragment;

    private Long eventId = null;

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

        eventId = null;

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

        if (getIntent() != null && getIntent().getExtras() != null) {
            loadEvent(getIntent().getLongExtra("id", 0));
        }

    }

    private  void loadEvent(Long id) {
        Event event = eventService.getEventById(id);
        if (event == null) return;

        eventId = id;

        String title = event.getTitle();
        String date = event.getDate();

        Location location = event.getLocation();
        Integer locationPosition = null;
        if (location != null) {
            String locationNameAndType = getLocationWithType(location);
            locationPosition = spinnerItems.indexOf(locationNameAndType);
        }

        String description = event.getDescription();

        titleEditText.setText(title);
        if (!Utils.isStringNullOrEmpty(date))
            dateEditText.setText(date);
        if (locationPosition != null)
            locationSpinner.setSelection(locationPosition);
        if (!Utils.isStringNullOrEmpty(description))
            descriptionEditText.setText(description);

    }

    private void saveEvent() {
        String title = titleEditText.getText() != null ? titleEditText.getText().toString() : "";
        String description = descriptionEditText.getText() != null ? descriptionEditText.getText().toString() : "";
        String location = locationSpinner.getSelectedItem() != null ? locationSpinner.getSelectedItem().toString() : "";
        String date = dateEditText.getText().toString();

        Location locationModel = null;
        if (!isStringNullOrEmpty(location) && databaseInstance != null && locationDAO != null && locationService != null) {
            Long locationId = locationsList.get(location);
            locationModel = locationService.getLocationById(locationId);
        }

        if (isStringNullOrEmpty(title)) {
            Toast.makeText(this, "Unable to save event. Please add title.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (eventId != null) {
            if (locationModel != null)
                eventService.updateEventWithLocation(title, date, description, locationModel.getId(),
                        locationModel.getName(), locationModel.getType(), locationModel.getLatitude(), locationModel.getLongitude(), eventId);
            else
                eventService.updateEvent(title, date, description, eventId);
        }
        else  {
            event = new Event();
            event.setTitle(title);

            if (!isStringNullOrEmpty(description))
                event.setDescription(description);

            if (!isStringNullOrEmpty(date))
                event.setDate(date);


            if (locationModel != null)
                event.setLocation(locationModel);


            if (databaseInstance != null && eventDAO != null && eventService != null) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = Utils.stringFromDate(calendar);
                event.setAddedDate(currentDate);

                eventService.addEvent(event);
            }
        }

        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onDateClick(Calendar calendar) {
        updateLabel(calendar);
    }

    @Override
    public void onClick(View v) {
        if (!mDatePickerDialogFragment.isAdded())
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void updateLabel(Calendar calendar) {
        String selectedate = Utils.stringFromDate(calendar);
        dateEditText.setText(selectedate);
    }

    private void setLocations() {

        if (databaseInstance == null || locationDAO == null || locationService == null) return;

        List<Location> locations = locationService.getLocations();
        for (Location location : locations) {
            String locationNameAndType = getLocationWithType(location);
            spinnerItems.add(locationNameAndType);
            locationsList.put(locationNameAndType, location.getId());

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


    private String getLocationWithType(Location location) {
        return location.getName() + " - " + location.getType();
    }
}
