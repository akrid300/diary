package com.example.agenda.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.ui.activity.EventActivity;
import com.example.agenda.ui.activity.MapsActivity;
import com.example.agenda.ui.adapter.EventsAdapter;
import com.example.agenda.ui.data.DatabaseInstance;
import com.example.agenda.ui.data.EventDAO;
import com.example.agenda.ui.data.EventService;
import com.example.agenda.ui.listener.RecyclerViewClickListener;
import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.utils.ContextMenuManager;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements RecyclerViewClickListener {

    private int RESULT_ADD_EVENT = 1000;
    private Button addEventButton;
    private Button viewOnMap;
    private TextView emptyList;

    private ArrayList events = new ArrayList();

    private DatabaseInstance databaseInstance;
    private EventDAO eventDAO;
    private EventService eventService;
    private RecyclerView eventsList;

    private EventsAdapter eventsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        addEventButton = root.findViewById(R.id.addEventButton);
        viewOnMap = root.findViewById(R.id.mapButton);
        emptyList = root.findViewById(R.id.emptyList);
        eventsList = root.findViewById(R.id.eventsList);

        // Show an empty list in case there are no events
        if (events.size() == 0) {
            emptyList.setVisibility(View.VISIBLE);
            eventsList.setVisibility(View.GONE);
        }
        else {
            emptyList.setVisibility(View.GONE);
            eventsList.setVisibility(View.VISIBLE);
        }

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the EventActivity
                Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                startActivityForResult(eventIntent, RESULT_ADD_EVENT);
            }
        });

        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the MapActivity
                Intent eventIntent = new Intent(getActivity(), MapsActivity.class);
                startActivity(eventIntent);
            }
        });

        // Get an instance of the database and the event service
        //EventService
        databaseInstance = DatabaseInstance.getInstance(getContext());
        eventDAO = databaseInstance.eventDAO();
        eventService = new EventService(eventDAO);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        eventsList.setLayoutManager(layoutManager);

        // Get the list of events from the database
        ArrayList<Event> events = new ArrayList<>(eventService.getEvents());

        // Create an events adapter to show the list of events
        eventsAdapter = new EventsAdapter(getContext(), this);
        eventsAdapter.setItems(events);

        // Attach the adapter to the recycler view
        eventsList.setAdapter(eventsAdapter);

        checkEmptyList();


        // Hide the context menu on scroll
        eventsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ContextMenuManager.getInstance(getActivity()).onScrolled(dy, false);
            }
        });

        return root;
    }

    // Recycler view listener on click
    @Override
    public void onClick(int position) {

    }


    // Called when the user clicks on edit on the context menu - for one item in the list
    @Override
    public void onEditClick(int position, Long eventId) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra("id", eventId);

        // Closing the Event Activity will call onActivityResult
        startActivityForResult(intent, RESULT_ADD_EVENT);
    }

    // Called when the user clicks on delete on the context menu - for one item in the list
    @Override
    public void onDelete(int position, Long eventId) {
        eventService.deleteEventById(eventId);
        checkEmptyList();
    }

    // Hide the context menu
    @Override
    public void onDestroyView() {
        ContextMenuManager.getInstance(getActivity()).hideContextMenu(true);
        super.onDestroyView();
    }

    // Method to check whether or not there are items in the list
    private void checkEmptyList() {
        if (eventsAdapter.getItemCount() > 0) {
            eventsList.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
        }
        else {
            eventsList.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    // Method called when the Event Activity is closed, after the user edited a filter
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RESULT_ADD_EVENT ) {
            ArrayList<Event> events = new ArrayList<>(eventService.getEvents());
            eventsAdapter.setItems(events);
            checkEmptyList();
        }
    }
}