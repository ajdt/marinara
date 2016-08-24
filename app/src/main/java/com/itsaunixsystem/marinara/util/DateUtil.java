package com.itsaunixsystem.marinara.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: ajdt on 8/23/16.
 * @description: ${0}
 */
public class DateUtil {

    // static members
    private static SimpleDateFormat __date_formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss") ;

    
    public static boolean dateWithinRange(Date to_check, Date start_date, Date end_date) {
        return to_check.compareTo(start_date) >= 0 && to_check.compareTo(end_date) <= 0 ;
    }

    public static Date parseDateString(String date_string) {
        Date parsed_date ;

        try {
            parsed_date = __date_formatter.parse(date_string) ;
        } catch (ParseException except) {
            // NOTE: for now, just use current date on failure.
            Log.d(DateUtil.class.getSimpleName(), "invalid date string:" + date_string) ;
            parsed_date = new Date() ;
        }

        return parsed_date ;
    }
}
