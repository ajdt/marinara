package com.itsaunixsystem.marinara.mock;

import com.itsaunixsystem.marinara.orm.Task;

import java.util.Date;

/**
 * @author: ajdt on 8/23/16.
 * @description: an interface implemented by both MockSessions and PomodoroSessions.
 * NOTE: this interface exists so I can easily use mock data during development without
 * relying on a full mocking framework.
 */
public interface Session {
    public String taskName() ;
    public long duration();
    public Date completionDate() ;
}
