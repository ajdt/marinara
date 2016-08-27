package com.itsaunixsystem.marinara.stats;

import com.github.mikephil.charting.data.Entry;
import com.itsaunixsystem.marinara.mock.Session;

import java.util.ArrayList;

/**
 * @author: ajdt on 8/23/16.
 * @description: An interface common to all objects that calculate entry stats from a List<Session>
 */
public interface SessionStats {
    public ArrayList<? extends Entry> getEntries() ;
    public ArrayList<String> getLabels() ;
    public ArrayList<Session> getOriginalSessions() ;
}
