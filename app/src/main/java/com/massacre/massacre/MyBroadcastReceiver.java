package com.massacre.massacre;

import android.content.Context;
import android.content.Intent;

import com.backendless.messaging.PublishOptions;
import com.backendless.push.BackendlessBroadcastReceiver;

/**
 * Created by saurabh on 19/4/16.
 */
public class MyBroadcastReceiver extends BackendlessBroadcastReceiver {
    public MyBroadcastReceiver() {
        super();
    }

    public MyBroadcastReceiver(String senderId) {
        super(senderId);
    }

    @Override
    public boolean onMessage(Context context, Intent intent) {

        Intent e=new Intent(context,ShowNotification.class);
        /*
        Log.e("SAURABH",intent.getStringExtra(PublishOptions.ANDROID_TICKER_TEXT_TAG));
        Log.e("SAURABH",intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TEXT_TAG));
        Log.e("SAURABH",intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TITLE_TAG));
        */
        e.putExtra(PublishOptions.ANDROID_TICKER_TEXT_TAG,intent.getStringExtra(PublishOptions.ANDROID_TICKER_TEXT_TAG));
        e.putExtra(PublishOptions.ANDROID_CONTENT_TEXT_TAG,intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TEXT_TAG));
        e.putExtra(PublishOptions.ANDROID_CONTENT_TITLE_TAG,intent.getStringExtra(PublishOptions.ANDROID_CONTENT_TITLE_TAG));
        e.putExtra(PublishOptions.MESSAGE_TAG,intent.getStringExtra(PublishOptions.MESSAGE_TAG));
        context.startService(e);
        //return super.onMessage(context, intent);
        return false;
    }

}
