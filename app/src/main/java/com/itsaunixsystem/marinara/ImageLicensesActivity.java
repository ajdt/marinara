package com.itsaunixsystem.marinara;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.itsaunixsystem.marinara.util.ImageLicenseAdapter;

public class ImageLicensesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLicenseAdapter adapter = new ImageLicenseAdapter(this) ;
        this.setListAdapter(adapter) ;
    }

}
