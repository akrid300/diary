package com.example.agenda.ui.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.ui.listener.RecyclerViewClickListener;
import com.example.agenda.ui.model.EventModel;
import com.example.agenda.ui.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventViewHolder  extends RecyclerView.ViewHolder {

    public EventViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(int position, EventModel eventModel, RecyclerViewClickListener listener) {
        ImageView menuButton = itemView.findViewById(R.id.menu_button);
        TextView eventTitle = itemView.findViewById(R.id.titleTextView);
        TextView eventDate = itemView.findViewById(R.id.dateTextView);
        SeekBar seekBar = itemView.findViewById(R.id.dateSeekBar);
        TextView description = itemView.findViewById(R.id.descriptionTextView);

        eventTitle.setText(eventModel.getTitle());

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar curentDate = Calendar.getInstance();

        if (!Utils.isStringNullOrEmpty(eventModel.getAddedDate())) {
            startDate = Utils.dateFromString(eventModel.getAddedDate());
        }

        if (!Utils.isStringNullOrEmpty(eventModel.getDate())) {
            endDate = Utils.dateFromString(eventModel.getDate());
        }

        int progress = 100;
        int max = 100;

        long msDiff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        Long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        if (daysDiff > 0) {
            max = daysDiff.intValue();
        }

        long currentMsDiff = endDate.getTimeInMillis() - curentDate.getTimeInMillis();
        Long currentDaysDiff = TimeUnit.MILLISECONDS.toDays(currentMsDiff);
        if (currentDaysDiff > 0) {
            progress = currentDaysDiff.intValue();
        }


        seekBar.setMax(max);
        seekBar.setProgress(progress);




        if (!Utils.isStringNullOrEmpty(eventModel.getDate())) {
            eventDate.setText(eventModel.getDate());
            eventDate.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
        }
        else {
            eventDate.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
        }

        if (!Utils.isStringNullOrEmpty(eventModel.getDescription())) {
            description.setText(eventModel.getDescription());
            description.setVisibility(View.VISIBLE);
        }
        else {
            description.setVisibility(View.GONE);
        }
    }
}