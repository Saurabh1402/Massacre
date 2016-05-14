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
        Toast.makeText(context, "service starting", Toast.LENGTH_LONG).show();


        if ((intent.getAction() != null) && (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")))
            {
                // Start the service or activity
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        final Intent service = new Intent(context, ContactSyncService.class);
                        final PendingIntent pending = PendingIntent.getService(context, 0, service, 0);
                        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarm.cancel(pending);
                        long interval = 60*60*1000;//milliseconds
                        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
                        return null;
                    }
                }.execute();




            }
        }
}
