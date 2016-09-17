package com.itsaunixsystem.marinara.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.itsaunixsystem.marinara.R;
import com.itsaunixsystem.marinara.TimerActivity;

/**
 * @author: ajdt on 8/14/16.
 * @description: holds static methods to DRY out commonly used android code. I may be going
 * overboard here.
 */
public class AndroidHelper {

    public static final int SESSION_FINISHED_NOTIFICATION_ID = 1;

    public static void launchActivity(Context context, Class activity_class) {
        Intent intent = new Intent(context, activity_class) ;
        context.startActivity(intent) ;
    }

    public static void issueNotification(Context ctx, Class<? extends Activity> result_activity,
                                         String title, String content_text) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.notification_template_icon_bg)
                        .setContentTitle(title)
                        .setContentText(content_text) ;

        // Creates intent for result of notification click
        //Intent result_intent = new Intent(ctx, result_activity) ;
        Intent result_intent = new Intent(ctx, TimerActivity.class) ;
        result_intent.setAction(Intent.ACTION_MAIN) ;
        result_intent.addCategory(Intent.CATEGORY_LAUNCHER) ;
        result_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stack_builder = TaskStackBuilder.create(ctx) ;

        // Adds the back stack for the Intent (but not the Intent itself)
        stack_builder.addParentStack(result_activity) ;

        // Adds the Intent that starts the Activity to the top of the stack
        stack_builder.addNextIntent(result_intent) ;
        PendingIntent result_pending_intent =
                stack_builder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                ) ;
        builder.setContentIntent(result_pending_intent) ;
        NotificationManager notification_manager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE) ;

        Notification notif = builder.build() ;
        notif.defaults |= Notification.DEFAULT_VIBRATE ;
        notif.defaults |= Notification.DEFAULT_SOUND ;

        notification_manager.notify(SESSION_FINISHED_NOTIFICATION_ID, notif) ;
    }
    public static void playNotificationSound(Context ctx) {
        Uri notification_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) ;
        Ringtone the_tone = RingtoneManager.getRingtone(ctx, notification_uri) ;
        the_tone.play() ;
    }
}
