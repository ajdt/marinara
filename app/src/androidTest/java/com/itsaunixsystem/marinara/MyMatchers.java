package com.itsaunixsystem.marinara;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * @author: ajdt on 7/31/16.
 * @description: additional matchers not provided by espresso
 */
public class MyMatchers {

    // Disclaimer: most of the code for this matcher came from (with minor changes by me -- ajdt):
    //      http://hitherejoe.com/testing-imageview-changes-android-espresso-automated-tests/
    //      https://medium.com/@dbottillo/android-ui-test-espresso-matcher-for-imageview-1a28c832626f#.m2e40yybm
    public static Matcher IsUsingDrawable(final int resource_id) {
        return new BoundedMatcher(ImageView.class) {
            @Override
            protected boolean matchesSafely(Object view) {
                if (!(view instanceof ImageView))
                    return false ;

                // cast to ImageView and obtain bitmap of image being displayed
                ImageView image_view = (ImageView) view ;
                Bitmap image_view_bitmap = ((BitmapDrawable)image_view.getDrawable()).getBitmap() ;

                // get bitmap corresponding to resource id given
                // NOTE: using deprecated getDrawable() since alternative is only available API 21 and above
                Drawable expected_drawable =
                        image_view.getResources().getDrawable(resource_id) ;
                Bitmap expected_bitmap = ((BitmapDrawable) expected_drawable).getBitmap() ;

                return expected_bitmap.sameAs(image_view_bitmap) ;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is using image with resource id: " + Integer.toString(resource_id)) ;
            }
        } ;
    }
}
