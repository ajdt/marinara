package com.itsaunixsystem.marinara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.itsaunixsystem.marinara.orm.Task;

import java.util.ArrayList;

/**
 * @author: ajdt on 8/9/16.
 * @description: a custom adapter to display a task name with a delete button
 */
public class TaskArrayAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Task> _items ;
    private Context _context ;

    public TaskArrayAdapter(ArrayList<Task> items, Context ctx) {
        _items      = new ArrayList<Task>(items) ;
        _context    = ctx ;
    }

    /****************************** ListAdapter Interface Methods ******************************/

    @Override
    public int getCount() { return _items.size() ; }

    @Override
    public Object getItem(int position) { return _items.get(position) ; }

    @Override
    public long getItemId(int position) { return _items.get(position).getId() ; }

    @Override
    public View getView(final int position, View old_view, ViewGroup parent) {
        View new_view = old_view ;

        // old_view will be reused if it's not null
        if (new_view == null) {
            LayoutInflater inflater = (LayoutInflater)_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            new_view = inflater.inflate(R.layout.list_view_task_item, null) ;
        }

        // set text of layout
        final Task the_task     = (Task)this.getItem(position) ;
        TextView task_name_tv   = (TextView)new_view.findViewById(R.id.task_name_tv) ;
        task_name_tv.setText(the_task.getName());

        /*
        // set onClick callback for delete button
        ImageButton delete_task_button = (ImageButton)new_view.findViewById(R.id.delete_task_ib) ;
        delete_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call Task deletion and remove the item locally too
                the_task.delete() ;

                TaskArrayAdapter.this._items.remove(the_task) ;
                TaskArrayAdapter.this.notifyDataSetChanged() ;
            }
        });
        */

        return new_view ;
    }

    /****************************** Misc ******************************/

    public int getPosition(Task task) { return _items.indexOf(task) ; }
}
