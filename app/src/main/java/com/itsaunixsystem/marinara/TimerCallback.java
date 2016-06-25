package com.itsaunixsystem.marinara;

/**
 * @author ajdt on 6/22/16.
 * @description required to use PomodoroTimer. Implements callbacks for single time tick and when
 * timer is finished.
 */
public interface TimerCallback {

    void onTimerTick(long millisec_remaining) ;
    void onTimerFinish() ;
}
