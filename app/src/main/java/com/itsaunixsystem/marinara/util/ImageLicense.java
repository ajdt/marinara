package com.itsaunixsystem.marinara.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;

import com.itsaunixsystem.marinara.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author: ajdt on 9/4/16.
 * @description: Class used to parse ImageLicense xml entries which contain image licensing info
 */
public class ImageLicense {

    private int _icon_resource_id ;
    private String _desc, _author, _license ;

    public ImageLicense(Context ctx, AttributeSet attrs) {
        // examine attributes one by one
        for (int i = 0 ; i < attrs.getAttributeCount() ; i++) {
            // each attribute is a resource id. obtain it
            int attr_resource_id = attrs.getAttributeResourceValue(i, 0 /* default value */) ;

            // for string fields assign string, for drawable field assign resource id
            switch (attrs.getAttributeName(i)) {
                case "icon":
                    _icon_resource_id =  attr_resource_id ;
                    break ;
                case "desc":
                    _desc = ctx.getString(attr_resource_id) ;
                    break ;
                case "author":
                    _author = ctx.getString(attr_resource_id) ;
                    break ;
                case "license":
                    _license = ctx.getString(attr_resource_id) ;
                    break ;
            }
        }
    }

    /****************************** GETTERS ******************************/
    public int getIconResourceId() { return _icon_resource_id ; }
    public String getDescription() { return _desc ;}
    public String getAuthor() { return _author ;}
    public String getLicense() { return _license ;}

    /****************************** XML INFLATOR ******************************/

    // inflate ImageLicense entries in R.xml.iamge_licenses
    public static ArrayList<ImageLicense> inflateFromXml(Context context) {
        ArrayList<ImageLicense> license_entries = new ArrayList<ImageLicense>() ;

        XmlResourceParser parser = context.getResources().getXml(R.xml.image_licenses) ;
        try {
            int current_token = parser.next();

            // assumed: start_tag will be an ImageLicense object with
            // no text content and no nested items
            while (current_token != XmlPullParser.END_DOCUMENT) {
                switch (current_token) {
                    case XmlPullParser.START_TAG:
                        license_entries.add(new ImageLicense(context, parser));
                        break;
                }
                current_token = parser.next();
            }
        } catch (XmlPullParserException parse_except) {
            // do nothing for now. Okay to return an empty list...
        } catch (IOException except) {
            //..ditto
        }

        return license_entries ;
    }


}
