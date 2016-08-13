package com.itsaunixsystem.marinara.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.itsaunixsystem.marinara.R;

// TODO: code is this file is too 'wordy'. Try simplifying before adding statistics settings. Maybe extract Preference() ADT?

/**
 * @author: ajdt on 7/1/16.
 * @description: a singleton class used to maintain and update preferences. It's recommended external
 * code refrain from 'saving' any preferences managed by this class and use getter methods below
 * whenever in all contexts a value is needed.
 */
public class MarinaraPreferences implements SharedPreferences.OnSharedPreferenceChangeListener {

    // preference member variables
    private long _timer_millisec ;
    private long _break_millisec ;
    private boolean _skip_break, _auto_start_break, _allow_pause_sessions ;
    private long _selected_task_id ;

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
    public final long _SELECTED_TASK_ID_DEFAULT = -1 ; // XXX: TODO: make sure this flag value is ok to use



    private static MarinaraPreferences prefs = null ;

    public static MarinaraPreferences getPrefs(Context ctx) {
        if (prefs == null) {
            prefs = new MarinaraPreferences(ctx) ;
        }

        return prefs ;
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


        // set initial value of preference vars from SharedPreferences
        SharedPreferences shared_prefs = PreferenceManager.getDefaultSharedPreferences(ctx) ;
        _timer_millisec                = shared_prefs.getLong(_POMODORO_MILLISEC_PREF_KEY, _SESSION_DURATION_MILLISEC_DEFAULT) ;
        _break_millisec                = shared_prefs.getLong(_BREAK_MILLISEC_PREF_KEY, _BREAK_DURATION_MILLISEC_DEFAULT) ;
        _skip_break                    = shared_prefs.getBoolean(_SKIP_BREAK_PREF_KEY, _SKIP_BREAK_DEFAULT) ;
        _auto_start_break              = shared_prefs.getBoolean(_AUTO_START_BREAK_PREF_KEY, _AUTO_START_BREAK_DEFAULT) ;
        _allow_pause_sessions          = shared_prefs.getBoolean(_ALLOW_PAUSE_SESSIONS_PREF_KEY, _ALLOW_PAUSE_SESSIONS_DEFAULT) ;

        // register singleton as listener so that preferences are updated
        shared_prefs.registerOnSharedPreferenceChangeListener(this) ;
    }

    /**
     * listener called when shared preferences change
     * @param shared_prefs
     * @param key
     */
    public void onSharedPreferenceChanged(SharedPreferences shared_prefs, String key) {
        if (key.equals(_POMODORO_MILLISEC_PREF_KEY))
            _timer_millisec = shared_prefs.getLong(_POMODORO_MILLISEC_PREF_KEY, _SESSION_DURATION_MILLISEC_DEFAULT) ;
        else if (key.equals(_BREAK_MILLISEC_PREF_KEY))
            _break_millisec = shared_prefs.getLong(_BREAK_MILLISEC_PREF_KEY, _BREAK_DURATION_MILLISEC_DEFAULT) ;
        else if (key.equals(_AUTO_START_BREAK_PREF_KEY))
            _auto_start_break = shared_prefs.getBoolean(_AUTO_START_BREAK_PREF_KEY, _AUTO_START_BREAK_DEFAULT) ;
        else if (key.equals(_SKIP_BREAK_PREF_KEY))
            _skip_break = shared_prefs.getBoolean(_SKIP_BREAK_PREF_KEY, _SKIP_BREAK_DEFAULT) ;
        else if (key.equals(_ALLOW_PAUSE_SESSIONS_PREF_KEY))
            _allow_pause_sessions = shared_prefs.getBoolean(_ALLOW_PAUSE_SESSIONS_PREF_KEY, _ALLOW_PAUSE_SESSIONS_DEFAULT) ;
        else if (key.equals(_SELECTED_TASK_ID_KEY))
            _selected_task_id = shared_prefs.getLong(_SELECTED_TASK_ID_KEY, _SELECTED_TASK_ID_DEFAULT) ;

        else
            return ;
    }

    /****************************** PREFERENCE GETTERS ******************************/

    public long timerMillisec() { return _timer_millisec ; }
    public long breakMillisec() { return _break_millisec ; }
    public boolean skipBreak() { return _skip_break ; }
    public boolean autoStartBreak() { return _auto_start_break ; }
    public boolean allowPauseSessions() { return _allow_pause_sessions ; }
    public long selectedTaskId() { return _selected_task_id ; }

    public void setSelectedTaskId(Context ctx, Long new_id) {
        SharedPreferences shared_prefs = PreferenceManager.getDefaultSharedPreferences(ctx) ;
        SharedPreferences.Editor editor = shared_prefs.edit() ;
        editor.putLong(_SELECTED_TASK_ID_KEY, new_id) ;
        editor.commit() ;
    }



}
