package com.massacre.massacre;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.backendless.messaging.PublishOptions;

import java.io.InputStream;

/**
 * Created by saurabh on 30/4/16.
 */
public class AutoStartBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent) {
//        Toast.makeText(context, "service starting", Toast.LENGTH_LONG).show();


               // Start the service or activity
                Intent contactSyncService = new Intent(context, ContactSyncService.class);
                context.startService(contactSyncService);
                Intent messagingSyncService= new Intent(context, MessagingService.class);
                context.startService(messagingSyncService);



        }
}
