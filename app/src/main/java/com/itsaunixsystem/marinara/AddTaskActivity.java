package com.itsaunixsystem.marinara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.itsaunixsystem.marinara.orm.Task;

public class AddTaskActivity extends AppCompatActivity {

    // name of last clicked list item, corresponds to name of Task in DB
    private String _last_clicked_list_item = "" ;

    /****************************** OVERRIDDEN METHODS  ******************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task) ;
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
        super.onPause() ;
        this.saveLastClickedTaskToPreferences() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /****************************** EVENT CALLBACKS ******************************/

    /**
     * Launches NewTaskDialogActivity. Called when 'add task' button is clicked.
     * @param view a handle to view that was clicked
     */
    public void onAddTaskButtonClicked(View view)  {
        Intent intent = new Intent(this, NewTaskDialogActivity.class) ;
        startActivity(intent) ;
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
                _last_clicked_list_item = (String) list_view.getAdapter().getItem(position);
            }
        });
    }

    /****************************** MISC HELPERS ******************************/

    private void saveLastClickedTaskToPreferences() {

        // no item has been clicked
        if (_last_clicked_list_item.equals(""))
            return ;

        // get Task object matching saved name String and save it's task id
        Task selected_task = Task.getByName(_last_clicked_list_item) ;

        if (selected_task == null) {// TODO: this should not happen
            Log.d(this.getClass().getSimpleName(), "error: last clicked 'name' is not in database:"
                    + _last_clicked_list_item) ;
        } else { // save selected task's id value
            MarinaraPreferences.getPrefs(this).setSelectedTaskId(this, selected_task.getId()) ;
        }
    }

    private void displayLastUsedTaskAsSelected() {

        // get Task obj of selected id saved in preferences
        long task_id = MarinaraPreferences.getPrefs(this).selectedTaskId() ;
        Task task = Task.getById(task_id) ;

        // don't highlight anything if task_id is unmatched in DB
        if (task == null) {
            return ;
        }

        // find position in adapter
        ListView list_view              = (ListView)findViewById(R.id.add_task_list_view) ;
        ArrayAdapter<String> adapter    = (ArrayAdapter<String>)list_view.getAdapter() ;
        String task_name                = task.getName() ;
        int position                    = adapter.getPosition(task_name) ;

        // set corresponding item to checked
        list_view.setItemChecked(position, true) ;
    }

    /**
     * create an adapter containing all task names and assign it to add_task_list_view
     */
    private void setAdapterForTaskNamesListView() {
        ListView list_view = (ListView)findViewById(R.id.add_task_list_view) ;
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_activated_1,
                Task.getActiveTaskNames()) ;
        list_view.setAdapter(adapter) ;
    }
}
