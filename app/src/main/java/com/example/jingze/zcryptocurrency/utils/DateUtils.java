package com.example.jingze.zcryptocurrency.utils;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    //"2018.03.31 13:00"
    private static DateFormat dateFormat =
            new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());

    // "Mar.31, 2018 - Sat"
    private static DateFormat dateFormatDate =
            new SimpleDateFormat("MMM.dd, yyyy - EEE", Locale.getDefault());

    // "13:00"
    private static DateFormat dateFormatTime =
            new SimpleDateFormat("HH:mm", Locale.getDefault());

    @NonNull
    public static Date stringToDate(@NonNull String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            return Calendar.getInstance().getTime();
        }
    }

    @NonNull
    public static String dateToString(@NonNull Date date) {
        return dateFormat.format(date);
    }

    @NonNull
    public static String dateToStringDate(@NonNull Date date) {
        return dateFormatDate.format(date);
    }

    @NonNull
    public static String dateToStringTime(@NonNull Date date) {
        return dateFormatTime.format(date);
    }
}
