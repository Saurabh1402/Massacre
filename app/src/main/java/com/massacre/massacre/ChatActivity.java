package com.massacre.massacre;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    public static String USER_PROFILE="USER_PROFILE";
    public static UserProfile userProfile;
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
            Log.e("SAURABH",pathName+fileName);
            Bitmap bitmap=BitmapFactory.decodeFile(pathName+fileName);
            if(bitmap==null)
                bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.ic_account_circle_white_48dp);

            circleImageView.setImageBitmap(bitmap);

        }
        setSupportActionBar(toolbar);
        final ChatDbHelper chatDbHelper=new ChatDbHelper(this);
        final EditText messageField=(EditText)findViewById(R.id.message_field_chat_activity);
        FloatingActionButton sendButton=(FloatingActionButton)findViewById(R.id.action_send_message);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=messageField.getText().toString();
                if(message!=null && !message.equals("")){
                    //String senderPhoneNumber=SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.COUNTRY_CODE,"")+SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.PHONE_NUMBER,"");
                    chatDbHelper.insertMessage(message,userProfile.getContact(),new Date(),ChatDbHelper.PENDING_MESSAGE,ChatDbHelper.TEXT_MESSAGE);
                    messageField.setText("");
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            final Intent messagingService = new Intent(getBaseContext(), MessagingService.class);
                            final PendingIntent pending = PendingIntent.getService(getBaseContext(), 0,messagingService , 0);
                            final AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarm.cancel(pending);
                            long interval = 5*1000;//milliseconds
                            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),interval, pending);return null;
                        }
                    }.execute();
                }
            }
        });

    }

}
