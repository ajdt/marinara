package com.itsaunixsystem.marinara.timer;

import android.os.Binder;

/**
 * @author: ajdt on 9/10/16.
 * @description: A Binder subclass providing access to PomodoroTimer service
 */
public class TimerBinder extends Binder {

    private PomodoroTimer _timer_service ;

    public TimerBinder(PomodoroTimer service) { _timer_service = service ; }
    public PomodoroTimer getService() { return _timer_service ; }
}
