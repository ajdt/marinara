package com.itsaunixsystem.marinara;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author: ajdt on 7/1/16.
 * @description: a singleton class used to maintain and update preferences
 */
public class MarinaraPreferences implements SharedPreferences.OnSharedPreferenceChangeListener {


    private long _timer_millisec ;
    private boolean _skip_break, _auto_start_break ;

    public final String _POMODORO_MILLISEC_PREF_KEY ;
    public final String _SKIP_BREAK_PREF_KEY ;
    public final String _AUTO_START_BREAK_PREF_KEY ;
    public final boolean _SKIP_BREAK_DEFAULT ;
    public final boolean _AUTO_START_BREAK_DEFAULT ;
    public final int _TIMER_CALLBACK_INTERVAL ;


    private static MarinaraPreferences prefs = null ;

    public MarinaraPreferences getPrefs(Context ctx) {
        if (prefs == null) {
            prefs = new MarinaraPreferences(ctx) ;
        }

        return prefs ;
    }

    /**
     * constructor is private to enforce this class as a singleton
     * @param ctx
     */
    private MarinaraPreferences(Context ctx) {

        // get keys and default values from xml resources
        _POMODORO_MILLISEC_PREF_KEY    = ctx.getResources().getString(R.string.pomodoro_session_millisec) ;
        _SKIP_BREAK_PREF_KEY           = ctx.getResources().getString(R.string.skip_break) ;
        _AUTO_START_BREAK_PREF_KEY     = ctx.getResources().getString(R.string.auto_start_break) ;
        _SKIP_BREAK_DEFAULT            = ctx.getResources().getBoolean(R.bool.default_skip_break) ;
        _AUTO_START_BREAK_DEFAULT      = ctx.getResources().getBoolean(R.bool.default_auto_start_break) ;
        _TIMER_CALLBACK_INTERVAL       = ctx.getResources().getInteger(R.integer.timer_callback_interval) ;

        // set initial value of preference vars from SharedPreferences
        SharedPreferences shared_prefs = PreferenceManager.getDefaultSharedPreferences(ctx) ;
        _timer_millisec                = shared_prefs.getLong(_POMODORO_MILLISEC_PREF_KEY, R.integer.default_timer_millisec) ;
        _auto_start_break              = shared_prefs.getBoolean(_AUTO_START_BREAK_PREF_KEY, _AUTO_START_BREAK_DEFAULT) ;
        _skip_break                    = shared_prefs.getBoolean(_SKIP_BREAK_PREF_KEY, _SKIP_BREAK_DEFAULT) ;

        shared_prefs.registerOnSharedPreferenceChangeListener(this) ;
    }

    /**
     * listener called when shared preferences change
     * @param shared_prefs
     * @param key
     */
    public void onSharedPreferenceChanged(SharedPreferences shared_prefs, String key) {
        if (key.equals(_POMODORO_MILLISEC_PREF_KEY))
            _timer_millisec = shared_prefs.getLong(_POMODORO_MILLISEC_PREF_KEY, R.integer.default_timer_millisec) ;
        else if (key.equals(_AUTO_START_BREAK_PREF_KEY))
            _auto_start_break = shared_prefs.getBoolean(_AUTO_START_BREAK_PREF_KEY, _AUTO_START_BREAK_DEFAULT) ;
        else if (key.equals(_SKIP_BREAK_PREF_KEY))
            _skip_break = shared_prefs.getBoolean(_SKIP_BREAK_PREF_KEY, _SKIP_BREAK_DEFAULT) ;
        else
            return ;
    }

    /****************************** PREFERENCE GETTERS ******************************/

    public long timerMillisec() { return _timer_millisec ; }
    public boolean skipBreak() { return _skip_break ; }
    public boolean autoStartBreak() { return _auto_start_break ; }


}
