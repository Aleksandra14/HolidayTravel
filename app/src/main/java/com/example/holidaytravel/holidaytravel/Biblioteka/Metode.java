package com.example.holidaytravel.holidaytravel.Biblioteka;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Metode {

    public static long getDateCurrentMillis(String startDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        //Locale.getDefault();

        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));

        try {
            Date d = sdf.parse(startDate);

            return d.getTime();

        } catch (Exception e) {
            System.out.println("");
        }

        return 0;
    }



}
