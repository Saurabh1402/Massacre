package com.massacre.massacre;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;

/**
 * Created by saurabh on 14/5/16.
 */
public class StarAllService extends AsyncTask<Context,Void,Void> {
    @Override
    protected Void doInBackground(Context... array) {
        Context context=array[0];

        startMessagingService(context);
        startContactSynService(context);
        return null;


    }
    public void startMessagingService(Context context){
        final Intent messagingService = new Intent(context, MessagingService.class);
        final PendingIntent pending = PendingIntent.getService(context, 0,messagingService , 0);
        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        long interval = 5*1000;//milliseconds
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
    }
    public void startContactSynService(Context context){
        final Intent messagingService = new Intent(context, ContactSyncService.class);
        final PendingIntent pending = PendingIntent.getService(context, 0,messagingService , 0);
        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        long interval = 60*60*1000;//milliseconds
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
    }
    public void startNotificationService(Context context){

    }
}
