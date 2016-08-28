package com.itsaunixsystem.marinara.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author: ajdt on 8/23/16.
 * @description: contains static helpers for comparing and manipulating Date objects not provided
 * in java.util.Date
 */
public class DateUtil {

    // static members
    private static SimpleDateFormat __date_time_formatter =
            new SimpleDateFormat("yyyy:MM:dd HH:mm:ss") ;
    private static SimpleDateFormat __date_formatter = new SimpleDateFormat("yyyy:MM:dd") ;


    /**
     * NOTE: this function ignores time of day. "2016:01:01 00:00:00" will be in the range
     * start_date = "2016:01:01 15:33:00" end_date "2016:01:01 16:33:00"
     *
     * @param to_check
     * @param start_date
     * @param end_date
     * @return true if date is within the inclusive range
     */
    public static boolean dateWithinDayRange(Date to_check, Date start_date, Date end_date) {
        // zero out time values for start/end dates before checking if date falls within range
        Date canonical_to_check = DateUtil.canonicalize(to_check) ;
        Date canonical_start = DateUtil.canonicalize(start_date) ;
        Date canoical_end = DateUtil.canonicalize(end_date) ;

        return !canonical_to_check.before(canonical_start)
                && !canonical_to_check.after(canoical_end) ;

    }

    /**
     *
     * @param date_string a string in the format "yyyy:MM:dd HH:mm:ss"
     * @return Date object representing parsed date/time or null if cannot parse
     */
    public static Date parseDateString(String date_string) {
        Date parsed_date ;

        try {
            parsed_date = __date_time_formatter.parse(date_string) ;
        } catch (ParseException except) {
            Log.d(DateUtil.class.getSimpleName(), "invalid date string:" + date_string) ;
            parsed_date = null ;
        }

        return parsed_date ;
    }

    /**
     * Date(1999, 11, 7, 15, 33, 23), [year, month, day, hour, min, sec] --> "1999:11:15"
     *
     * @param the_date
     * @return string representing calendar date of paramater Date object
     */
    public static String toCalendarDateString(Date the_date) {
        return __date_formatter.format(the_date) ;
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
            // format with only date info and then parse
            return __date_formatter.parse(__date_formatter.format(the_date)) ;
        } catch(ParseException except) {

            // TODO: uses deprecated methods. java.util.Date is kind of a mess anyway.
            // consider using another date/time class instead
            return new Date(the_date.getYear(), the_date.getMonth(), the_date.getDay()) ;
        }
    }

    /**
     * @return today's date in canonicalized format
     */
    public static Date todayCanonicalized() { return canonicalize(new Date()) ; }

    /**
     * @param the_date
     * @return Date object representing the last Monday that occurred before the_date
     */
    public static Date getPreviousMonday(Date the_date) {
        Calendar cal = new GregorianCalendar() ;
        cal.setTime(the_date) ;

        // decrement by one day until we get to the previous monday
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            cal.add(Calendar.DATE, -1) ;

        return cal.getTime() ;
    }

    /**
     * @param the_date
     * @return DateRange for entire month that the_date takes place. first day of the month as
     * lower bound and last day of month as upper bound.
     */
    public static DateRange getMonthRangeFromDate(Date the_date) {
        Calendar cal = new GregorianCalendar() ;
        cal.setTime(the_date) ;

        // find number of days in month
        int days_in_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH) ;

        // create Calendar objects for first and last day of month
        Calendar first_of_month = new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), 1) ;
        Calendar last_of_month = new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), days_in_month) ;

        // canonicalize and create range
        return new DateRange(DateUtil.canonicalize(first_of_month.getTime()),
                canonicalize(last_of_month.getTime())) ;
    }

    /**
     * @param the_date
     * @return DateRange for week that the_date takes place in. last monday before the_date as
     * lower bound and following sunday as upper bound.
     */
    public static DateRange getWeekRangeFromDate(Date the_date) {
        Date monday = getPreviousMonday(the_date) ;

        // create calendar set to monday and advance to sunday
        Calendar cal = new GregorianCalendar() ;
        cal.setTime(monday) ;
        cal.add(Calendar.DATE, 6) ;

        // canonicalize and create range
        return new DateRange(DateUtil.canonicalize(monday),
                DateUtil.canonicalize(cal.getTime())) ;
    }



}
