package com.itsaunixsystem.marinara.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ajdt on 8/23/16.
 * @description: generates a List<MockPomodoroSession>
 *     NOTE: for now I've hardcoded a few sessions, so I can focus on developing the StatsActivity
 *     instead. If I need a fuller implementation (maybe loading mock data from json files?), I'll
 *     add to this implementation later. For now, we'll follow the YAGNI principle.
 */
public class MockSessionsLoader extends SessionQueryHelper {

    public List<Session> getAllSessions() {
        ArrayList<Session> sessions = new ArrayList<Session>() ;
        long duration = 25 * 60 * 1000 ; // 25 min in millisec format

        // 3 sessions in one day Mon aug 15th
        sessions.add(new MockPomodoroSession("code", duration, "2016:08:15 10:12:12")) ;
        sessions.add(new MockPomodoroSession("code", duration, "2016:08:15 11:30:15")) ;
        sessions.add(new MockPomodoroSession("code", duration, "2016:08:15 12:47:33")) ;

        // another two sessions on Thurs aug 18th
        sessions.add(new MockPomodoroSession("chores", duration, "2016:08:18 15:11:23")) ;
        sessions.add(new MockPomodoroSession("chores", duration, "2016:08:18 18:55:49")) ;

        // five sessions on Friday aug 19th
        sessions.add(new MockPomodoroSession("code", duration, "2016:08:19 08:32:49")) ;
        sessions.add(new MockPomodoroSession("code", duration, "2016:08:19 09:15:17")) ;
        sessions.add(new MockPomodoroSession("chores", duration, "2016:08:19 12:21:21")) ;
        sessions.add(new MockPomodoroSession("chores", duration, "2016:08:19 13:38:56")) ;
        sessions.add(new MockPomodoroSession("code", duration, "2016:08:19 20:15:14")) ;

        // one session on sunday aug 21st
        sessions.add(new MockPomodoroSession("code", duration, "2016:08:21 09:15:17")) ;

        return sessions ;
    }
}
