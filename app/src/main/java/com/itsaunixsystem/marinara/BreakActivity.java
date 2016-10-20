package com.itsaunixsystem.marinara;

import android.os.Bundle;

import com.itsaunixsystem.marinara.timer.TimerState;
import com.itsaunixsystem.marinara.util.MarinaraPreferences;

public class BreakActivity extends BaseTimerActivity {

    // NOTE: BaseTimerActivity.onCreate() is sufficient. Not overridden

    public final static String __RUN_LONG_BREAK = "long break" ;

    @Override
    public void onTimerFinish() {
        super.onTimerFinish() ;
        this.finish() ;

    }

    @Override
    public long timerDurationPreference() {
        Bundle extras               = getIntent().getExtras() ;
        MarinaraPreferences prefs   = MarinaraPreferences.getPrefs(this) ;

        // if long break requested, return long break duration preference...
        if (extras != null && extras.getBoolean(__RUN_LONG_BREAK))
            return prefs.longBreakMillisec() ;

        // ...otherwise return duration of regular break
        return prefs.breakMillisec() ;
    }

    // NOTE: App should not allow breaks to be paused. After break ends,
    // next pomodoro session won't start automatically anyway, so there's
    // no reason to pause.
    @Override
    public boolean allowPausePreference() { return false ; }

    /**
     * Dialog occuring before BreakActivity is started will ask user if break should be launched,
     * so timer can just start running.
     * @return an initial state of RUNNING
     */
    @Override
    public TimerState initialState() { return TimerState.RUNNING ; }

}
