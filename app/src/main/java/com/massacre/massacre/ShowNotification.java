package com.massacre.massacre;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by saurabh on 29/4/16.
 */
public class ShowNotification extends IntentService{
    final int NOTIFICATION_ID=231994;

    public ShowNotification(String name) {
        super(name);
    }

    public ShowNotification() {
        super("Notification Service");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_USER_PRESENT);

    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String tickerText=intent.getStringExtra(PublishOptions.ANDROID_TICKER_TEXT_TAG);
        String contentTitle=intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TITLE_TAG);
        String contentText=intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TEXT_TAG);
        String messageObjectString=intent.getStringExtra(PublishOptions.MESSAGE_TAG);
        //Log.e("SAURABH",tickerText+"   ticker");
        //Log.e("SAURABH",contentText+" content text");
        //Log.e("SAURABH",contentTitle+" content title");
        //Log.e("SAURABH",messageObjectString+"  message");

        com.massacre.massacre.Message messageObject= new Gson().fromJson(messageObjectString,Message.class);
        String message=messageObject.message;
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(messageObject.getTime());
        long timeStamp= calendar.getTimeInMillis();//timestamp used for noitfication


        String recipient=messageObject.getRecipient();
        int messageType=messageObject.getType();
        int messageSenderReceived=ChatDbHelper.RECEIVED_MESSAGE;
        // ID, MESSAGE, RECIPIENT, DATE, SEND OR RECEIVED, TYPE
        //type  ===>>    1=> text ,,2=> photo 3=>video 4=>audio
        //Send or received  ====>>>   send =1 and recieved = 2 pending =>3 downloaded=3(in case of audio video and pic)

        ChatDbHelper chatDbHelper=new ChatDbHelper(this);
        if(chatDbHelper.insertMessage(message,recipient,messageObject.getTime(),messageSenderReceived,messageType)){
            //Log.e("SAURABH",true+" inserted");
        }
//        Log.e("Saurabh",chatDbHelper.deleteMessageofPhone(messageObject.getRecipient()).intValue()+"");
        Cursor cursor=chatDbHelper.getAllMessage();
        Log.e("Saurabh",cursor.getCount()+" column count");
        chatDbHelper.close();


        //BroadCasting to Chat Message
        Intent broadcastMessage=new Intent(MessageLoader.MESSAGE_LISTENER_INTENT_FILTER_STRING);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(broadcastMessage);



        //Notification Service
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        Intent start=new Intent(getBaseContext(),ContactActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getBaseContext(),0, start, 0);
        NotificationManager nm=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(contentText);
        builder.setContentTitle(SaveFile.getDataFromSharedPreference(getBaseContext(),recipient,recipient));
        builder.setContentIntent(pi);
        builder.setAutoCancel(true);
        builder.setSubText(message);
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(defaultRingtoneUri);
        builder.setWhen(timeStamp);
        builder.setTicker(tickerText);



        String pathName=MyApplication.getExternalAPPFolder()+"/"+MyApplication.getThumbnailFriendsProfileFolder(getBaseContext())+"/";
        String filName=recipient+".jpg";
        Bitmap image=SaveFile.getImageFromMobile(pathName,filName);
        if(image!=null)
            builder.setLargeIcon(image);
        else{
            image=BitmapFactory.decodeResource(getResources(),R.drawable.saurabh);
            builder.setLargeIcon(image);
        }


        nm.notify(NOTIFICATION_ID, builder.build());
    }

}
