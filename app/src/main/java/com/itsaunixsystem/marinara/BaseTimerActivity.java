package com.itsaunixsystem.marinara;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itsaunixsystem.marinara.timer.PomodoroTimer;
import com.itsaunixsystem.marinara.timer.TimerBinder;
import com.itsaunixsystem.marinara.timer.TimerCallback;
import com.itsaunixsystem.marinara.timer.TimerState;


import static com.itsaunixsystem.marinara.util.TimeConversionHelper.millisecToTimeString;

/**
 * @author: ajdt on 8/14/16.
 * @description: abstract class implementing timer functionality used
 * by both BreakActivity and TimerActivity
 */
public abstract class BaseTimerActivity extends AppCompatActivity
        implements TimerCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    private PomodoroTimer _timer = null;
    private ServiceConnection _connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            // save reference to timer service
            BaseTimerActivity.this._timer = ((TimerBinder) service).getService() ;

            // register callback, reset timer and start it if necessary
            BaseTimerActivity.this._timer.registerCallback(BaseTimerActivity.this) ;
            BaseTimerActivity.this.resetTimerAndUpdateDisplay() ;
            if (BaseTimerActivity.this.initialState() == TimerState.RUNNING)
                BaseTimerActivity.this._timer.start() ;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            BaseTimerActivity.this._timer = null ;
            // TODO: safeguard against using _timer when it equals null
        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NOTE: we're using the layout for TimerActivity because I didn't want
        // to create a new one, and android doesn't have any sort of 'layout inheritance'
        setContentView(R.layout.activity_timer) ;

        // register callback on shared prefs ( want to update timer display on duration pref change)
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(this) ;

        this.bindService(new Intent(this, PomodoroTimer.class), _connection, Context.BIND_AUTO_CREATE) ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy() ;
        this.unbindService(_connection) ;
        this._timer = null ;
    }

    @Override
    protected void onResume() {
        super.onResume() ;
        if (_timer != null) {
            _timer.registerCallback(this);
            // NOTE: this call is needed to update UI after activity regains focus
            this.updateTimerDisplay(_timer.getRemainingMillisec()) ;

        }

    }

    @Override
    protected void onPause() {
        super.onPause() ;
        if (_timer != null) {
            _timer.unregisterCallback(this) ;
        }

    }


    /****************************** TIMER AND UI CALLBACKS ******************************/

    /**
     * handle click event from timer image button used to start, pause, resume & restart
     * @param clicked_view
     */
    public void onTimerButtonClicked(View clicked_view) {
        switch (_timer.state()) {
            case READY:
                _timer.start() ;
                break ;
            case RUNNING:
                if (!this.allowPausePreference())
                    return ;
                _timer.pause() ;
                break ;
            case PAUSED:
                _timer.resume() ;
                break ;
            case DONE:
                resetTimerAndUpdateDisplay();
                break ;
        }

        // update timer image view to reflect changes in state
        this.updateTimerButtonImage() ;
    }

    /**
     * callback that runs every timer tick.
     * @param millisec_remaining
     */
    public void onTimerTick(long millisec_remaining) {
        this.updateTimerCountdown(millisec_remaining);
    }

    /**
     * callback issued when timer is finished running
     */
    public void onTimerFinish() { this.updateTimerDisplay(0) ; }

    /**
     * callback implemented to listen for changes to session duration preference. That
     * way we can update timer countdown display as soon as duration preference is changed.
     *
     * @param prefs
     * @param key
     */
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        // if session duration changes and we haven't started timer yet, update timer & display
        if ( key.equals(getResources().getString(R.string.pomodoro_session_millisec)) &&
                _timer.state() == TimerState.READY) {
            resetTimerAndUpdateDisplay() ;
        }
    }

    /****************************** UI UPDATING ******************************/

    private void updateTimerDisplay(final long millisec_remaining) {
        // explicitly run on UI thread because onTimerTick() and onTimerDisplay()
        // may be called from separate thread in PomodoroTimer service
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTimerCountdown(millisec_remaining) ;
                updateTimerButtonImage() ;
            }
        });

    }
    
    /**
     * update countdown display with remaining time
     * @param millisec_remaining
     */
    private void updateTimerCountdown(long millisec_remaining) {
        TextView timer_tv = (TextView)findViewById(R.id.timer_tv) ;
        timer_tv.setText(millisecToTimeString(millisec_remaining));
    }

    /**
     * update timer button image depending on timer's state
     */
    private void updateTimerButtonImage() {
        ImageView timer_iv = (ImageView)findViewById(R.id.timer_iv) ;

        switch (_timer.state()) {
            case READY:
                timer_iv.setImageResource(R.drawable.play_white_256_256) ;
                break ;
            case RUNNING:
                timer_iv.setImageResource(R.drawable.pause_white_256_256) ;
                break ;
            case PAUSED:
                timer_iv.setImageResource(R.drawable.play_white_256_256) ;
                break ;
            case DONE:
                timer_iv.setImageResource(R.drawable.done_white_256_256) ;
                break ;
        }
    }

    /**
     * call timerDurationPreference() to get the latest duration value (preferences may have changed),
     * reset the timer with this (possibly) new duration and update the display to show the new duration
     */
    private void resetTimerAndUpdateDisplay() {
        // NOTE: important to reset timer first update display or display will be incorrect
        long duration   = this.timerDurationPreference() ;
        _timer.reset(duration);
        updateTimerDisplay(duration) ;
    }

    /****************************** HELPERS ******************************/
    public long getCurrentSessionDuration() { return _timer.getDuration() ; }

    /****************************** SUBCLASSES MUST OVERRIDE THESE TO CHANGE BEHAVIOR ******************************/

    public abstract long timerDurationPreference() ;
    public abstract boolean allowPausePreference() ;
    public abstract TimerState initialState() ;

}
