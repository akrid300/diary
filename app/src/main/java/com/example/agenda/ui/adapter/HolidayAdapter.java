package com.example.agenda.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.ui.model.HolidayModel;
import com.example.agenda.ui.viewholder.HolidayViewHolder;

import java.util.ArrayList;

public class HolidayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<HolidayModel> items = new ArrayList<>();
    private Context context;

    // Provide a suitable constructor (depends on the kind of data set)
    public HolidayAdapter(Context context, ArrayList<HolidayModel> items) {
        this.context = context;
        if (items != null)
            this.items = items;
    }

    // Return the size of your data-set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View eventView = inflater.inflate(R.layout.list_item_holiday, viewGroup, false);
        viewHolder = new HolidayViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        HolidayViewHolder holidayViewHolder = (HolidayViewHolder) viewHolder;
        configureHolidayViewHolder(holidayViewHolder, position);
    }

    private void configureHolidayViewHolder(HolidayViewHolder holidayViewHolder, int position) {
        HolidayModel holidayModel = items.get(position);
        holidayViewHolder.bind(holidayModel.getImage(), holidayModel.getTitle(), holidayModel.getDate());
    }
}
