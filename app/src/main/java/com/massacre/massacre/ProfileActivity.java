package com.massacre.massacre;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class ProfileActivity extends AppCompatActivity {
    private static final int REMOVE_PHOTO = 3;
    public final int SELECT_PHOTO_FROM_GALLERY =1;
    private final int REQUEST_IMAGE_CAPTURE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Set Title and logo to Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);




        //Go to next Activity
        FabSpeedDial fab = (FabSpeedDial) findViewById(R.id.action_next);
        if (fab != null) {
            fab.setMenuListener(new SimpleMenuListenerAdapter(){
                @Override
                public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                    String username=SaveFile.getDataFromSharedPreference(ProfileActivity.this,MyApplication.USERNAME,"");
                    if(username==""||username.equals("")){
                        Toast.makeText(ProfileActivity.this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(ProfileActivity.this, ContactActivity.class);
                        startActivity(intent);
                    }
                    return false;
                }

                @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    return false;
                }
            });
        }
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=SaveFile.getDataFromSharedPreference(ProfileActivity.this,MyApplication.USERNAME,"");
                if(username==""||username.equals("")){
                   Toast.makeText(ProfileActivity.this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(ProfileActivity.this,ContactActivity.class);
                    startActivity(intent);
                }
            }
        });*/

        //Go to username UpdateActivity
        ImageButton usernameUpdateButton=(ImageButton)findViewById(R.id.username_update_button);
        //CHECK INTERNET IS AVAILABLE OR NOT
        usernameUpdateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,UsernameActivity.class);
                startActivity(intent);

            }
        });



        //Clip the ImageButton of Profile Picture
        //CircleImageView profile_pic=(CircleImageView) findViewById(R.id.profile_pic);
        /*profile_pic.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        profile_pic.setClipToOutline(true);
*/

        //Image Choose for updating profile Image
        FabSpeedDial update_pic=(FabSpeedDial) findViewById(R.id.update_profile);
        update_pic.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return super.onPrepareMenu(navigationMenu);
            }

            @Override
            public void onMenuClosed() {
                super.onMenuClosed();
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.choose_from_gallery){
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    ProfileActivity.this.startActivityForResult(intent, SELECT_PHOTO_FROM_GALLERY);
                    return true;
                }
                else if(menuItem.getItemId()==R.id.choose_from_camera){
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ProfileActivity.this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                    return true;
                }
                else if(menuItem.getItemId()==R.id.remove_photo){

                }
                return false;
            }
        });

        /*assert update_pic != null;
        update_pic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image*//*");
                startActivityForResult(intent,SELECT_PHOTO_FROM_GALLERY);
            }
        });

*/
        String fileName=SaveFile.getDataFromSharedPreference(getApplicationContext(),MyApplication.COUNTRY_CODE,"")+
                        SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.PHONE_NUMBER,"")+
                ".jpg";

        String pathName=MyApplication.getExternalAPPFolder()+"/";
        SaveFile.setImage(this,R.id.profile_pic,pathName,fileName);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String username=SaveFile.getDataFromSharedPreference(this,MyApplication.USERNAME,"");
        TextView usernameTv=(TextView)findViewById(R.id.username_tv);
        usernameTv.setText(username);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_PHOTO_FROM_GALLERY:
                if(resultCode==RESULT_OK){
                    String fileName=SaveFile.getDataFromSharedPreference(getApplicationContext(),MyApplication.COUNTRY_CODE,"")+
                            SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.PHONE_NUMBER,"")+
                            ".jpg";
                    String pathName=MyApplication.getExternalAPPFolder();
                    final Uri imageUri=data.getData();
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap image= BitmapFactory.decodeStream(imageStream);
                        CircleImageView profile_pic=(CircleImageView) findViewById(R.id.profile_pic);
                        image=Bitmap.createScaledBitmap(image,640,640,true);
                        SaveFile.uploadImageToBackendless(ProfileActivity.this,profile_pic,image,MyApplication.QUALITY_PROFILE_PICTURE,fileName,"ProfilePicture",pathName,fileName);;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap image = (Bitmap) extras.get("data");
                    String fileName=SaveFile.getDataFromSharedPreference(getApplicationContext(),MyApplication.COUNTRY_CODE,"")+
                            SaveFile.getDataFromSharedPreference(getBaseContext(),MyApplication.PHONE_NUMBER,"")+
                            ".jpg";
                    String pathName=MyApplication.getExternalAPPFolder();
                    CircleImageView profile_pic=(CircleImageView)findViewById(R.id.profile_pic);
                    image=Bitmap.createScaledBitmap(image,640,640,true);
                    SaveFile.uploadImageToBackendless(ProfileActivity.this,
                            profile_pic,image,MyApplication.QUALITY_PROFILE_PICTURE,
                            fileName, "ProfilePicture", pathName+"/", fileName);;



                }

                break;
            case REMOVE_PHOTO:
                    break;
        }
    }
}
