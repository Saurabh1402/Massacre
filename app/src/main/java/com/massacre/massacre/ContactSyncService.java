package com.massacre.massacre;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by saurabh on 6/5/16.
 */
public class ContactSyncService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_USER_PRESENT);

    }@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    SaveFile.updateFriendsObject(ContactSyncService.this.getBaseContext());
                    try {
                        Log.e("SAURABH","contactSyncSerevice ");
                        Thread.sleep(60*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }
}
