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
public class ContactSyncService extends IntentService{

    public ContactSyncService(String name) {
        super(name);
    }

    public ContactSyncService() {
        super("Update Contact Service");
    }

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
    protected void onHandleIntent(Intent intent) {
        //Log.e("Saurabh", "1");
//        if(MyApplication.isNetworkAvailable(getBaseContext())){
//            Toast.makeText(getBaseContext(),"Available", Toast.LENGTH_SHORT).show();
//        }
//        else{
//
//            Toast.makeText(getBaseContext(),"Not Available", Toast.LENGTH_SHORT).show();
//        }
        /*int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        */    SaveFile.updateFriendsObject(this.getBaseContext());
        //}


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.e("Saurabh", "1");
//        if(MyApplication.isNetworkAvailable(getBaseContext())){
//            Toast.makeText(getBaseContext(),"Available", Toast.LENGTH_SHORT).show();
//            Log.e("SAURABH","available"+new Date());
//        }
//        else{
//
//            Toast.makeText(getBaseContext(),"Not Available", Toast.LENGTH_SHORT).show();
//            Log.e("SAURABH","unavailable"+new Date());
//        }
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
            SaveFile.updateFriendsObject(this.getBaseContext());
        }
        return START_STICKY;
    }
}
