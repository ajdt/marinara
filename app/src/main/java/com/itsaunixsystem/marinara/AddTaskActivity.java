package com.itsaunixsystem.marinara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.itsaunixsystem.marinara.orm.Task;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    @Override
    protected void onResume() {
        super.onResume() ;
        setListViewAdapter() ;
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


    public void onAddTaskButtonClicked(View view)  {
        Intent intent = new Intent(this, NewTaskDialogActivity.class) ;
        startActivity(intent) ;
    }

    private void setListViewAdapter() {
        ListView list_view = (ListView)findViewById(R.id.add_task_list_view) ;
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.getTasks()) ;
    }

    // TODO: create a DB utility function that does this???
    private ArrayList<Task> getTasks() {
        return new ArrayList(Task.listAll(Task.class)) ;
    }
}
