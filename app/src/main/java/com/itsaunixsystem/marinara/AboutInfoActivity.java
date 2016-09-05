package com.itsaunixsystem.marinara;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.itsaunixsystem.marinara.util.AndroidHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AboutInfoActivity extends ListActivity {

    ArrayList<String> _entries ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain string list from resources and add version string
        _entries = new ArrayList<>(Arrays.asList(this.getResources().getStringArray(R.array.about_array))) ;
        this.addVersionString(_entries) ;

        // create/set array adapter
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                                                    android.R.layout.simple_list_item_1, _entries) ;
        this.setListAdapter(adapter) ;
    }

    @Override
    protected void onListItemClick(ListView list_view, View view, int position, long id) {
        String clicked = (String)this.getListAdapter().getItem(position) ;

        if (clicked.equals( this.getResources().getString(R.string.image_license_text)))
            AndroidHelper.launchActivity(this, ImageLicensesActivity.class) ;
    }

    /**
     * add version string separately. Version isn't part of R.array.about_array because
     * I can't access BuildConfig.VERSION_NAME from strings.xml
     */
    private void addVersionString(ArrayList<String> entries) {
        String version_string = this.getResources().getString(R.string.version_text) +
                                            " " + BuildConfig.VERSION_NAME ;

        entries.add(0, version_string) ;
    }



}
