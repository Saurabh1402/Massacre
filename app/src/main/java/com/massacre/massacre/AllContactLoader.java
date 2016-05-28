package com.massacre.massacre;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by saurabh on 17/5/16.
 */
public class AllContactLoader extends AsyncTaskLoader<ArrayList<UserProfile>> {
    public static final String INTENT_FILTER_ALL_FRIEND="com.massacre.SYNC_FRIEND";
    ArrayList<UserProfile> cached;
    public AllContactLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter=new IntentFilter(INTENT_FILTER_ALL_FRIEND);
        localBroadcastManager.registerReceiver(broadcastReceiver,filter);

        if(cached==null){
            forceLoad();
        }else{
            deliverResult(cached);
        }

    }

    @Override
    public ArrayList<UserProfile> loadInBackground() {
        String all_friend = SaveFile.getDataFromSharedPreference(getContext(), MyApplication.ALL_FRIENDS_OBJECT, "");
        //Log.e("SAURABH",all_friend);
        if (!all_friend.equals("")) {

            ArrayList<UserProfile> userProfiles = new Gson().fromJson(all_friend, Wrapper.class).userProfiles;
            //Log.e("SAURABH",wrapper.userProfiles+"");

            return userProfiles;
        }
        return null;
    }
    @Override
    public void deliverResult(ArrayList<UserProfile> data) {
        super.deliverResult(data);
        cached=data;

    }

    @Override
    protected void onReset() {
        super.onReset();
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    };
}
