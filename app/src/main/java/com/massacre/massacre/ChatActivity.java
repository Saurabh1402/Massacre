package com.massacre.massacre;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity{
    public static String USER_PROFILE="USER_PROFILE";
    public UserProfile userProfile;
    public ChatMessageHolderAdapter adapter;
    RecyclerView recyclerView;
    public static ArrayList<Message> messageList;
    public final int MESSAGE_LOADER=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent=getIntent();
        String userProfileString=intent.getStringExtra(USER_PROFILE);
//        Log.e("SAURABH",userProfileString);
        userProfile=new Gson().fromJson(userProfileString,UserProfile.class);
//        Log.e("SAURABH",userProfile.getContact());
        if(userProfile!=null) {
            String contactName=SaveFile.getDataFromSharedPreference(getBaseContext(), userProfile.getContact(), "");
//          Log.e("SAURABH","NAME:  "+contactName);
            LinearLayout ll=(LinearLayout)toolbar.findViewById(R.id.back_button_chat_activity);
            if(ll!=null)
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            ((TextView)toolbar.findViewById(R.id.name_activty_chat)).setText(contactName);
            Bitmap arrow_Back=BitmapFactory.decodeResource(getResources(),R.drawable.ic_arrow_back_black_48dp);
            ((TextView)toolbar.findViewById(R.id.last_seen_activity_chat)).setText(getResources().getText(R.string.last_seen)+ MyApplication.changeDateFormat(userProfile.getLast_seen()));
            CircleImageView circleImageView=(CircleImageView)toolbar.findViewById(R.id.chat_profile_pic);
            String pathName=MyApplication.getExternalAPPFolder()+"/"+MyApplication.getThumbnailFriendsProfileFolder(getBaseContext())+"/";
            String fileName=userProfile.getContact()+".jpg";
//            Log.e("SAURABH",pathName+fileName);
            Bitmap bitmap=BitmapFactory.decodeFile(pathName+fileName);
            if(bitmap==null)
                bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ic_account_circle_white_48dp);

            circleImageView.setImageBitmap(bitmap);

        }
        setSupportActionBar(toolbar);




        final ChatDbHelper chatDbHelper=new ChatDbHelper(this);
        final EditText messageField=(EditText)findViewById(R.id.message_field_chat_activity);


        FloatingActionButton sendButton=(FloatingActionButton)findViewById(R.id.action_send_message);
        if(sendButton!=null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String message = messageField.getText().toString();
                    if (message != null && !message.equals("")) {
                        Log.e("MESSAGE", message);
                        //String senderPhoneNumber=SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.COUNTRY_CODE,"")+SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.PHONE_NUMBER,"");
                        boolean insertedMessage=chatDbHelper.insertMessage(message, userProfile.getContact(), new Date(), ChatDbHelper.PENDING_MESSAGE, ChatDbHelper.TEXT_MESSAGE);
                        messageField.setText("");
                        //BroadCasting to Chat Message
                        if(insertedMessage) {
                            Intent broadcastMessage = new Intent(MessageLoader.MESSAGE_LISTENER_INTENT_FILTER_STRING);

                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(broadcastMessage);
                        }

                    }
                }
            });
        }
        final Intent messagingService = new Intent(getBaseContext(), MessagingService.class);
        startService(messagingService);
//        new AsyncTask<Void,Void,Void>(){
//            @Override
//            protected Void doInBackground(Void... voids) {
//                final Intent messagingService = new Intent(getBaseContext(), MessagingService.class);
//
//                final PendingIntent pending = PendingIntent.getService(getBaseContext(), 0,messagingService , 0);
//                final AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                alarm.cancel(pending);
//                long interval = 1000;//milliseconds
//                alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//
//            }
//        }.execute();



        adapter=new ChatMessageHolderAdapter(getBaseContext(),new ArrayList<Message>());
        recyclerView=(RecyclerView)findViewById(R.id.chat_message_recycler_view);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager ll=new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(ll);

        //loadMessage(chatDbHelper);
        getSupportLoaderManager().initLoader(MESSAGE_LOADER,null,loaderCallbacks);
    }


    private LoaderManager.LoaderCallbacks<ArrayList<Message>> loaderCallbacks=new LoaderManager.LoaderCallbacks<ArrayList<Message>>() {
        @Override
        public Loader<ArrayList<Message>> onCreateLoader(int id, Bundle args) {
            if(id==MESSAGE_LOADER) {
                Intent intent = getIntent();
                String userProfileString = intent.getStringExtra(USER_PROFILE);
    //        Log.e("SAURABH",userProfileString);
                UserProfile userProfile1 = new Gson().fromJson(userProfileString, UserProfile.class);
    //
                Loader<ArrayList<Message>> loader = new MessageLoader(getApplicationContext(), userProfile1);
                return loader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Message>> loader, ArrayList<Message> data) {
                adapter.swapData(data);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Message>> loader) {
            adapter.swapData(new ArrayList<Message>());
        }
    };
}
