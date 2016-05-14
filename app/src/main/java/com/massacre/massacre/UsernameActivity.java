package com.massacre.massacre;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UsernameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Enter Your Name");
        setSupportActionBar(toolbar);
        Button cancelButton=(Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Button saveButton=(Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(MyApplication.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
                EditText editText=(EditText)findViewById(R.id.username_et);
                String username=editText.getText().toString();
                if(username!=""||!username.equals("")) {
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString(MyApplication.USERNAME,username);
                    editor.commit();
                    onBackPressed();
                }
            }
        });
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(MyApplication.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
        EditText editText=(EditText)findViewById(R.id.username_et);
        editText.setText(sharedPreferences.getString(MyApplication.USERNAME,""));
    }

}
