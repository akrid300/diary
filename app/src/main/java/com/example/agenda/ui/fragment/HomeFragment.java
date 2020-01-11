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
                Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                startActivityForResult(eventIntent, RESULT_ADD_EVENT);
            }
        });

        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventIntent = new Intent(getActivity(), MapsActivity.class);
                startActivity(eventIntent);
            }
        });

        //EventService
        databaseInstance = DatabaseInstance.getInstance(getContext());
        eventDAO = databaseInstance.eventDAO();
        eventService = new EventService(eventDAO);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        eventsList.setLayoutManager(layoutManager);

        ArrayList<Event> events = new ArrayList<>(eventService.getEvents());

        eventsAdapter = new EventsAdapter(getContext(), this);
        eventsAdapter.setItems(events);
        eventsList.setAdapter(eventsAdapter);

        checkEmptyList();


        eventsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ContextMenuManager.getInstance(getActivity()).onScrolled(dy, false);
            }
        });

        return root;
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onEditClick(int position, Long eventId) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra("id", eventId);
        startActivityForResult(intent, RESULT_ADD_EVENT);
    }

    @Override
    public void onDelete(int position, Long eventId) {
        eventService.deleteEventById(eventId);
        checkEmptyList();
    }

    @Override
    public void onDestroyView() {
        ContextMenuManager.getInstance(getActivity()).hideContextMenu(true);
        super.onDestroyView();
    }

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