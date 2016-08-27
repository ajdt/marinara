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

    private ArrayList<Session> all_sessions ;
    private ArrayList<String> labels ;
    private ArrayList<Entry> entries ;

    public LineChartStats(List<Session> sessions) {
        all_sessions = new ArrayList<Session>(sessions) ;

        computeEntries(sessions) ;

    }

    public ArrayList<Entry> getEntries() { return entries ; }
    public ArrayList<String> getLabels() { return labels ; }

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
        List<Date> date_list = Arrays.asList((Date[]) day_to_num_sessions.keySet().toArray()) ;
        Collections.sort(date_list) ;
        Date first_date = date_list.get(0) ;


        labels = new ArrayList<String>() ;
        entries = new ArrayList<Entry>() ;

        // for each day: use the calendar date as a label
        // and create an entry using days elapsed (since first day) and number of sessions on
        // that day.
        for (Date date: date_list) {
            entries.add(new Entry(DateUtil.daysBetween(first_date, date) + 1,
                    day_to_num_sessions.get(date))) ;
            labels.add(DateUtil.dateObjectToDateString(date)) ;
        }

    }
}
