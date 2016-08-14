package com.itsaunixsystem.marinara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itsaunixsystem.marinara.orm.Task;
import com.itsaunixsystem.marinara.util.AndroidHelper;
import com.itsaunixsystem.marinara.util.MarinaraPreferences;
import com.itsaunixsystem.marinara.util.TaskArrayAdapter;

public class ManageTasksActivity extends AppCompatActivity {

    // stores Task.id of last clicked item (to be saved in preferences)
    // NOTE: -1 will never be used as legitimate Task id, safe to use as flag
    private long _last_clicked_task_id = -1 ;

    /****************************** OVERRIDDEN METHODS  ******************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks) ;
        setListViewOnItemClickListener() ;
    }

    @Override
    protected void onResume() {
        super.onResume() ;

        // done in case a new task has been added since activity left foreground
        setAdapterForTaskNamesListView() ;
        displayLastUsedTaskAsSelected() ;
    }

    @Override
    protected void onPause() {
        // NOTE: we save the task on pause() b/c onDestroy() may run after successor
        // Activity's onCreate() or onResume() methods
        super.onPause() ;
        this.saveLastClickedTaskToPreferences() ;
    }

    /****************************** EVENT CALLBACKS ******************************/

    /**
     * Launches NewTaskDialogActivity. Called when 'add task' button is clicked.
     * @param view a handle to view that was clicked
     */
    public void onAddTaskButtonClicked(View view)  {
        AndroidHelper.launchActivity(this, NewTaskDialogActivity.class) ;
    }

    /**
     * sets item click listener for ListView that displays all task names. When a task is
     * selected from the ListView we save it's name (String) to a member variable.
     */
    private void setListViewOnItemClickListener() {
        final ListView list_view = (ListView)findViewById(R.id.add_task_list_view) ;

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when item is clicked, save its name
                _last_clicked_task_id = list_view.getAdapter().getItemId(position) ;
            }
        });
    }

    /****************************** MISC HELPERS ******************************/

    private void saveLastClickedTaskToPreferences() {

        // no item has been clicked
        if (_last_clicked_task_id == -1)
            return ;

        MarinaraPreferences.getPrefs(this).setSelectedTaskId(_last_clicked_task_id) ;
    }

    private void displayLastUsedTaskAsSelected() {
        // get Task obj for task_id in preferences and get listView
        long task_id        = MarinaraPreferences.getPrefs(this).selectedTaskId() ;
        Task task           = Task.getById(task_id) ;
        ListView list_view  = (ListView)findViewById(R.id.add_task_list_view) ;

        // if can't find task in DB, then highlight first item in listview
        if (task == null) {
            list_view.setItemChecked(0, true) ;
            return ;
        }

        // otherwise, find task name's position in adapter...
        TaskArrayAdapter adapter        = (TaskArrayAdapter)list_view.getAdapter() ;
        int position                    = adapter.getPosition(task) ;

        // ...and set corresponding item in listview to checked
        list_view.setItemChecked(position, true) ;
    }

    /**
     * create an adapter containing all task names and assign it to add_task_list_view
     */
    private void setAdapterForTaskNamesListView() {
        ListView list_view = (ListView)findViewById(R.id.add_task_list_view) ;
        TaskArrayAdapter adapter = new TaskArrayAdapter(Task.getActiveTasks(), this) ;
        list_view.setAdapter(adapter) ;
    }
}
