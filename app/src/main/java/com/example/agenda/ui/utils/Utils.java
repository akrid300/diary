package com.example.agenda.ui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
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

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    private static int screenHeight = 0;
    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                screenHeight = size.y;
            }
        }
        return screenHeight;
    }
}
