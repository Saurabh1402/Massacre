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
public class StartAllService extends AsyncTask<Context,Void,Void> {
    @Override
    protected Void doInBackground(Context... array) {
        Context context=array[0];

        startMessagingService(context);
        startContactSynService(context);
        return null;


    }
    public void startMessagingService(Context context){
        final Intent messagingService = new Intent(context, MessagingService.class);
        context.startService(messagingService);
    }
    public void startContactSynService(Context context){
        final Intent contactSync = new Intent(context, ContactSyncService.class);
        context.startService(contactSync);
    }
    public void startNotificationService(Context context){

    }
}
