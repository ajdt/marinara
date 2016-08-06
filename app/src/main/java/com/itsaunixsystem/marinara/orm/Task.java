package com.itsaunixsystem.marinara.orm;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * @author: ajdt on 8/6/16.
 * @description: an orm class to facilitate DB access of tasks users create/delete
 */
public class Task extends SugarRecord {

    // TODO: not sure what this does, if anything
    @Unique
    private String name ;

    public Task() { }

    public Task(String name) { this.name = name ; }

    // a single task may have multiple sessions
    // this is the only way I know to obtain those sessions
    public List<PomodoroSession> getPomodoroSessions() {
        return PomodoroSession.find(PomodoroSession.class, "task = ?", Long.toString(getId())) ;
    }

}
