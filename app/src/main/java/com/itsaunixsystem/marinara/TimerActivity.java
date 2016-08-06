package com.itsaunixsystem.marinara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;


public class TimerActivity extends AppCompatActivity
        implements TimerCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    private PomodoroTimer   _timer = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer) ;

        // TimerActivity uses callback to update countdown when duration preference changes
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(this) ;

        this.initTimer() ;

        // initialize database
        FlowManager.init(this) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer, menu) ;
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId() ;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true ;
        }

        return super.onOptionsItemSelected(item) ;
    }

    /****************************** TIMER AND UI CALLBACKS ******************************/

    /**
     * callback for "Settings" option in options menu. Launches SettingsActivity
     * @param item
     */
    public void onSettingsMenuClicked(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class) ;
        startActivity(intent) ;
    }

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
     * callback updates UI and changes timer_state to done
     */
    public void onTimerFinish() {
        this.updateTimerDisplay(0) ;

        // break time?
        if (!this.skipBreaks()) {
            launchBreak() ;
        }
    }

    /**
     * callback implemented to listen for changes to session duration preference. That
     * way we can update timer countdown display as soon as duration preference is changed.
     *
     * @param prefs
     * @param key
     */
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if ( key.equals(getResources().getString(R.string.pomodoro_session_millisec)) ) {
            this.resetTimerAndUpdateDisplay() ;
        }
    }

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
        timer_tv.setText(this.millisecToTimeString(millisec_remaining));
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

    /**
     * call getTimerDuration() to get the latest duration value (preferences may have changed),
     * reset the timer with this (possibly) new duration and update the display to show the new duration
     */
    private void resetTimerAndUpdateDisplay() {
        long new_duration = this.getTimerDuration() ;
        _timer.setDuration(new_duration) ;
        _timer.reset() ;
        updateTimerDisplay(new_duration) ;
    }

    /****************************** HELPERS ******************************/

    private void launchBreak() {
        Intent intent = new Intent(this, BreakActivity.class) ;
        this.startActivity(intent) ;
    }
    /**
     *
     * @param millisec
     * @return the time-value as a string of the form "<minutes>:<seconds>"
     * NOTE: My requirements are simple, so I opted out of using DateFormat or related classes
     */
    private String millisecToTimeString(long millisec) {
        long seconds = (millisec / 1000) % 60 ;
        long minutes = (millisec / (1000 * 60)) % 60 ;

        return String.format("%02d:%02d", minutes, seconds) ;
    }

    /**
     * Should only be called from onCreate(). Sets initial timer state and instantiates timer.
     */
    public void initTimer() {
        MarinaraPreferences prefs = MarinaraPreferences.getPrefs(this) ;
        _timer          = new PomodoroTimer(this, this.getTimerDuration(), this.getTimerCallbackInterval()) ;

        // NOTE: if display not updated here, timer will appear to countdown one second less than
        // desired duration
        this.updateTimerDisplay(this.getTimerDuration()) ;

        // TODO: refactor initialState to a boolean that asks whether timer should autorun??
        // timer is to be initialized in running state state
        if (this.initialState() == TimerState.RUNNING)
            _timer.start() ;
    }

    /****************************** SUBCLASSES MUST OVERRIDE THESE TO CHANGE BEHAVIOR ******************************/
    // NOTE: MarinaraPreferences is called every time a preference is needed. This guarantees
    // we are always using the latest value, and saves the trouble of having to save local copies
    // of the preference values (and implementing onSharedPreferenceChangeListener in this class too).

    public long getTimerDuration() { return MarinaraPreferences.getPrefs(this).timerMillisec() ;}
    public long getTimerCallbackInterval() {
        return MarinaraPreferences.getPrefs(this)._TIMER_CALLBACK_INTERVAL_DEFAULT;
    }
    public boolean skipBreaks() { return MarinaraPreferences.getPrefs(this).skipBreak() ; }
    public boolean allowPause() { return MarinaraPreferences.getPrefs(this).allowPauseSessions() ; }
    public TimerState initialState() { return TimerState.READY ; }

}
