package com.example.agenda.ui.utils;

import android.widget.ImageView;

import com.example.agenda.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static boolean isStringNullOrEmpty(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }

    public static void loadImageFromURLNoPlaceholders(String url, ImageView imageView) {
        if (isStringNullOrEmpty(url)) {
            imageView.setImageResource(R.drawable.ic_placeholder);
        } else {
            Picasso.get()
                    .load(url)
                    .error(R.drawable.ic_placeholder)
                    .into(imageView);
        }
    }

    public static String stringFromDate(Calendar calendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat format = new SimpleDateFormat(myFormat, Locale.US);
       return format.format(calendar.getTime());
    }

    public static Calendar dateFromString(String dateString) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat format = new SimpleDateFormat(myFormat, Locale.US);

        Date date = new Date();

        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }

        return cal;
    }


}
