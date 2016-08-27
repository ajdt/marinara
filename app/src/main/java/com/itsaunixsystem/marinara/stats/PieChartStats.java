package com.itsaunixsystem.marinara.stats;

import com.github.mikephil.charting.data.PieEntry;
import com.itsaunixsystem.marinara.mock.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: ajdt on 8/26/16.
 * @description: aggregates sessions provided by List<Session> by task name. Produces an
 * ArrayList<PieEntry> for each task name containing the number of sessions with that task name
 */
public class PieChartStats implements SessionStats{

    ArrayList<Session> _all_sessions;
    ArrayList<PieEntry> _entries;
    ArrayList<String> _labels;

    public PieChartStats(List<Session> sessions) {
        _all_sessions = new ArrayList<Session>(sessions) ;

        computeEntries(sessions) ;
    }
    /****************************** GETTERS ******************************/
    public ArrayList<PieEntry> getEntries() { return new ArrayList<PieEntry>(_entries) ; }
    public ArrayList<String> getLabels() { return new ArrayList<String>(_labels) ; }
    public ArrayList<Session> getOriginalSessions() { return new ArrayList<Session>(_all_sessions) ; }

    /****************************** HELPERS ******************************/
    /**
     * Compute number of sessions per task name to generate a list of
     * PieEntry(task_number, session_count_for_task), where "task_number"
     * is just the index of the PieEntry in its ArrayList. PieEntry can
     * only be constructed with numeric values, so we use index numbers
     * to identify task names. However, when displayed a pie chart will use
     * task names as labels instead of the given index numbers.
     *
     * @param sessions
     */
    private void computeEntries(List<Session> sessions) {
        HashMap<String, Integer> task_to_session_count = new HashMap<String, Integer>() ;

        // count the number of sessions per task
        for (Session some_session: sessions) {
            if (task_to_session_count.containsKey(some_session.taskName())) {
                // increment count
                task_to_session_count.put(some_session.taskName(),
                        task_to_session_count.get(some_session.taskName()) + 1) ;
            } else {
                task_to_session_count.put(some_session.taskName(), 1) ;
            }
        }

        // create an entry for each task, and save a label for each entry
        _entries = new ArrayList<PieEntry>() ;
        _labels = new ArrayList<String>() ;
        int task_index_number = 0 ;
        for (String task_name : task_to_session_count.keySet()) {
            // create entry & label
            _entries.add(new PieEntry(task_index_number, task_to_session_count.get(task_name))) ;
            _labels.add(task_name) ;

            task_index_number++ ;
        }

    }
}

