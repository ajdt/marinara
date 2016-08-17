package com.itsaunixsystem.marinara;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itsaunixsystem.marinara.timer.PomodoroTimer;
import com.itsaunixsystem.marinara.timer.TimerCallback;
import com.itsaunixsystem.marinara.timer.TimerState;

import static com.itsaunixsystem.marinara.util.TimeConversionHelper.millisecToTimeString;

/**
 * @author: ajdt on 8/14/16.
 * @description: abstract class implementing timer functionality used
 * by both BreakActivity and TimerActivity
 */
public abstract class BaseTimerActivity extends AppCompatActivity implements TimerCallback {

    private PomodoroTimer _timer = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NOTE: we're using the layout for TimerActivity because I didn't want
        // to create a new one, and android doesn't have any sort of 'layout inheritance'
        setContentView(R.layout.activity_timer) ;

        this.initNewTimerAndUpdateDisplay() ;
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
                if (!this.allowPause())
                    return ;
                _timer.pause() ;
                break ;
            case PAUSED:
                _timer.resume() ;
                break ;
            case DONE:
                initNewTimerAndUpdateDisplay();
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

    /****************************** UI UPDATING ******************************/

    private void updateTimerDisplay(long millisec_remaining) {
        updateTimerCountdown(millisec_remaining) ;
        updateTimerButtonImage() ;
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
                timer_iv.setImageResource(R.drawable.ready_600_200) ;
                break ;
            case RUNNING:
                timer_iv.setImageResource(R.drawable.running_600_200) ;
                break ;
            case PAUSED:
                timer_iv.setImageResource(R.drawable.paused_600_200) ;
                break ;
            case DONE:
                timer_iv.setImageResource(R.drawable.done_600_200) ;
                break ;
        }
    }

    // TODO: below method is protected b/c TimerActivity uses it to update the timer display when
    // The pomodoro session duration changes (see TimerActivity.onSharedPreferenceChanged()
    // Can we do something better than exposing the method to the entire package?
    /**
     * call getTimerDuration() to get the latest duration value (preferences may have changed),
     * reset the timer with this (possibly) new duration and update the display to show the new duration
     */
    protected void initNewTimerAndUpdateDisplay() {
        // new timer
        long duration   = this.getTimerDuration() ;
        _timer          = new PomodoroTimer(this, duration, this.getTimerCallbackInterval()) ;

        // display
        updateTimerDisplay(duration) ;

        // check if timer is to be initialized in running state and start it if so
        if (this.initialState() == TimerState.RUNNING)
            _timer.start() ;
    }

    /****************************** HELPERS ******************************/

    public long getCurrentSessionDuration() {
        return _timer.duration() ;
    }

    /****************************** SUBCLASSES MUST OVERRIDE THESE TO CHANGE BEHAVIOR ******************************/

    // TODO: remove unnecessary abstract methods (getTimerCallbackInterval() ?)
    public abstract long getTimerDuration() ;
    public abstract long getTimerCallbackInterval() ;
    public abstract boolean skipBreaks() ;
    public abstract boolean allowPause() ;
    public abstract TimerState initialState() ;

}
