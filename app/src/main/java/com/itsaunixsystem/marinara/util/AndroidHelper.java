package com.itsaunixsystem.marinara.util;

import android.content.Context;
import android.content.Intent;

/**
 * @author: ajdt on 8/14/16.
 * @description: holds static methods to DRY out commonly used android code. I may be going
 * overboard here.
 */
public class AndroidHelper {

    public static void launchActivity(Context context, Class activity_class) {
        Intent intent = new Intent(context, activity_class) ;
        context.startActivity(intent) ;
    }
}
