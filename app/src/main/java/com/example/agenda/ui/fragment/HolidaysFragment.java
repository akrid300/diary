package com.example.agenda.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.ui.adapter.HolidayAdapter;
import com.example.agenda.ui.model.HolidayModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HolidaysFragment extends Fragment {

    public HolidaysFragment() {
    }

    public static HolidaysFragment newInstance() {
        HolidaysFragment fragment = new HolidaysFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_holiday, container, false);
        TextView emptyView = root.findViewById(R.id.emptyList);
        RecyclerView holidaysList = root.findViewById(R.id.holidaysList);

        if (getActivity() == null) return root;
        try {
            InputStream is = getActivity().getAssets().open("holidays.json");
            Gson gson = new GsonBuilder().create();

            ArrayList<HolidayModel> holidays = new ArrayList<>();
            JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            reader.beginArray();
            while (reader.hasNext()) {
                HolidayModel holidayModel = gson.fromJson(reader, HolidayModel.class); //gson.fromJson(reader, Holidays.class);
                if (holidayModel != null && holidayModel.getTitle() != null) {
                    holidays.add(holidayModel);
                }
            }

            if (holidays.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                holidaysList.setVisibility(View.GONE);
            }
            else {
                emptyView.setVisibility(View.GONE);
                holidaysList.setVisibility(View.VISIBLE);
            }

            HolidayAdapter holidayAdapter = new HolidayAdapter(getContext(), holidays);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            holidaysList.setLayoutManager(layoutManager);
            holidaysList.setAdapter(holidayAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }


}