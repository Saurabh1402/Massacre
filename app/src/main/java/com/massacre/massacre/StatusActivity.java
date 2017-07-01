package com.massacre.massacre;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Status");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"Bam",Toast.LENGTH_SHORT).show();
            }
        });
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }
    public void initialize(){
        ListView listView=(ListView)findViewById(R.id.status_list_view);
        String wrapperString=SaveFile.getDataFromSharedPreference(getBaseContext(),getResources().getString(R.string.your_all_status_shared_preference),"");
        Wrapper wrapper=new Gson().fromJson(wrapperString,Wrapper.class);
        ArrayList<HashMap<String,String>> yourAllString=wrapper.yourAllStatus;
        ListAdapter adapter=new SimpleAdapter(StatusActivity.this,yourAllString,R.layout.status_list_view_entry,new String[]{
                "status"
        },
        new int[]{
           R.id.list_entry_status
        });
        listView.setAdapter(adapter);
    }

}
