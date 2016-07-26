package com.itsaunixsystem.marinara;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class BreakActivity extends TimerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NOTE: uses same layout as TimerActivity since android doesn't support layout 'inheritance'
        setContentView(R.layout.activity_timer);
        this.initTimer() ;
    }

    @Override
    public void onTimerFinish() {
        super.onTimerFinish() ;
        this.finish() ;

    }

    @Override
    public long getTimerDuration() { return MarinaraPreferences.getPrefs(this).breakMillisec() ;}

    // NOTE: this is a break, don't need another
    @Override
    public boolean skipBreaks() { return false ; }

    // NOTE: App should not allow breaks to be paused. After break ends,
    // next pomodoro session won't start automatically anyway, so there's
    // no reason to pause.
    @Override
    public boolean allowPause() { return false ; }

    /**
     * Dialog occuring before BreakActivity is started will ask user if break should be launched,
     * so timer can just start running.
     * @return an initial state of RUNNING
     */
    @Override
    public TimerState initialState() { return TimerState.RUNNING ; }



}
