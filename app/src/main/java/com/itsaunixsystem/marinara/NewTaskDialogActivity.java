package com.itsaunixsystem.marinara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itsaunixsystem.marinara.orm.Task;

public class NewTaskDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_dialog);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task_dialog, menu);
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


    public void onOkButtonClicked(View view) {
        // get task name
        String new_task_name = ((EditText)findViewById(R.id.task_dialog_edit_text))
                                    .getText().toString() ;

        // task name is valid, add it to task list and finish
        if (new_task_name.equals("")) {
            Toast.makeText(NewTaskDialogActivity.this, "task name is empty", Toast.LENGTH_SHORT).show() ;
        } else if (Task.isActiveTask(new_task_name)) {
            Toast.makeText(NewTaskDialogActivity.this, "task name already exists", Toast.LENGTH_SHORT).show() ;
        } else if (Task.isDeletedTask(new_task_name)) {
            // task existed previously, but was deleted. Restore its status
            Task the_task = Task.getByName(new_task_name) ;
            the_task.status = Task.ACTIVE_STATUS ;
            the_task.save() ;

        }
        else {
            // new task name that's valid
            Task new_task = new Task(new_task_name, Task.ACTIVE_STATUS) ;
            new_task.save() ;
            this.finish() ;
        }
    }
}