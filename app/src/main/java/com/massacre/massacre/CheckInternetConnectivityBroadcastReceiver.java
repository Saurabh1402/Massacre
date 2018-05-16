package com.massacre.massacre;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by saurabh on 30/5/16.
 */
public class CheckInternetConnectivityBroadcastReceiver extends BroadcastReceiver {
    public static boolean isConnected=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(activeNetwork!=null)
            Log.e("SAURABH","internet connectivity changed: "+activeNetwork.isConnectedOrConnecting());
        if(isConnected){
            new StartAllService().execute(context);
        }
        else{
            Intent contactSyncService=new Intent(context,ContactSyncService.class);
            Intent messageSyncService=new Intent(context,MessagingService.class);
            context.stopService(contactSyncService);
            context.stopService(messageSyncService);

        }
    }
}
