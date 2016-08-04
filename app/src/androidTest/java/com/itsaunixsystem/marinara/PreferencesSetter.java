package com.itsaunixsystem.marinara;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * @author: ajdt on 7/31/16.
 * @description: used by instrumentation tests to set preferences as desired
 */
public class PreferencesSetter {

    // preference keys
    private String _POMODORO_MILLISEC_PREF_KEY ;
    private String _BREAK_MILLISEC_PREF_KEY ;
    private String _SKIP_BREAK_PREF_KEY ;
    private String _AUTO_START_BREAK_PREF_KEY ;
    private String _ALLOW_PAUSE_SESSIONS_PREF_KEY ;

    // preference manager
    private SharedPreferences shared_prefs ;
    private SharedPreferences.Editor prefs_editor ;

    public PreferencesSetter(Context ctx) {

        Resources resources             = ctx.getResources() ;
        // initialize preference keys
        // TODO: use R.string instead!!!
        _POMODORO_MILLISEC_PREF_KEY     = "pomodoro_session_millisec" ;
        _BREAK_MILLISEC_PREF_KEY        = "break_session_millisec" ;
        _SKIP_BREAK_PREF_KEY            = "skip_break" ;
        _AUTO_START_BREAK_PREF_KEY      = "auto_start_break" ;
        _ALLOW_PAUSE_SESSIONS_PREF_KEY  = "allow_pause_session" ;

        shared_prefs = PreferenceManager.getDefaultSharedPreferences(ctx) ;
        prefs_editor = shared_prefs.edit() ;
    }

    public void setTimerMillisec(long millisec) {
        prefs_editor.putLong(_POMODORO_MILLISEC_PREF_KEY, millisec) ;
        prefs_editor.commit() ;
    }

    public void setBreakMillisec(long millisec) {
        prefs_editor.putLong(_BREAK_MILLISEC_PREF_KEY, millisec) ;
        prefs_editor.commit() ;
    }

    public void setSkipBreak(boolean skip_break) {
        prefs_editor.putBoolean(_SKIP_BREAK_PREF_KEY, skip_break) ;
        prefs_editor.commit() ;
    }

    public void setAutoStartBreak(boolean auto_start_break) {
        prefs_editor.putBoolean(_AUTO_START_BREAK_PREF_KEY, auto_start_break) ;
        prefs_editor.commit() ;
    }

    public void setAllowPauseSessions(boolean allow_pause_sessions) {
        prefs_editor.putBoolean(_ALLOW_PAUSE_SESSIONS_PREF_KEY, allow_pause_sessions) ;
        prefs_editor.commit() ;
    }
}

// TODO: DRY out this code (refactor too?)
