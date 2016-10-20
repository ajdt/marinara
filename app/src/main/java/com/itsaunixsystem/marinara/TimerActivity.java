package com.itsaunixsystem.marinara;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.itsaunixsystem.marinara.orm.PomodoroSession;
import com.itsaunixsystem.marinara.orm.Task;
import com.itsaunixsystem.marinara.timer.TimerState;
import com.itsaunixsystem.marinara.util.AndroidHelper;
import com.itsaunixsystem.marinara.util.MarinaraPreferences;


public class TimerActivity extends BaseTimerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: content view is set by BaseTimerActivity
        super.onCreate(savedInstanceState);

        // set listener so long-pressing task name text view will launch ManageTasksActivity
        this.setLongPressListenerForTaskTextView() ;
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
        if (!this.skipBreaksPreference()) {
            AndroidHelper.launchActivity(this, BreakActivity.class) ;
        }
    }

    /****************************** UI UPDATING ******************************/

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

    public long timerDurationPreference() { return MarinaraPreferences.getPrefs(this).timerMillisec() ;}
    public boolean skipBreaksPreference() { return MarinaraPreferences.getPrefs(this).skipBreak() ; }
    public boolean allowPausePreference() { return MarinaraPreferences.getPrefs(this).allowPauseSessions() ; }


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
