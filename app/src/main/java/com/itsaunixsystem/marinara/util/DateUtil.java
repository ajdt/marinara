package com.itsaunixsystem.marinara.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author: ajdt on 8/23/16.
 * @description:
 */
public class DateUtil {



    // static members
    private static SimpleDateFormat __date_time_formatter =
            new SimpleDateFormat("yyyy:MM:dd HH:mm:ss") ;
    private static SimpleDateFormat __date_formatter = new SimpleDateFormat("yyyy:MM:dd") ;


    /**
     *
     * @param to_check
     * @param start_date
     * @param end_date
     * @return true if date is within the inclusive range
     */
    public static boolean dateWithinRange(Date to_check, Date start_date, Date end_date) {
        return !to_check.before(start_date) && !to_check.after(end_date) ;
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
     * @return string representing calendar date of paramater Date
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

    public static Date getPreviousMonday(Date the_date) {
        Calendar cal = new GregorianCalendar() ;
        cal.setTime(the_date) ;

        // decrement by one day until we get to the previous monday
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            cal.add(Calendar.DATE, -1) ;

        return cal.getTime() ;
    }


    // TODO: instead of Today use fixed date within mock dates


    public static DateRange getMonthRangeFromDate(Date the_date) {
        Calendar cal = new GregorianCalendar() ;
        cal.setTime(the_date) ;

        int days_in_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH) ;

        Calendar first_of_month = new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DATE)) ;
        Calendar last_of_month = new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DATE)) ;

        return new DateRange(first_of_month.getTime(), last_of_month.getTime()) ;
    }

    public static DateRange getWeekRangeFromDate(Date the_date) {
        Date monday = getPreviousMonday(the_date) ;
        Calendar cal = new GregorianCalendar() ;
        cal.setTime(monday) ;
        cal.add(Calendar.DATE, 6) ; // advance to sunday

        return new DateRange(monday, cal.getTime()) ;
    }




}
