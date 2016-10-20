package com.itsaunixsystem.marinara;

import android.os.Bundle;

import com.itsaunixsystem.marinara.timer.TimerState;
import com.itsaunixsystem.marinara.util.MarinaraPreferences;

public class BreakActivity extends BaseTimerActivity {

    // NOTE: BaseTimerActivity.onCreate() is sufficient. Not overridden

    @Override
    public void onTimerFinish() {
        super.onTimerFinish() ;
        this.finish() ;

    }

    @Override
    public long timerDurationPreference() { return MarinaraPreferences.getPrefs(this).breakMillisec() ;}

    // NOTE: this is a break, don't need another
    @Override
    public boolean skipBreaksPreference() { return true ; }

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
