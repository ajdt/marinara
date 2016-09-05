package com.itsaunixsystem.marinara.util;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.itsaunixsystem.marinara.R;
import com.itsaunixsystem.marinara.orm.Task;

import java.util.ArrayList;

/**
 * @author: ajdt on 9/4/16.
 * @description: an adapter for ImageLicense objects
 */
public class ImageLicenseAdapter extends BaseAdapter implements ListAdapter {

    private Context _context ;
    ArrayList<ImageLicense> _entries ;

    public ImageLicenseAdapter(Context context) {
        _entries = ImageLicense.inflateFromXml(context) ;
        _context = context ;
    }

    @Override
    public int getCount() { return _entries.size() ; }

    @Override
    public Object getItem(int position) { return _entries.get(position) ; }

    // NOTE: not used
    @Override
    public long getItemId(int position) { return position ; }

    @Override
    public View getView(final int position, View old_view, ViewGroup parent) {
        View new_view = old_view ;

        // old_view will be reused if it's not null
        if (new_view == null) {
            LayoutInflater inflater = (LayoutInflater)_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            new_view = inflater.inflate(R.layout.list_item_image_license, null) ;
        }

        // corresponding license object
        ImageLicense license_obj = _entries.get(position) ;

        // set icon
        ImageView icon_view = (ImageView)new_view.findViewById(R.id.list_item_image_view) ;
        icon_view.setImageResource(license_obj.getIconResourceId()) ;

        // get text views
        TextView desc_tv = (TextView)new_view.findViewById(R.id.description_tv) ;
        TextView author_tv = (TextView)new_view.findViewById(R.id.author_tv) ;
        TextView license_tv = (TextView)new_view.findViewById(R.id.license_tv) ;

        // set text fields
        desc_tv.setText(Html.fromHtml(license_obj.getDescription())) ;
        author_tv.setText(Html.fromHtml(license_obj.getAuthor())) ;
        license_tv.setText(Html.fromHtml(license_obj.getLicense())) ;

        // set TextView movement method to make links clickable
        desc_tv.setMovementMethod(LinkMovementMethod.getInstance()) ;
        author_tv.setMovementMethod(LinkMovementMethod.getInstance()) ;
        license_tv.setMovementMethod(LinkMovementMethod.getInstance()) ;


        return new_view ;
    }

}
