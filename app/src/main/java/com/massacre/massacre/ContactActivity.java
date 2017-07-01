package com.massacre.massacre;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.*;
import com.backendless.messaging.Message;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class ContactActivity extends AppCompatActivity {
    LoadFriendsProfileAdapter adapter;
    ArrayList<UserProfile> userProfiles;
    public final int LOAD_ALL_FRIEND=1;
    public RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*
        final Intent service = new Intent(getApplicationContext(), MessagingService.class);
        final PendingIntent pending = PendingIntent.getService(getApplicationContext(), 0, service, 0);
        final AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        long interval = 10*1000;//milliseconds
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
        */
        if(fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent statusActivity=new Intent(ContactActivity.this,StatusActivity.class);
                    startActivity(statusActivity);
//                    ChatDbHelper chatDbHelper = new ChatDbHelper(ContactActivity.this);
//
//                    Log.e("Saurabh", chatDbHelper.deleteAllMessages() + "");
                }
            });
        }

        recyclerView=(RecyclerView)findViewById(R.id.all_friend_recycler_view);
        adapter = new LoadFriendsProfileAdapter(new ArrayList<UserProfile>(), getBaseContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager ll=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(ll);
        recyclerViewTouchHandle(recyclerView);

        getSupportLoaderManager().initLoader(LOAD_ALL_FRIEND,null,loaderCallbacks);

    }
    public void recyclerViewTouchHandle(RecyclerView recyclerView){
        final GestureDetector mGestureDetector=new GestureDetector(ContactActivity.this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });




        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View view=rv.findChildViewUnder(e.getX(),e.getY());
                if(view!=null && mGestureDetector.onTouchEvent(e)){
                    //Toast.makeText(getBaseContext(),"touch Event",Toast.LENGTH_SHORT).show();
                    TextView userProfileTv=(TextView)view.findViewById(R.id.user_profile);
                    //UserProfile userProfile=new Gson().fromJson(userProfileTv.getText().toString(),UserProfile.class);
                    Intent intent=new Intent(getBaseContext(),ChatActivity.class);
//                    Log.e("SAURABH",userProfileTv.getText().toString());
                    intent.putExtra(ChatActivity.USER_PROFILE,userProfileTv.getText());
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {



            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }
    private LoaderManager.LoaderCallbacks<ArrayList<UserProfile>> loaderCallbacks=
            new LoaderManager.LoaderCallbacks<ArrayList<UserProfile>>() {
                @Override
                public Loader<ArrayList<UserProfile>> onCreateLoader(int id, Bundle args) {
                    if(id==LOAD_ALL_FRIEND){
                        return new AllContactLoader(getBaseContext());
                    }
                    return null;
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<UserProfile>> loader, ArrayList<UserProfile> data) {
                    adapter.swapData(data);
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<UserProfile>> loader) {

                }
            };



}
