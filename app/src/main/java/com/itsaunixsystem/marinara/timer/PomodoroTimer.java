package com.itsaunixsystem.marinara.timer;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.itsaunixsystem.marinara.TimerActivity;
import com.itsaunixsystem.marinara.util.AndroidHelper;
import com.itsaunixsystem.marinara.util.MarinaraPreferences;

import java.util.ArrayList;

/**
 * @author ajdt on 6/22/16.
 * @description A wrapper for CountDownTimer that allows us to pause the timer
 */
public class PomodoroTimer extends Service {


    private ArrayList<TimerCallback>    _callbacks                  = null;
    private CountDownTimer              _countdown_timer            = null;
    private long                        _remaining_millisec, _duration ;
    private TimerState                  _state ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new TimerBinder(this) ;
    }

    @Override
    public void onCreate() {
        // initialize member vars. CountDownTimer() will be initialized on call to reset(millisec)
        _callbacks = new ArrayList<TimerCallback>() ;
        _state = TimerState.DONE ;
        _remaining_millisec = _duration = 0 ;
    }

    /****************************** CountDownTimer CREATION/DESTRUCTION ******************************/

    /**
     * create a new CoundDownTimer object with given duration and set remaining time
     * @param duration_millisec duration for CountDownTimer that will be created
     */
    private void initNewCountDownTimer(long duration_millisec) {
        // set time remaining, create new countdown object and set state to READY
        _remaining_millisec = duration_millisec ;
        long callback_interval = MarinaraPreferences.getPrefs(this)._TIMER_CALLBACK_INTERVAL_DEFAULT ;
        _countdown_timer    = new CountDownTimer(duration_millisec, callback_interval)  {
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

    public void registerCallback(TimerCallback callback_obj ) {
        if (_callbacks.contains(callback_obj))
            return ; // allow registering only once
        _callbacks.add(callback_obj) ;
    }

    public void unregisterCallback(TimerCallback callback_obj) {
        if (_callbacks.contains(callback_obj))
            _callbacks.remove(callback_obj) ;
    }
    /**
     * update remaining millisec and execute our callback's onTimerTick()
     * @param millisec_remaining
     */
    public void onTick(long millisec_remaining) {
        _remaining_millisec = millisec_remaining ;

        for (TimerCallback callback : _callbacks)
            callback.onTimerTick(millisec_remaining) ;
    }

    /**
     * when timer is finished, set remaining millisec to zero and issue callback
     * NOTE: PomodoroTimer::onTick() will NOT get called from here.
     */
    public void onFinish() {
        _remaining_millisec = 0 ;
        _state              = TimerState.DONE ;

        // issue notification if screen doesn't have focus. Otherwise just play sound/vibrate
        // TODO: improve way we check if app is visible on-screen
        if (_callbacks.size() == 0)
            AndroidHelper.issueNotification(this, TimerActivity.class,
                    "Session Ended", "Would you like to continue?") ;
        else
            AndroidHelper.playNotificationSound(this) ;

        for (TimerCallback callback: _callbacks)
            callback.onTimerFinish() ;


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

    public void reset(long millisec) {
        _duration = millisec ;
        this.deleteCountDownTimer() ;
        this.initNewCountDownTimer(millisec) ;
        _state = TimerState.READY ;
    }

    /****************************** SETTERS/GETTERS ******************************/

    public TimerState state() { return _state ; }
    public long getDuration() { return _duration ; }
    public long getRemainingMillisec() { return _remaining_millisec ; }

}
