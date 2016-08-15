package com.itsaunixsystem.marinara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itsaunixsystem.marinara.orm.Task;
import com.itsaunixsystem.marinara.orm.TaskStatus;

public class NewTaskDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_dialog);
    }

    /**
     * create a new task with the name in task_dialog_edit_text if a valid, unused
     * task name is provided. If an existing task name is given, ensure the existing task
     * has status ACTIVE and not DELETED
     * @param view handle to view that was clicked
     */
    public void onOkButtonClicked(View view) {
        // get task name
        String new_task_name = ((EditText)findViewById(R.id.task_dialog_edit_text))
                                    .getText().toString() ;
        // save new task, if valid
        if (new_task_name.equals("")) {
            // empty task name
            Toast.makeText(NewTaskDialogActivity.this, "task name is empty", Toast.LENGTH_SHORT).show() ;
        } else if (Task.isActiveTask(new_task_name)) {
            // existing task name
            Toast.makeText(NewTaskDialogActivity.this, "task name already exists", Toast.LENGTH_SHORT).show() ;
        } else {
            // new task name that's valid
            Task new_task = new Task(new_task_name, TaskStatus.ACTIVE) ;
            new_task.create() ;
            this.finish() ;
        }
    }
}
