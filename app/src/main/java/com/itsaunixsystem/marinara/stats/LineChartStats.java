package com.itsaunixsystem.marinara.stats;

import com.github.mikephil.charting.data.Entry;
import com.itsaunixsystem.marinara.mock.Session;
import com.itsaunixsystem.marinara.util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author: ajdt on 8/23/16.
 * @description: aggregates sessions provided by List<Session> object by first ordering the
 * sessions by completion date and then calculating the number of sessions that were
 * completed per calendar date.
 */
public class LineChartStats implements SessionStats {

    // data members
    private ArrayList<Session> _sessions;
    private ArrayList<String> _labels;
    private ArrayList<Entry> _entries;


    public LineChartStats(List<Session> sessions) {
        _sessions = new ArrayList<Session>(sessions) ;
        computeEntries(sessions) ;
    }

    /****************************** GETTERS ******************************/
    public ArrayList<Entry> getEntries() { return new ArrayList<Entry>(_entries) ; }
    public ArrayList<String> getLabels() { return new ArrayList<String>(_labels) ; }
    public ArrayList<Session> getOriginalSessions() { return new ArrayList<Session>(_sessions) ; }

    /****************************** HELPERS ******************************/
    /**
     * Group sessions by calendar date, create a list of Entry(date_number, number_of_sessions)
     * per calendary date
     *
     * date_number is the number of days elapsed between the given calendar date and the
     * chronologically first date. It's a simple way to assign numeric values to the dates
     * which is required by MPAndroidChart's Entry objects.
     *
     * @param sessions
     */
    private void computeEntries(List<Session> sessions) {

        // count the number of sessions for every calendar day
        HashMap<Date, Integer> day_to_num_sessions = new HashMap<Date, Integer>() ;
        for(Session some_session : sessions) {
            Date canonical_date = DateUtil.canonicalize(some_session.completionDate()) ;

            if (day_to_num_sessions.containsKey(canonical_date)) {
                day_to_num_sessions.put(canonical_date,
                        day_to_num_sessions.get(canonical_date) + 1) ;
            } else {
                day_to_num_sessions.put(canonical_date, 1) ;
            }
        }

        // sort the distinct calendar days and find the first day
        ArrayList<Date> date_list = new ArrayList<Date>() ;
        for (Date some_date : day_to_num_sessions.keySet())
            date_list.add(some_date) ;
        Collections.sort(date_list) ;
        Date first_date = date_list.get(0) ;


        _labels = new ArrayList<String>() ;
        _entries = new ArrayList<Entry>() ;

        // for each day: use the calendar date as a label
        // and create an entry using days elapsed (since first day) and number of sessions on
        // that day.
        for (Date date: date_list) {
            _entries.add(new Entry(DateUtil.daysBetween(first_date, date) + 1,
                    day_to_num_sessions.get(date))) ;
            _labels.add(DateUtil.toCalendarDateString(date)) ;
        }

    }
}
