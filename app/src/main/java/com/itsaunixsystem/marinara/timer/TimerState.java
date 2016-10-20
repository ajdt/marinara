package com.itsaunixsystem.marinara.timer;

/**
 * @author ajdt on 6/25/16.
 */
public enum TimerState {
    /*
    * READY: indicates timer hasn't started a session yet. If session duration is changed, can
    * take effect immediately.
    *
    * PAUSED: state occurs part way through a session if user pauses timer,
    * so changes to timing settings cannot be used right away.
    *
    * STOPPED: state entered only in brief windown when a CountDownTimer that didn't finish is
    * destroyed, but new one hasn't been created. Meant for internal usage only.
    */
    READY, RUNNING, PAUSED, DONE, STOPPED ;
}
