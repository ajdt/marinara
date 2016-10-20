package com.itsaunixsystem.marinara;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.itsaunixsystem.marinara.orm.PomodoroSession;
import com.itsaunixsystem.marinara.orm.Task;
import com.itsaunixsystem.marinara.orm.TaskStatus;
import com.itsaunixsystem.marinara.timer.TimerState;
import com.itsaunixsystem.marinara.util.AndroidHelper;
import com.itsaunixsystem.marinara.util.MarinaraPreferences;

import java.util.Date;


public class TimerActivity extends BaseTimerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note: content view set by BaseTimerActivity
        // load preferences and initialize timer/callbacks
        MarinaraPreferences prefs = MarinaraPreferences.getPrefs(this) ;
        this.initCallbacks() ;
    }

    @Override
    protected void onResume() {
        super.onResume() ;

        // set task name again, in case a new task was selected
        this.setTaskNameInTaskTextView() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer, menu) ;
        return true ;
    }

    /****************************** TIMER AND UI CALLBACKS ******************************/

    private void initCallbacks() {

        // long-pressing text view displaying task will launch ManageTasksActivity
        this.setLongPressListenerForTaskTextView() ;

        // NOTE: remaining callbacks are registered via xml layouts
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                AndroidHelper.launchActivity(this, SettingsActivity.class) ;
                break ;
            case R.id.add_task_menu_item:
                AndroidHelper.launchActivity(this, ManageTasksActivity.class) ;
                break ;
            case R.id.stats_menu_item:
                AndroidHelper.launchActivity(this, StatsActivity.class) ;
                break ;
            case R.id.about_menu_item:
                AndroidHelper.launchActivity(this, AboutInfoActivity.class) ;
                break ;
        }
        return true ; // consume UI event
    }

    /**
     * callback updates UI and changes timer_state to done
     */
    @Override
    public void onTimerFinish() {
        super.onTimerFinish() ;
        PomodoroSession.saveNewSession(this.getCurrentSessionDuration(), this.getSelectedTaskName()) ;

        // break time?
        if (!this.skipBreaks()) {
            AndroidHelper.launchActivity(this, BreakActivity.class) ;
        }
    }

    /**
     * enable long-clicking and set a listener on text view that displays current active task name
     */
    public void setLongPressListenerForTaskTextView() {

        // get textview, enable long-clicking and set long-click listener
        TextView text_view = (TextView)findViewById(R.id.task_tv) ;
        text_view.setLongClickable(true) ;
        text_view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                AndroidHelper.launchActivity(TimerActivity.this, ManageTasksActivity.class) ;
                return true ; // return true to indicate long click is consumed
            }

        });
    }

    /****************************** UI UPDATING ******************************/

    /**
     * set the text of task_tv to the currently selected task's name
     */
    public void setTaskNameInTaskTextView() {
        TextView task_text_view = (TextView)findViewById(R.id.task_tv) ;
        task_text_view.setText(this.getSelectedTaskName()) ;
    }

    /****************************** SUBCLASSES MUST OVERRIDE THESE TO CHANGE BEHAVIOR ******************************/
    // NOTE: MarinaraPreferences is called every time a preference is needed. This guarantees
    // we are always using the latest value, and saves the trouble of having to save local copies
    // of the preference values (and implementing onSharedPreferenceChangeListener in this class too).

    public long getTimerDuration() { return MarinaraPreferences.getPrefs(this).timerMillisec() ;}
    public boolean skipBreaks() { return MarinaraPreferences.getPrefs(this).skipBreak() ; }
    public boolean allowPause() { return MarinaraPreferences.getPrefs(this).allowPauseSessions() ; }


    public TimerState initialState() { return TimerState.READY ; }

    /**
     *
     * @return task name of the currently selected task
     */
    public String getSelectedTaskName() {
        long id     = MarinaraPreferences.getPrefs(this).selectedTaskId() ;
        Task task   = Task.getById(id) ;

        // if task not found, use first available task. Task.delete() ensures
        // at least one active task is in the Task table
        if (task == null) {
            task = Task.getActiveTasks().get(0) ;
        }

        return task.getName() ;
    }
}
