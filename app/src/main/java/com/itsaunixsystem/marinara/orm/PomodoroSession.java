package com.itsaunixsystem.marinara.orm;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * @author: ajdt on 8/6/16.
 * @description: an orm class representing a single pomodoro session
 */
public class PomodoroSession extends SugarRecord {

    private Date start_time ;

    private long duration ;

    private Task task ;

    public PomodoroSession() {}

    public PomodoroSession(Date started_at, long duration) {
        this.start_time = new Date(started_at.getTime()) ;
        this.duration = duration ;
    }
}
