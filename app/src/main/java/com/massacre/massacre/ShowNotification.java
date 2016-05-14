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
import android.text.format.DateFormat;
import android.util.Log;

import com.backendless.messaging.PublishOptions;
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
        long timeStamp= calendar.getTimeInMillis();
        String recipient=messageObject.getRecipient();
        int messageType=messageObject.getType();

        // ID, MESSAGE, RECIPIENT, DATE, SEND OR RECEIVED, TYPE
        //type  ===>>    1=> text ,,2=> photo 3=>video 4=>audio
        //Send or received  ====>>>   send =1 and recieved = 2 pending =>3 downloaded=3(in case of audio video and pic)

        ChatDbHelper chatDbHelper=new ChatDbHelper(this);
        if(chatDbHelper.insertMessage(message,recipient,messageObject.getTime(),ChatDbHelper.RECEIVED_MESSAGE,messageType)){
            //Log.e("SAURABH",true+" inserted");
        }
//        Log.e("Saurabh",chatDbHelper.deleteMessageofPhone(messageObject.getRecipient()).intValue()+"");
        Cursor cursor=chatDbHelper.getAllMessage();
//          Log.e("Saurabh",cursor.getCount()+" column count");
//        String[] cols=new String[]{
//                ChatDbHelper.MESSAGE_COLUMN_ID,
//                ChatDbHelper.MESSAGE_COLUMN_MESSAGE,
//                ChatDbHelper.MESSAGE_COLUMN_RECIPIENT,
//                ChatDbHelper.MESSAGE_COLUMN_DATE,
//                ChatDbHelper.MESSAGE_COLUMN_SEND_OR_RECEIVED,
//                ChatDbHelper.MESSAGE_COLUMN_TYPE
//        };
        /*cursor.moveToFirst();
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

*/
        chatDbHelper.close();
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        Intent start=new Intent(getBaseContext(),Register.class);
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
