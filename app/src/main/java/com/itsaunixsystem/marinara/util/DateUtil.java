package com.itsaunixsystem.marinara.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: ajdt on 8/23/16.
 * @description:
 */
public class DateUtil {

    // static members
    private static SimpleDateFormat __date_formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss") ;
    private static SimpleDateFormat __string_formatter = new SimpleDateFormat("yyyy:MM:dd") ;


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

    public static String dateObjectToDateString(Date the_date) {
        return __string_formatter.format(the_date) ;
    }

    /**
     * NOTE: order doesn't matter, always returns non-negative answer
     * @param first
     * @param second
     * @return the number of days between two Date objects as truncated long
     */
    public static long daysBetween(Date first, Date second) {
        long millisec = Math.abs(second.getTime() - first.getTime()) ;
        return millisec / 1000 / 60 / 60 / 24 ; // divide to get days
    }

    /**
     * NOTE: canonicalizing a date means zeroing out all time of day information
     * @param the_date
     * @return a new Date object set to same year, month and day, but with the time of the day
     * set to 0 hours, 0 min, 0 seconds (i.e. "YYYY:MM:DD hh:mm:ss" ---> "YYYY:MM:DD 00:00:00"
     */
    public static Date canonicalize(Date the_date) {
        try {
            return __string_formatter.parse(__string_formatter.format(the_date)) ;
        } catch(ParseException except) {

            // TODO: uses deprecated methods. java.util.Date is kind of a mess anyway.
            // consider using another date/time class instead
            return new Date(the_date.getYear(), the_date.getMonth(), the_date.getDay()) ;
        }
    }


}
