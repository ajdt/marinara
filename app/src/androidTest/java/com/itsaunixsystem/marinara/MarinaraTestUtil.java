package com.itsaunixsystem.marinara;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.os.Environment;

/**
 * @author: ajdt on 8/4/16.
 * @description: utilities that can be used by instrumented tests (right now just screenshot saver)
 */
public class MarinaraTestUtil {

    // NOTE: none of the code for takeScreenshot() is mine. I obtained it from:
    // http://testdroid.com/tech/tips-and-tricks-taking-screenshots-with-espresso-or-espresso-v2-0
    // It has been modified (mostly formatting) from its original form, however.
    public static void takeScreenshot(String name, Activity activity) {
        String path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/" + name + ".png" ;


        View scrView = activity.getWindow().getDecorView().getRootView() ;
        scrView.setDrawingCacheEnabled(true) ;
        Bitmap bitmap = Bitmap.createBitmap(scrView.getDrawingCache()) ;
        scrView.setDrawingCacheEnabled(false) ;

        OutputStream out = null ;
        File imageFile = new File(path) ;

        try {
            out = new FileOutputStream(imageFile) ;
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out) ;
            out.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Error: could not write file: " + path +". Check Permissions") ;
            System.out.println(e.getMessage()) ;
            System.out.println(e.getStackTrace()) ;
        } catch (IOException e) {
            System.out.println("I/O Error when writing file: " + path ) ;
            System.out.println(e.getMessage()) ;
            System.out.println(e.getStackTrace()) ;
        } finally {

            try {
                if (out != null) {
                    out.close();
                }

            } catch (Exception exc) {
            }

        }
    }
}
