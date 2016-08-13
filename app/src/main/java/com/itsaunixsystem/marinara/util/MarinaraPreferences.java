package com.itsaunixsystem.marinara.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.itsaunixsystem.marinara.R;

/**
 * @author: ajdt on 7/1/16.
 * @description: a singleton class used to maintain and update preferences. It's recommended external
 * code refrain from 'saving' any preferences managed by this class and use getter methods below
 * whenever in all contexts a value is needed.
 */
public class MarinaraPreferences {

    // keys to store/access from SharedPreferences
    public final String _POMODORO_MILLISEC_PREF_KEY ;
    public final String _BREAK_MILLISEC_PREF_KEY ;
    public final String _SKIP_BREAK_PREF_KEY ;
    public final String _AUTO_START_BREAK_PREF_KEY ;
    public final String _ALLOW_PAUSE_SESSIONS_PREF_KEY ;
    public final String _SELECTED_TASK_ID_KEY ;

    // constants to reference default values
    public final boolean _SKIP_BREAK_DEFAULT ;
    public final boolean _AUTO_START_BREAK_DEFAULT ;
    public final boolean _ALLOW_PAUSE_SESSIONS_DEFAULT ;
    public final int _TIMER_CALLBACK_INTERVAL_DEFAULT;
    public final int _SESSION_DURATION_MILLISEC_DEFAULT ;
    public final int _BREAK_DURATION_MILLISEC_DEFAULT ;
    public final long _SELECTED_TASK_ID_DEFAULT = -1 ; // flag, SQLiteDatabase will use non-negative ids

    // class is just a layer of indirection to using sharedPreferences, so it
    // requires a handle to default shared preferences
    private SharedPreferences _shared_prefs ;

    // preferences singleton
    private static MarinaraPreferences prefs_singleton = null ;

    public static MarinaraPreferences getPrefs(Context ctx) {
        if (prefs_singleton == null) {
            prefs_singleton = new MarinaraPreferences(ctx) ;
        }

        return prefs_singleton;
    }

    /**
     * constructor is private to enforce this class as a singleton
     * @param ctx android.Content.Context instance used to read strings and constants from xml resources
     */
    private MarinaraPreferences(Context ctx) {

        // get keys and default values from xml resources
        Resources resources             = ctx.getResources() ;
        _POMODORO_MILLISEC_PREF_KEY     = resources.getString(R.string.pomodoro_session_millisec) ;
        _BREAK_MILLISEC_PREF_KEY        = resources.getString(R.string.break_session_millisec) ;
        _SKIP_BREAK_PREF_KEY            = resources.getString(R.string.skip_break) ;
        _AUTO_START_BREAK_PREF_KEY      = resources.getString(R.string.auto_start_break) ;
        _ALLOW_PAUSE_SESSIONS_PREF_KEY  = resources.getString(R.string.allow_pause_session) ;
        _SELECTED_TASK_ID_KEY           = resources.getString(R.string.selected_task_id) ;

        _SKIP_BREAK_DEFAULT             = resources.getBoolean(R.bool.default_skip_break) ;
        _AUTO_START_BREAK_DEFAULT       = resources.getBoolean(R.bool.default_auto_start_break) ;
        _ALLOW_PAUSE_SESSIONS_DEFAULT   = resources.getBoolean(R.bool.default_allow_pause_sessions) ;

        _SESSION_DURATION_MILLISEC_DEFAULT = resources.getInteger(R.integer.default_timer_millisec) ;
        _BREAK_DURATION_MILLISEC_DEFAULT   = resources.getInteger(R.integer.default_break_millisec) ;
        _TIMER_CALLBACK_INTERVAL_DEFAULT   = resources.getInteger(R.integer.timer_callback_interval) ;

        _shared_prefs                   = PreferenceManager.getDefaultSharedPreferences(ctx) ;

    }

    /****************************** PREFERENCE GETTERS ******************************/

    public long timerMillisec() {
        return _shared_prefs.getLong(_POMODORO_MILLISEC_PREF_KEY, _SESSION_DURATION_MILLISEC_DEFAULT) ;
    }
    public long breakMillisec() {
        return _shared_prefs.getLong(_BREAK_MILLISEC_PREF_KEY, _BREAK_DURATION_MILLISEC_DEFAULT) ;
    }
    public boolean skipBreak() {
        return _shared_prefs.getBoolean(_SKIP_BREAK_PREF_KEY, _SKIP_BREAK_DEFAULT) ;
    }
    public boolean autoStartBreak() {
        return _shared_prefs.getBoolean(_AUTO_START_BREAK_PREF_KEY, _AUTO_START_BREAK_DEFAULT) ;
    }
    public boolean allowPauseSessions() {
        return _shared_prefs.getBoolean(_ALLOW_PAUSE_SESSIONS_PREF_KEY, _ALLOW_PAUSE_SESSIONS_DEFAULT) ;
    }
    public long selectedTaskId() {
        return _shared_prefs.getLong(_SELECTED_TASK_ID_KEY, _SELECTED_TASK_ID_DEFAULT) ;
    }

    /****************************** PREFERENCE SETTERS ******************************/

    public void setSelectedTaskId(Long new_id) {
        SharedPreferences.Editor editor = _shared_prefs.edit() ;
        editor.putLong(_SELECTED_TASK_ID_KEY, new_id) ;
        editor.commit() ;
    }

}
