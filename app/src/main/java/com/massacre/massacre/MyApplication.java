package com.massacre.massacre;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.backendless.Backendless;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Created by saurabh on 11/4/16.
 */
public class MyApplication extends Application{
    public static final String SHARED_PREFERENCE_NAME="Massacre";
    public static final String USERNAME="USERNAME";
    public static final String COUNTRY_CODE="CountryCode";
    public static final String PHONE_NUMBER="PhoneNumber";
    public static final String GCM_SENDER_ID="284759704693";
    public static final String APPLICATION_SECRET_KEY_BACKENDLESS="2A109822-B560-B2AC-FF08-102DDD12EA00";
    public static final String ANDROID_SECRET_KEY_BACKENDLESS="80002E80-F512-35F5-FF21-7BAF360D0B00";
    public static final String APPLICATION_VERSION_BACKENDLESS="v1";
    public static final String MY_PROFILE_OBJECT="MyProfileObject";
    public static final String ALL_FRIENDS_OBJECT="AllFriendObject";
    public static final String MY_DEVICE_ID="MyDeviceId";
    public static final String USER_STATUS="UserStatus";
    public static final String REGISTERED="Registered";
    public static final int QUALITY_PROFILE_PICTURE=75;
    public static final int QUALITY_THUMBNAIL=10;
    public static final String FilesFolderBackendless="https://api.backendless.com"+"/"+
            MyApplication.APPLICATION_SECRET_KEY_BACKENDLESS+"/"+
            MyApplication.APPLICATION_VERSION_BACKENDLESS+"/"+
            "files";
    public static final String PROFILE_PICTURE_FOLDER_BACKENDLESS="ProfilePicture";
    public static final String THUMBNAIL_PICTURE_FOLDER_BACKENDLESS="ProfilePicture/thumbnail";


    public static String getInternalAPPFolder(Context context){
        return context.getFilesDir()+"/Massacre";
    }
    public static String getExternalAPPFolder(){
        return Environment.getExternalStorageDirectory()+"/Massacre";
    }
    public static String getFriendsProfilFolder(Context context){
        return "Profile Picture";
    }
    public static String getThumbnailFriendsProfileFolder(Context context){
        return "Profile Picture/Thumbnail";
    }

    public static Date getExpirationDate(){
        //Calendar cal=new GregorianCalendar(2099,12,31);
        Date date=new Date();
        date.setTime(4081755976000L);
        return date;
    }
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
        /*try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }*/
    }
    public static String changeDateFormat(Date date){
        String format="dd MMM, h:mm a";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
    public static String getDeviceId(Context context,String phoneNumber){
        String allFriendString=SaveFile.getDataFromSharedPreference(context,MyApplication.ALL_FRIENDS_OBJECT,"");
        if(!allFriendString.equals("")){
            Wrapper wrapper=new Gson().fromJson(allFriendString,Wrapper.class);
            if(wrapper!=null && wrapper.userProfiles!=null){
            ArrayList<UserProfile> userProfiles=wrapper.userProfiles;
                Iterator<UserProfile> it=userProfiles.iterator();
                do{
                    UserProfile userProfile=it.next();
//                    Log.e("SAURABH",userProfile.getContact());
                    if(userProfile.getContact().equals(phoneNumber))
                        return userProfile.getDevice_id();
                }while (it.hasNext());
            }
        }
        return null;
    }

}
