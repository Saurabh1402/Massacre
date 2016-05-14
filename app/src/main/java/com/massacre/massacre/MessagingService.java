package com.massacre.massacre;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PushPolicyEnum;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by saurabh on 12/5/16.
 */
public class MessagingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        Backendless.initApp(this,
                MyApplication.APPLICATION_SECRET_KEY_BACKENDLESS,
                MyApplication.ANDROID_SECRET_KEY_BACKENDLESS,
                MyApplication.APPLICATION_VERSION_BACKENDLESS
        );
        ChatDbHelper db=new ChatDbHelper(this);
        Cursor cursor=db.getPendingMessages();
        Log.e("Saurabh",cursor.getCount()+" column count");
        String[] cols=new String[]{
                ChatDbHelper.MESSAGE_COLUMN_ID,
                ChatDbHelper.MESSAGE_COLUMN_MESSAGE,
                ChatDbHelper.MESSAGE_COLUMN_RECIPIENT,
                ChatDbHelper.MESSAGE_COLUMN_DATE,
                ChatDbHelper.MESSAGE_COLUMN_SEND_OR_RECEIVED,
                ChatDbHelper.MESSAGE_COLUMN_TYPE
        };
        cursor.moveToFirst();
        if(cursor.getCount()!=0)
            do{
                Log.e("SAURABH",cursor.getString(cursor.getColumnIndex(cols[0])));
                Log.e("SAURABH",cursor.getString(cursor.getColumnIndex(cols[1])));
                Log.e("SAURABH",cursor.getString(cursor.getColumnIndex(cols[2])));
                Log.e("SAURABH",cursor.getString(cursor.getColumnIndex(cols[3])));
                Log.e("SAURABH",cursor.getString(cursor.getColumnIndex(cols[4])));
                Log.e("SAURABH",cursor.getString(cursor.getColumnIndex(cols[5])));
                cursor.moveToNext();
            }while(!cursor.isAfterLast());


        return START_STICKY;
    }
    public void sendMessage(final ChatDbHelper db,String contentTitle,String contentText,String tickerText,String message,String recipientDeviceId){
        DeliveryOptions deliveroption = new DeliveryOptions();
        deliveroption.setPushPolicy(PushPolicyEnum.ONLY);
        deliveroption.addPushSinglecast(recipientDeviceId);//9936851c  f89ade4b

        PublishOptions publishOptions = new PublishOptions();
        publishOptions.putHeader("android-ticker-text",tickerText);
        publishOptions.putHeader("android-content-title",contentTitle);
        publishOptions.putHeader("android-content-text",contentText);

        com.massacre.massacre.Message messageObject=new com.massacre.massacre.Message();
        messageObject.setMessage(message);
        String senderPhoneNumber=SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.COUNTRY_CODE,"")+SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.PHONE_NUMBER,"");
        messageObject.setRecipient(senderPhoneNumber);
        messageObject.setTime(new Date());
        messageObject.setType(1);
        message=new Gson().toJson(messageObject);
        Backendless.Messaging.publish(message, publishOptions, deliveroption,new AsyncCallback<MessageStatus>(){

            @Override
            public void handleResponse(MessageStatus messageStatus) {
                Log.e("SAURABH",messageStatus.toString());
                //db.updateMessage()
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.e("SAURABH",backendlessFault.getMessage());

            }
        });

    }

}
