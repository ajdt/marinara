package com.itsaunixsystem.marinara;

import android.os.CountDownTimer;

/**
 * @author ajdt on 6/22/16.
 * @description A wrapper for CountDownTimer that allows us to pause/start/reset the timer
 */
public class PomodoroTimer {


    private TimerCallback       _callback_obj   = null;
    private CountDownTimer      _timer          = null;

    private final long      _DURATION_MILLISEC, _INTERVAL_MILLISEC ;
    private long            _remaining_millisec ;



    public PomodoroTimer(TimerCallback callback, long duration_millisec, long interval_millisec) {
        _DURATION_MILLISEC = duration_millisec ;
        _INTERVAL_MILLISEC = interval_millisec ;

        _callback_obj = callback ;

        this.initNewTimer(_DURATION_MILLISEC);
    }

    /****************************** TIMER CREATION/DESTRUCTION ******************************/

    /**
     * create a new CoundDownTimer object with given duration and set remaining time
     * @param duration millisec duration of timer
     */
    private void initNewTimer(long duration) {
        _remaining_millisec = duration ;

        _timer = new CountDownTimer(duration, _INTERVAL_MILLISEC) {
            @Override
            public void onTick(long millis_until_finished) { this.onTick(millis_until_finished) ; }

            @Override
            public void onFinish() { this.onFinish() ; }
        } ;

    }

    /**
     * cancel the current timer and release our reference to it
     */
    private void deleteTimer() {
        if (_timer == null)
            return ;
        _timer.cancel() ;
        _timer = null ;
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
        _callback_obj.onTimerFinish() ;
    }

    /****************************** TIME KEEPING METHODS ******************************/

    public void start() {
        _timer.start() ;
    }

    /**
     * Pausing is accomplished by cancelling current timer, and initializing new one with
     * '_remaining_millisec' time on the clock. New timer isn't started.
     */
    public void pause() {
        this.deleteTimer() ;
        this.initNewTimer(_remaining_millisec);
    }

    public void resume() {
        this.start() ;
    }

    /**
     * start a new timer with base duration of this instance (_DURATION_MILLISEC)
     */
    public void reset() {
        this.deleteTimer() ;
        this.initNewTimer(_DURATION_MILLISEC) ;
    }

}
