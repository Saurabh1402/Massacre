package com.massacre.massacre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.DeviceRegistration;
import com.backendless.Persistence;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.async.callback.UploadCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by saurabh on 13/4/16.
 */
public class Register {
    public void registerInBackground(final Activity context){
                EditText countryCode=(EditText)context.findViewById(R.id.country_code);
                String countryCodeString=countryCode.getText().toString();

                // DO COUNTRY CODE VALIDATION... IF INVALID SO ALERT DIALOG

                EditText phoneNumber=(EditText)context.findViewById(R.id.phone_number);
                String phoneNumberString = phoneNumber.getText().toString();

                // Validate Phone Number

                String password= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                //Log.e("Saurabh","password: "+password);


                //Log.e("Saurabh","contact:  "+contact);

                userRegistration(context,countryCodeString,phoneNumberString,password);

                //Move messageRegistration to userRegistration's handleResponse method after debugging
                messageRegistraion(context,countryCodeString,phoneNumberString);











    }
    public void userRegistration(final Activity context, final String countryCode, final String phoneNumber, final String password){
        BackendlessUser user=new BackendlessUser();
        user.setPassword(password);
        user.setProperty("contact",countryCode+phoneNumber);
        Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                Toast.makeText(context, "Registered", Toast.LENGTH_LONG).show();

                //Store COUNTRY CODE AND PHONE NUBMER HERE


            }

            @Override
            public void handleFault(BackendlessFault fault) {
                //super.handleFault(fault);
                //Toast.makeText(context, fault.getMessage(), Toast.LENGTH_LONG).show();

                //Handle Registration
                // => if registered to different device then logout from that device
                // =>Recall userRegistration(context,contact, password);


            }
        });
    }
    public void messageRegistraion(final Activity context, final String countryCode, final String phoneNumber){
        List<String> list=new ArrayList<String>();
        list.add("default");
        Backendless.Messaging.registerDevice(MyApplication.GCM_SENDER_ID, list, MyApplication.getExpirationDate(),new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {
                Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
                    @Override
                    public void handleResponse(DeviceRegistration deviceRegistration) {

                        final String device_id=deviceRegistration.getDeviceId();
                        final UserProfile userProfile=new UserProfile();
                        userProfile.setDevice_id(device_id);
                        userProfile.setContact(countryCode+phoneNumber);
                        userProfile.setLast_seen(new Date());
                        userProfile.setProfile_picture_available(false);
                        userProfile.setUser_status("Hey There! I am using Massacre.");
                        Toast.makeText(context,"Messaging Registered: "+deviceRegistration.getDeviceId(),Toast.LENGTH_SHORT).show();

                        Backendless.Persistence.save(userProfile, new AsyncCallback<UserProfile>() {
                            @Override
                            public void handleResponse(UserProfile userProfile1) {
                                Toast.makeText(context,"Data Saved" , Toast.LENGTH_SHORT).show();
                                // if Registered send to Profile Page
                                // else Show Error Dialog
                                //
                                //
                                // ===>> (Registration should be successful in any case)
                                // save some sharedParameter Registration to true and Phonenumber and Device Id;
                                Gson gson=new Gson();
                                String profileStringObject=gson.toJson(userProfile1);
                                SaveFile.saveDataToSharedPreference(context,MyApplication.MY_PROFILE_OBJECT,profileStringObject);
                                SaveFile.saveDataToSharedPreference(context,MyApplication.MY_DEVICE_ID,device_id);
                                SaveFile.saveDataToSharedPreference(context,MyApplication.COUNTRY_CODE,countryCode);
                                SaveFile.saveDataToSharedPreference(context,MyApplication.PHONE_NUMBER,phoneNumber);
                                SaveFile.saveDataToSharedPreference(context,MyApplication.REGISTERED,true);
                                SaveFile.saveDataToSharedPreference(context,MyApplication.USER_STATUS,"Hey There!I am using Massacre");
                                Intent contactSyncService=new Intent(context,ContactSyncService.class);
                                context.startService(contactSyncService);
                                Intent intent=new Intent(context,ProfileActivity.class);
                                context.startActivity(intent);
                                context.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                Toast.makeText(context,backendlessFault.getMessage()+"Line:135", Toast.LENGTH_LONG).show();

                                BackendlessDataQuery dataQuery=new BackendlessDataQuery("contact='"+countryCode+phoneNumber+"' OR device_id='"+device_id+"'");
                                Backendless.Persistence.of(UserProfile.class).find(dataQuery, new AsyncCallback<BackendlessCollection<UserProfile>>() {
                                    @Override
                                    public void handleResponse(BackendlessCollection<UserProfile> userProfileBackendlessCollection) {
                                        Iterator<UserProfile> it=userProfileBackendlessCollection.getCurrentPage().iterator();
                                        do{
                                            UserProfile temp=it.next();
                                            //Log.e("SAURABH",temp.getContact()+"  "+temp.getDevice_id());
                                            Backendless.Persistence.of(UserProfile.class).remove(temp, new AsyncCallback<Long>() {
                                                @Override
                                                public void handleResponse(Long aLong) {

                                                }

                                                @Override
                                                public void handleFault(BackendlessFault backendlessFault) {

                                                }
                                            });
                                        }while(it.hasNext());
                                        messageRegistraion(context,countryCode,phoneNumber);

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {

                                        Toast.makeText(context, "SaveProfile:"+backendlessFault.getMessage()+"Line:164", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(context,backendlessFault.getMessage()+"Line:174", Toast.LENGTH_LONG).show();

                    }
                });

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context,backendlessFault.getMessage()+"Line:178", Toast.LENGTH_SHORT).show();


            }
        });


    }
}

