package com.itsaunixsystem.marinara.orm;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * @author: ajdt on 8/6/16.
 * @description: an orm class representing a single pomodoro session
 */
public class PomodoroSession extends SugarRecord {

    // NOTE: all fields are made public b/c I am not doing any error checking on values assigned
    // to fields so setters/getters seemed redundant
    public Date completion_date ;

    public long duration ;

    public Task task ;

    public PomodoroSession() {}

    public PomodoroSession(Date started_at, long duration) {
        this.completion_date    = new Date(started_at.getTime()) ;
        this.duration           = duration ;
    }
}
