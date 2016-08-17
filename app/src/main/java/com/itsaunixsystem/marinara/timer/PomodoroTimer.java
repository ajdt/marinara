package com.itsaunixsystem.marinara.timer;

import android.os.CountDownTimer;

/**
 * @author ajdt on 6/22/16.
 * @description A wrapper for CountDownTimer that allows us to pause the timer
 */
public class PomodoroTimer {


    private TimerCallback _callback_obj             = null;
    private CountDownTimer _countdown_timer         = null;

    private long            _remaining_millisec ;
    private final long      _SESSION_DURATION ;
    private final long      _CALLBACK_INTERVAL_MILLISEC ;

    private TimerState _state ;



    public PomodoroTimer(TimerCallback callback, long duration_millis, long interval_millis) {
        _CALLBACK_INTERVAL_MILLISEC = interval_millis ;
        _SESSION_DURATION           = duration_millis ;
        _callback_obj               = callback ;

        this.initNewCountDownTimer(duration_millis);
    }

    /****************************** CountDownTimer CREATION/DESTRUCTION ******************************/

    /**
     * create a new CoundDownTimer object with given duration and set remaining time
     * @param duration_millisec duration for CountDownTimer that will be created
     */
    private void initNewCountDownTimer(long duration_millisec) {
        // set time remaining, create new countdown object and set state to READY
        _remaining_millisec = duration_millisec ;
        _countdown_timer    = new CountDownTimer(duration_millisec, _CALLBACK_INTERVAL_MILLISEC) {
            @Override
            public void onTick(long millis_until_finished) { PomodoroTimer.this.onTick(millis_until_finished) ; }

            @Override
            public void onFinish() { PomodoroTimer.this.onFinish() ; }
        } ;

        _state = TimerState.READY ;
    }

    /**
     * cancel the current timer and release our reference to it
     */
    private void deleteCountDownTimer() {
        if (_countdown_timer == null)
            return ;
        _countdown_timer.cancel() ;
        _countdown_timer    = null ;
        _state              = TimerState.STOPPED ;
    }

    /****************************** CALLBACKS ******************************/

    /**
     * update remaining millisec and execute our callback's onTimerTick()
     * @param millisec_remaining
     */
    public void onTick(long millisec_remaining) {
        _remaining_millisec = millisec_remaining ;
        _callback_obj.onTimerTick(millisec_remaining) ;
    }

    /**
     * when timer is finished, set remaining millisec to zero and issue callback
     * NOTE: PomodoroTimer::onTick() will NOT get called from here.
     */
    public void onFinish() {
        _remaining_millisec = 0 ;
        _state              = TimerState.DONE ;
        _callback_obj.onTimerFinish() ;
    }

    /****************************** TIME KEEPING METHODS ******************************/

    public void start() {
        _countdown_timer.start() ;
        _state = TimerState.RUNNING ;
    }

    /**
     * Pausing is accomplished by cancelling current timer, and initializing new one with
     * '_remaining_millisec' time on the clock. New timer isn't started.
     */
    public void pause() {
        this.deleteCountDownTimer() ;
        this.initNewCountDownTimer(_remaining_millisec) ;

        _state = TimerState.PAUSED ;
    }

    public void resume() {
        this.start() ;
        _state = TimerState.RUNNING ;
    }

    /****************************** SETTERS/GETTERS ******************************/

    public TimerState state() { return _state ; }
    public long duration() { return _SESSION_DURATION ; }

}
