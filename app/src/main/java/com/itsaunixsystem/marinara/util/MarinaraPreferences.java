package com.itsaunixsystem.marinara.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.itsaunixsystem.marinara.R;
import com.itsaunixsystem.marinara.orm.Task;

/**
 * @author: ajdt on 7/1/16.
 * @description: a singleton class used to maintain and update preferences. It is recommended that external
 * code refrain from 'saving' any preferences managed by this class and use getter methods below
 * whenever in all contexts a value is needed.
 */
public class MarinaraPreferences {

    // preferences singleton
    private static MarinaraPreferences prefs_singleton = null ;


    // class is just a layer of indirection to using sharedPreferences, so it
    // requires a handle to default shared preferences and resource strings
    private Resources _resources = null ;
    private SharedPreferences _shared_prefs ;



    public static MarinaraPreferences getPrefs(Context ctx) {
        if (prefs_singleton == null) {
            prefs_singleton = new MarinaraPreferences(ctx) ;
        }

        return prefs_singleton;
    }

    /**
     * constructor is private to enforce this class as a singleton
     * @param ctx android.Content.Context instance used to access xml resources and PreferenceManager
     */
    private MarinaraPreferences(Context ctx) {
        _resources      = ctx.getResources() ;
        _shared_prefs   = PreferenceManager.getDefaultSharedPreferences(ctx) ;
    }

    /****************************** HELPERS ******************************/
    public boolean getBoolean(int key_id, int value_id) {
        return _shared_prefs.getBoolean(_resources.getString(key_id), _resources.getBoolean(value_id)) ;
    }
    public long callbackIntervalMillisec() {
        return _resources.getInteger(R.integer.timer_callback_interval) ;
    }

    /****************************** PREFERENCE GETTERS ******************************/
    public long getLong(int key_id, int value_id) {
        return _shared_prefs.getLong(_resources.getString(key_id), _resources.getInteger(value_id));
    }
    public long timerMillisec() {
        return this.getLong(R.string.pomodoro_session_millisec, R.integer.default_timer_millisec);
    }

    public long breakMillisec() {
        return this.getLong(R.string.break_session_millisec, R.integer.default_break_millisec) ;
    }

    public long selectedTaskId() {
        // NOTE: we don't store a default value for task id. use invalid id flag in Task class instead
        return _shared_prefs.getLong(_resources.getString(R.string.selected_task_id),
                                            Task.INVALID_TASK_ID_FLAG) ;
    }

    public long longBreakMillisec() {
        return this.getLong(R.string.long_break_millisec, R.integer.default_long_break_millisec) ;
    }




    public boolean skipBreak() {
        return this.getBoolean(R.string.skip_break, R.bool.default_skip_break) ;
    }
    public boolean autoStartBreak() {
        return this.getBoolean(R.string.auto_start_break, R.bool.default_auto_start_break) ;
    }
    public boolean allowPauseSessions() {
        return this.getBoolean(R.string.allow_pause_session, R.bool.default_allow_pause_sessions) ;
    }

    public boolean autoStartNextSession() {
        return this.getBoolean(R.string.auto_start_next_session, R.bool.default_auto_start_next_session) ;
    }



    public int sessionsToLongBreak() {
        // NOTE: sessions stored as String because UI element used to update the preference
        // is an EditTextPreference
        String string_key = _resources.getString(R.string.sessions_to_long_break) ;
        String default_value = Integer.toString(
                _resources.getInteger(R.integer.default_sessions_to_long_break)) ;

        return Integer.parseInt(_shared_prefs.getString(string_key, default_value)) ;
    }

    /****************************** PREFERENCE SETTERS ******************************/

    public void setSelectedTaskId(Long new_id) {
        SharedPreferences.Editor editor = _shared_prefs.edit() ;
        editor.putLong(_resources.getString(R.string.selected_task_id), new_id) ;
        editor.commit() ;
    }

}
