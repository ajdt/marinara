package com.itsaunixsystem.marinara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class TimerActivity extends AppCompatActivity implements TimerCallback {

    private PomodoroTimer   _timer = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer) ;

        this.initTimer() ;
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
        // update internal state and timer
        // TODO: move state to inside of timer. Makes more sense than keeping it here.
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
                // set duration in case it's changed since last session
                // TODO: move this to READY case so that timer can be changed any time before starting it again
                _timer.setDuration(this.getTimerDuration()) ;
                _timer.reset() ;
                break ;

        }

        // update timer image view to reflect changes in state
        this.updateTimerImage() ;
    }

    /**
     * callback that runs every timer tick.
     * @param millisec_remaining
     */
    public void onTimerTick(long millisec_remaining) {
        this.updateTimerCountdownDisplay(millisec_remaining);
    }

    /**
     * callback updates UI and changes timer_state to done
     */
    public void onTimerFinish() {
        // update internal state and UI
        this.updateTimerImage() ;
        this.updateTimerCountdownDisplay(0) ;

        // break time?
        if (!this.skipBreaks()) {
            launchBreak() ;
        }
    }




    /****************************** UI UPDATING ******************************/

    /**
     * update UI to display number of millisec remaining
     * @param millisec_remaining
     */
    private void updateTimerCountdownDisplay(long millisec_remaining) {

        // update time remaining display
        TextView timer_tv = (TextView)findViewById(R.id.timer_tv) ;
        timer_tv.setText(this.millisecToTimeString(millisec_remaining));

    }


    /**
     * update the ImageView image depending on the state of the timer
     */
    private void updateTimerImage() {
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

        // NOTE: if UI not initialized here, timer will appear to countdown one second less than
        // desired duration
        this.initTimerUI() ;

        // TODO: refactor initialState to a boolean that asks whether timer should autorun??
        // timer is to be initialized in running state state
        if (this.initialState() == TimerState.RUNNING)
            _timer.start() ;
    }

    /**
     * when a new timer is initialized we need to initialize the counter and image being used too
     */
    public void initTimerUI() {
        this.updateTimerImage() ;
        this.updateTimerCountdownDisplay(this.getTimerDuration()) ;
    }


    /****************************** SUBCLASSES MUST OVERRIDE THESE TO CHANGE BEHAVIOR ******************************/

    public long getTimerDuration() { return MarinaraPreferences.getPrefs(this).timerMillisec() ;}
    public long getTimerCallbackInterval() {
        return MarinaraPreferences.getPrefs(this)._TIMER_CALLBACK_INTERVAL_DEFAULT;
    }
    public boolean skipBreaks() { return MarinaraPreferences.getPrefs(this).skipBreak() ; }
    public boolean allowPause() { return MarinaraPreferences.getPrefs(this).allowPauseSessions() ; }
    public TimerState initialState() { return TimerState.READY ; }

}
