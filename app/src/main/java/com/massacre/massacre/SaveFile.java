package com.massacre.massacre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PushPolicyEnum;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurabh on 23/4/16.
 */
public class SaveFile {
    public static void saveImage(Bitmap image,int quality,String pathName,String fileName){
        try {

            // GET YOUR CONTACT NO

            File direct = new File(pathName);

            if (!direct.exists()) {
                File wallpaperDirectory = new File(pathName);
                wallpaperDirectory.mkdirs();

                //Log.e("SAURABH",pathName);
            }
            File file=new File(new File(pathName),fileName);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fout=new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,quality,fout);
            fout.flush();
            fout.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setImage(Activity activity,int resourceId,String pathName,String fileName){

        File file=new File(new File(pathName),fileName);
        if(file.exists()){
            Bitmap bitmap=BitmapFactory.decodeFile(file.getPath());
            CircleImageView profile_pic=(CircleImageView) activity.findViewById(resourceId);
            profile_pic.setImageBitmap(bitmap);
        }
    }
    public static void uploadImageToBackendless(final Activity activity,final CircleImageView imageButton, final Bitmap image, int quality,final String remoteName,final String remotePath, final String pathName, final String fileName){
        try {
            Backendless.Files.Android.upload(image, Bitmap.CompressFormat.JPEG,quality, remoteName, remotePath,true, new AsyncCallback<BackendlessFile>() {

                @Override
                public void handleResponse(BackendlessFile backendlessFile) {
                    Backendless.Files.Android.upload(image, Bitmap.CompressFormat.JPEG,MyApplication.QUALITY_THUMBNAIL, remoteName, remotePath+"/thumbnail/",true, new AsyncCallback<BackendlessFile>(){

                        @Override
                        public void handleResponse(BackendlessFile backendlessFile) {

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });
                    saveImage(image,MyApplication.QUALITY_PROFILE_PICTURE,pathName,fileName);

                    imageButton.setImageBitmap(image);
                    UserProfile userProfile=new Gson().fromJson(SaveFile.getDataFromSharedPreference(activity,MyApplication.MY_PROFILE_OBJECT,""),UserProfile.class);
                    userProfile.setProfile_picture_available(true);
                    userProfile.setProfile_picture_last_updated(new Date());
                    Backendless.Persistence.save(userProfile, new AsyncCallback<UserProfile>() {
                        @Override
                        public void handleResponse(UserProfile userProfile) {

                            SaveFile.saveDataToSharedPreference(activity,MyApplication.MY_PROFILE_OBJECT,new Gson().toJson(userProfile));
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });
                    Toast.makeText(activity,"Uploaded",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    Toast.makeText(activity,backendlessFault.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void saveDataToSharedPreference(Context activity,String key,String value){
        SharedPreferences sharedPreferences=activity.getApplicationContext().getSharedPreferences(MyApplication.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }
    public static void saveDataToSharedPreference(Context activity,String key,Boolean value){
        SharedPreferences sharedPreferences=activity.getApplicationContext().getSharedPreferences(MyApplication.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();

    }
    public static String getDataFromSharedPreference(Context activity,String  key,String def){
        SharedPreferences sharedPreferences=activity.getApplicationContext().getSharedPreferences(MyApplication.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,def);
    }
    public static Boolean getDataFromSharedPreference(Context activity,String  key,Boolean def){
        SharedPreferences sharedPreferences=activity.getApplicationContext().getSharedPreferences(MyApplication.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,def);
    }
    public static void updateFriendsObject(Context context){
        Backendless.initApp(context,
                MyApplication.APPLICATION_SECRET_KEY_BACKENDLESS,
                MyApplication.ANDROID_SECRET_KEY_BACKENDLESS,
                MyApplication.APPLICATION_VERSION_BACKENDLESS
        );
        AsyncTask<Context,Void,ArrayList<UserProfile>> arrayListAsyncTask=new AsyncTask<Context,Void ,ArrayList<UserProfile>>(){

            @Override
            protected ArrayList<UserProfile> doInBackground(Context... voids) {
                final Context context=voids[0];
                MyContact myContact=new MyContact();
                ArrayList<HashMap<String,String>> allContact=myContact.getArrayListContact(context);
                Iterator<HashMap<String,String>> iterator=allContact.iterator();
                String query="";
                SaveFile.getDataFromSharedPreference(context,MyApplication.ALL_FRIENDS_OBJECT,"");

                do{
                    HashMap<String,String> data=iterator.next();
                    query+="contact='"+data.get(MyContact.COUNTRYCODE_OF_CONTACT)+data.get(MyContact.PHONE_NUMBER_OF_CONTACT)+"' OR ";
                }while(iterator.hasNext());
                query+="contact='nothing'";
                BackendlessDataQuery dataQuery=new BackendlessDataQuery(query);
                //Log.e("SAURABH","Outside");
                Backendless.Persistence.of(UserProfile.class).find(dataQuery, new AsyncCallback<BackendlessCollection<UserProfile>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<UserProfile> userProfileBackendlessCollection) {
                        String all_friendString=SaveFile.getDataFromSharedPreference(context,MyApplication.ALL_FRIENDS_OBJECT,"");
                        //Log.e("SAURABH","Inside  "+all_friendString);
                        //Log.e("SAURABH",all_friendString);
                        Iterator<UserProfile> it = userProfileBackendlessCollection.getCurrentPage().iterator();
                        ArrayList<UserProfile> tempList=null;
                        ArrayList<UserProfile> friendList=new ArrayList<UserProfile>();
                        Wrapper wrapper;
                        if(!all_friendString.equals("")) {
                            wrapper = new Gson().fromJson(all_friendString, Wrapper.class);
                            if (wrapper.userProfiles != null)
                                tempList = wrapper.userProfiles;
                        }
                        if(!all_friendString.equals("") && (tempList!=null&&tempList.size()!=0)){
                            do{
                                UserProfile remoteUserProfile=it.next();
                                //Log.e("SAURABH","remote user profile"+remoteUserProfile.getContact()+"  "+remoteUserProfile.getProfile_picture_available());
                                boolean flag=false;
                                friendList.add(remoteUserProfile);
                                for (int i=0;i<tempList.size();i++){
                                    UserProfile savedUserProfile=tempList.get(i);
                                    //Log.e("SAURABH",savedUserProfile.getContact()+" "+remoteUserProfile.getContact());
                                    if(savedUserProfile.getContact().equals(remoteUserProfile.getContact())){

                                        //Log.e("SAURABH",savedUserProfile.getProfile_picture_last_updated()+"");
                                        if(remoteUserProfile.getProfile_picture_available()==true){
                                            //Log.e("SAURABH","UDNO"+savedUserProfile.getContact());
                                            if (savedUserProfile.getProfile_picture_last_updated() == null) {
                                                Log.e("SAURABH","last updated:null");
                                                String urlString=MyApplication.FilesFolderBackendless+"/"+MyApplication.THUMBNAIL_PICTURE_FOLDER_BACKENDLESS+"/"+remoteUserProfile.getContact()+".jpg";
                                                String downloadLocation=MyApplication.getExternalAPPFolder() +"/"+ MyApplication.getThumbnailFriendsProfileFolder(context)+"/";
                                                downloadImageFromBackendless(remoteUserProfile,urlString,MyApplication.QUALITY_THUMBNAIL,downloadLocation);
                                            }
                                            else{
                                                if(remoteUserProfile.getProfile_picture_last_updated().after(savedUserProfile.getProfile_picture_last_updated())){
                                                    String urlString=MyApplication.FilesFolderBackendless+"/"+MyApplication.THUMBNAIL_PICTURE_FOLDER_BACKENDLESS+"/"+remoteUserProfile.getContact()+".jpg";
                                                    String downloadLocation=MyApplication.getExternalAPPFolder() +"/"+ MyApplication.getThumbnailFriendsProfileFolder(context)+"/";
                                                    downloadImageFromBackendless(remoteUserProfile,urlString,MyApplication.QUALITY_THUMBNAIL,downloadLocation);

                                                }else{
                                                    String downloadLocation=MyApplication.getExternalAPPFolder() +"/"+ MyApplication.getThumbnailFriendsProfileFolder(context)+"/";
                                                    String fileName=savedUserProfile.getContact()+".jpg";
                                                    //Log.e("SAURABH","last updated:not null " +downloadLocation+fileName);
                                                    if(!imageExist(downloadLocation,fileName)){
                                                        //Log.e("SAURABH","last updated:not null and is before");

                                                        String urlString=MyApplication.FilesFolderBackendless+"/"+MyApplication.THUMBNAIL_PICTURE_FOLDER_BACKENDLESS+"/"+remoteUserProfile.getContact()+".jpg";
                                                        downloadImageFromBackendless(remoteUserProfile,urlString,MyApplication.QUALITY_THUMBNAIL,downloadLocation);


                                                    }
                                                }

                                            }
                                        }else {
                                            //Log.e("SAURABH","Delete"+savedUserProfile.getContact()+"  "+savedUserProfile.getProfile_picture_available());

                                            String downloadLocation=MyApplication.getExternalAPPFolder() +"/"+ MyApplication.getThumbnailFriendsProfileFolder(context)+"/";
                                            String fileName=savedUserProfile.getContact()+".jpg";
                                            deleteImage(downloadLocation,fileName);

                                        }
                                        break;
                                    }
                                }
                            }while(it.hasNext());


                        }else {
                            do {
                                UserProfile user = it.next();
                                friendList.add(user);
                                if(user.getProfile_picture_available()){
                                    String urlString=MyApplication.FilesFolderBackendless+"/"+MyApplication.THUMBNAIL_PICTURE_FOLDER_BACKENDLESS+"/"+user.getContact()+".jpg";
                                    String downloadLocation=MyApplication.getExternalAPPFolder() +"/"+ MyApplication.getThumbnailFriendsProfileFolder(context)+"/";
                                    downloadImageFromBackendless(user,urlString,MyApplication.QUALITY_THUMBNAIL,downloadLocation);

                                }
                                //Toast.makeText(context,MyApplication.THUMBNAIL_PICTURE_FOLDER_BACKENDLESS+"/"+user.getContact()+".jpg",Toast.LENGTH_SHORT).show();
                            } while (it.hasNext());


                        }
                        Wrapper wrapper1=new Wrapper(friendList);
                        //BroadCast Data to ContactActivity
                        Intent sendBroadcast=new Intent(AllContactLoader.INTENT_FILTER_ALL_FRIEND);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(sendBroadcast);


                        all_friendString= new Gson().toJson(wrapper1);
                        SaveFile.saveDataToSharedPreference(context,MyApplication.ALL_FRIENDS_OBJECT,all_friendString);


                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });


                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<UserProfile> aVoid) {
                super.onPostExecute(aVoid);

            }
        }.execute(context);
        try {
            ArrayList<UserProfile> userProfiles=arrayListAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void downloadImageFromBackendless(UserProfile user,String urlString,int quality,String downloadLocation){
        try {
            URL url=new URL(urlString);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);

            Log.e("SAURABH DownloadImage",conn.getContentLength()+"  "+user.getContact());
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();

            SaveFile.saveImage(bitmap, quality,downloadLocation , user.getContact() + ".jpg");
            //Toast.makeText(context,"Downloaded",Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void deleteImage(String pathName,String fileName){

        // GET YOUR CONTACT NO

        File direct = new File(pathName);

        if (!direct.exists()) {
            File wallpaperDirectory = new File(pathName);
            wallpaperDirectory.mkdirs();

            //Log.e("SAURABH",pathName);
        }
        File file=new File(new File(pathName),fileName);
        if(file.exists()){
            file.delete();
        }



    }
    public static boolean imageExist(String pathName,String fileName){

        // GET YOUR CONTACT NO

        File direct = new File(pathName);

        if (!direct.exists()) {
            File wallpaperDirectory = new File(pathName);
            wallpaperDirectory.mkdirs();

            //Log.e("SAURABH",pathName);
        }
        File file=new File(new File(pathName),fileName);
        if(file.exists()){
            return true;
        }

        return false;

    }
    public static Bitmap getImageFromMobile(String pathName,String fileName){
        Bitmap bitmap=null;
        File file=new File(new File(pathName),fileName);
        if(file.exists()){
            bitmap=BitmapFactory.decodeFile(file.getPath());
        }
        return bitmap;
    }


}
