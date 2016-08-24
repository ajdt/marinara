package com.itsaunixsystem.marinara.mock;

import com.itsaunixsystem.marinara.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: ajdt on 8/23/16.
 * @description: A common "interface" implemented by all classes used to look up PomodoroSessions.
 * Created to facilitate substituting mock PomodoroSessions objects (MockPomodoroSession)
 * during development.
 */
public abstract class SessionQueryHelper {

    public abstract List<Session> getAllSessions() ;

    /****************************** IMPLEMENTED METHODS ******************************/
    public List<Session> getSessionsInRange(Date start_date, Date end_date) {
        ArrayList<Session> filtered_sessions = new ArrayList<Session>() ;
        for (Session session : this.getAllSessions() ) {
            if (DateUtil.dateWithinRange(session.completionDate(), start_date, end_date))
                filtered_sessions.add(session) ;
        }

        return filtered_sessions ;

    }

    public List<Session> getSessionsWithTaskName(String task_name) {
        ArrayList<Session> filtered_sessions = new ArrayList<Session>() ;
        for (Session session : this.getAllSessions() ) {
            if (session.taskName().equals(task_name))
                filtered_sessions.add(session) ;
        }

        return filtered_sessions ;
    }

}
