package com.example.agenda.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.agenda.R;
import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.EventDAO;
import com.example.agenda.ui.data.EventService;
import com.example.agenda.ui.data.LocationDAO;
import com.example.agenda.ui.data.LocationService;
import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.model.Location;
import com.example.agenda.ui.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.agenda.ui.utils.Utils.isStringNullOrEmpty;

public class ContentFragment  extends Fragment {

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_event, container, false);

        titleEditText = root.findViewById(R.id.titleEditText);
        dateEditText = root.findViewById(R.id.dateEditText);
        locationSpinner = root.findViewById(R.id.locationSpinner);
        descriptionEditText = root.findViewById(R.id.descriptionEditText);
        doneButton = root.findViewById(R.id.doneButton);

        databaseInstance = DatabaseInstance.getInstance(getContext());

        //LocationService
        locationDAO = databaseInstance.locationDAO();
        locationService = new LocationService(locationDAO);

        //EventService
        eventDAO = databaseInstance.eventDAO();
        eventService = new EventService(eventDAO);


        setDate();
        setLocations();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
            }
        });
        return root;
    }


    private void saveEvent() {
        String title = titleEditText.getText() != null ? titleEditText.getText().toString() : "";
        String description = descriptionEditText.getText() != null ? descriptionEditText.getText().toString() : "";
        String location = locationSpinner.getSelectedItem() != null ? locationSpinner.getSelectedItem().toString() : "";
        String date = dateEditText.getText().toString();

        if (isStringNullOrEmpty(title)) {
            Toast.makeText(getActivity(), "Unable to save event. Please add title.", Toast.LENGTH_SHORT).show();
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

    }

    private void setDate() {
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(calendar);
            }
        };

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() == null) return;

                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
            spinnerItems.add(location.getName());
        }

        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinnerItems) {

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