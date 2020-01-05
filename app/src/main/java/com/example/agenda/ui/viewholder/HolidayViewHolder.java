package com.example.agenda.ui.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.agenda.R;
import com.example.agenda.ui.utils.Utils;
import com.squareup.picasso.Picasso;

public class HolidayViewHolder extends RecyclerView.ViewHolder {

    public HolidayViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(String image, String name, String date) {
        ImageView holidayImage = itemView.findViewById(R.id.holidayImage);
        TextView holidayName = itemView.findViewById(R.id.holidayName);
        TextView holidayDay = itemView.findViewById(R.id.holidayDate);

        Utils.loadImageFromURLNoPlaceholders(image, holidayImage);
        holidayName.setText(name);
        holidayDay.setText(date);
    }
}