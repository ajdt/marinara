package com.itsaunixsystem.marinara.mock;

import com.itsaunixsystem.marinara.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: ajdt on 8/23/16.
 * @description: A class used as a mock object for PomodoroSession. Stats are generated
 * from PomodoroSession objects only, so this mock object saves a task name instead of
 * a reference to a Task object (like PomodoroSession does).
 *
 * NOTE: this class was created because I didn't want to introduce a mocking framework
 * and I didn't want to use PomodoroSession objects as mock objects to avoid accidentally
 * saving to the database.
 */
public class MockPomodoroSession implements Session {

    private String _task_name;
    private long _duration ;
    private Date _completion_date ;


    public MockPomodoroSession(String task_name, long duration, Date completion_date) {
        _task_name = task_name ;
        _duration           = duration ;
        _completion_date    = completion_date ;
    }

    public MockPomodoroSession(String task_name, long duration, String completion_date) {
        this(task_name, duration, DateUtil.parseDateString(completion_date)) ;
    }

    /****************************** GETTERS ******************************/
    public String taskName() { return _task_name; }
    public long duration() { return _duration ; }
    public Date completionDate() { return _completion_date ; }




}
