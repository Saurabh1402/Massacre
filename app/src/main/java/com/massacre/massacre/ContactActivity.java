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
    static LoadFriendsProfileAdapter adapter;
    static ArrayList<UserProfile> userProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*final Intent service = new Intent(getApplicationContext(), MessagingService.class);
        final PendingIntent pending = PendingIntent.getService(getApplicationContext(), 0, service, 0);
        final AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        long interval = 10*1000;//milliseconds
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
        */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChatDbHelper chatDbHelper=new ChatDbHelper(ContactActivity.this);

                Log.e("Saurabh",chatDbHelper.deleteAllMessages()+"");


                /*DeliveryOptions  deliveroption = new DeliveryOptions();
                deliveroption.setPushPolicy(PushPolicyEnum.ONLY);
                deliveroption.addPushSinglecast("f89ade4b");//9936851c  f89ade4b

                PublishOptions publishOptions = new PublishOptions();
                publishOptions.putHeader("android-ticker-text","Saurabh Vishwakarma");
                publishOptions.putHeader("android-content-title","Push Notification");
                publishOptions.putHeader("android-content-text","sfsdfdsfdsfdsafsdafsdafasdfasdfsafasdfsadfasdf");

                com.massacre.massacre.Message messageObject=new com.massacre.massacre.Message();
                String message="message allsdfdsfs";
                messageObject.setMessage(message);
                messageObject.setRecipient("919450495661");
                messageObject.setTime(new Date());
                messageObject.setType();
                message=new Gson().toJson(messageObject);
                Backendless.Messaging.publish(message, publishOptions, deliveroption,new AsyncCallback<MessageStatus>(){

                    @Override
                    public void handleResponse(MessageStatus messageStatus) {
                        Log.e("SAURABH",messageStatus.toString());
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Log.e("SAURABH",backendlessFault.getMessage());

                    }
                });

*/
                /*Gson gson=new Gson();
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(MyApplication.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

                Wrapper wrapper=gson.fromJson(sharedPreferences.getString("ALLCONTACT",""),Wrapper.class);
                ArrayList<HashMap<String,String>> allContact=wrapper.userProfiles;
                Iterator<HashMap<String,String>> iterator=allContact.iterator();
                do{
                    HashMap<String,String> data=iterator.next();
                    //BackendlessDataQuery
                    Log.e("SAURABH",data.get(MyContact.NAME_OF_CONTACT)+"  "+data.get(MyContact.PHONE_NUMBER_OF_CONTACT));
                }while(iterator.hasNext());*/
            }
        });


        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.all_friend_recycler_view);
        String all_friend=SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.ALL_FRIENDS_OBJECT,"");
        //Log.e("SAURABH",all_friend);
        if(!all_friend.equals("")) {

            userProfiles=new Gson().fromJson(all_friend,Wrapper.class).userProfiles;
            //Log.e("SAURABH",wrapper.userProfiles+"");
            if(userProfiles!=null) {
                adapter = new LoadFriendsProfileAdapter(userProfiles, getBaseContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager ll=new LinearLayoutManager(this);
                recyclerView.setLayoutManager(ll);
                recyclerViewTouchHandle(recyclerView);

            }
        }

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
                    Log.e("SAURABH",userProfileTv.getText().toString());
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



}
