package com.itsaunixsystem.marinara;

import android.os.CountDownTimer;

/**
 * @author ajdt on 6/22/16.
 * @description A wrapper for CountDownTimer that allows us to pause/start/reset the timer
 */
public class PomodoroTimer {

    private TimerCallback _callback_obj ;
    private CountDownTimer _timer ;

    private final long _DURATION_MILLISEC, _INTERVAL_MILLISEC ;
    private long _remaining_millisec ;

    public PomodoroTimer(TimerCallback callback, long duration_millisec, long interval_millisec) {
        _DURATION_MILLISEC = duration_millisec ;
        _INTERVAL_MILLISEC = interval_millisec ;

        _callback_obj = callback ;

        // TODO: initialize new timer

    }

    /****************************** TIMER CREATION/DESTRUCTION ******************************/

    public void initNewTimer(long duration) {
        _remaining_millisec = duration ;


    }

    private void deleteTimer() {

    }

    /****************************** CALLBACKS ******************************/

    public void onTick(long millisec_remaining) {

    }

    public void onFinish() {

    }

    /****************************** TIME KEEPING METHODS ******************************/

    public void start() {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void reset() {

    }




}
