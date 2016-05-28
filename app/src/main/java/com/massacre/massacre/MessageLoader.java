package com.massacre.massacre;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;

import com.backendless.servercode.annotation.Async;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by saurabh on 17/5/16.
 */
public class MessageLoader extends AsyncTaskLoader<ArrayList<Message>>{
    UserProfile userProfile;
    public static final String MESSAGE_LISTENER_INTENT_FILTER_STRING ="com.massacre.MESSAGE_LISTENER";
    ArrayList<Message> cached;
    public MessageLoader(Context context,UserProfile userProfile) {
        super(context);
        this.userProfile=userProfile;


    }

    @Override
    protected void onStartLoading() {
        LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter=new IntentFilter(MESSAGE_LISTENER_INTENT_FILTER_STRING);
        localBroadcastManager.registerReceiver(messageListenerReceiver,filter);
        if(cached==null){
            forceLoad();
        }else{
            deliverResult(cached);

        }

    }

    @Override
    public ArrayList<Message> loadInBackground() {
        final ChatDbHelper chatDbHelper=new ChatDbHelper(getContext());
        Cursor cursor = chatDbHelper.getMessageofPhone(userProfile.getContact());
        String[] cols=new String[]{
                ChatDbHelper.MESSAGE_COLUMN_ID,
                ChatDbHelper.MESSAGE_COLUMN_MESSAGE,
                ChatDbHelper.MESSAGE_COLUMN_RECIPIENT,
                ChatDbHelper.MESSAGE_COLUMN_DATE,
                ChatDbHelper.MESSAGE_COLUMN_SEND_OR_RECEIVED,
                ChatDbHelper.MESSAGE_COLUMN_TYPE
        };
        cursor.moveToFirst();
        ArrayList<Message> messageList=new ArrayList<Message>();
        if(cursor.getCount()!=0)
        {
            do{
                int messageId=cursor.getInt(cursor.getColumnIndex(cols[0]));
                String messageText=cursor.getString(cursor.getColumnIndex(cols[1]));
                String messageRecipient= cursor.getString(cursor.getColumnIndex(cols[2]));
                String messageDate=cursor.getString(cursor.getColumnIndex(cols[3]));
                int sendOrReceived=cursor.getInt(cursor.getColumnIndex(cols[4]));
                int messageType=cursor.getInt(cursor.getColumnIndex(cols[5]));
                Date date=new Date();
//              Log.e("SAURABH",messageDate+"  "+ messageId);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    date=dateFormat.parse(messageDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//              Log.e("SAURABH",messageDate);
                Message message=new Message();
                message.setSendOrReceived(sendOrReceived);
                message.setRecipient(messageRecipient);
                message.setMessageId(messageId);
                message.setMessage(messageText);
                message.setType(messageType);
                message.setTime(date);
                //Log.e("SAURABH", "id:"+messageId+", message:"+messageText);
                messageList.add(message);
                cursor.moveToNext();
            }while(!cursor.isAfterLast());

        }
        return messageList;
    }

    @Override
    public void deliverResult(ArrayList<Message> data) {
        cached=data;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(messageListenerReceiver);
    }

    private BroadcastReceiver messageListenerReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    };
}
