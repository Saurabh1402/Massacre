package com.massacre.massacre;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saurabh on 29/4/16.
 */
public class LoadFriendsProfileAdapter extends RecyclerView.Adapter<LoadFriendsProfileAdapter.MyHolder> {
    public static final int TYPE_REGISTERED_ON_MASSACRE=1;
    public static final int TYPE_NOT_REGISTERED_ON_MASSACRE=0;

    public ArrayList<UserProfile> list;
    public Context context;

    public LoadFriendsProfileAdapter(ArrayList<UserProfile> list,Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_REGISTERED_ON_MASSACRE){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_friend_all_contact,parent,false);
            //view.setBackground(context.getDrawable(R.drawable.ripple_background));
            LoadFriendsProfileAdapter.MyHolder myHolder=new LoadFriendsProfileAdapter.MyHolder(view,viewType,context);
            return myHolder;
        }
        else if(viewType==TYPE_NOT_REGISTERED_ON_MASSACRE){

        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(holder.holderId==TYPE_REGISTERED_ON_MASSACRE){
            final UserProfile userProfile = list.get(position);
            holder.friend_status.setText(userProfile.getUser_status());
            String s=SaveFile.getDataFromSharedPreference(context,userProfile.getContact(),userProfile.getContact());
            holder.friend_name.setText(s);
            String pathName=MyApplication.getExternalAPPFolder()+"/"+MyApplication.getThumbnailFriendsProfileFolder(context)+"/";
            String filName=userProfile.getContact()+".jpg";
            final Bitmap bitmap=SaveFile.getImageFromMobile(pathName,filName);
            if(bitmap!=null)
            holder.imageView.setImageBitmap(bitmap);
            holder.user_profile.setText(new Gson().toJson(userProfile));
            /*holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                   Toast.makeText(context,userProfile.getContact(),Toast.LENGTH_SHORT).show();
                   Intent intent=new Intent(context,ChatActivity.class);
                   intent.putExtra("UserProfile",new Gson().toJson(userProfile));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(intent);
                }
            });*/
            holder.imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"fsdfsd",Toast.LENGTH_SHORT).show();
                }
            });
        }else if(holder.holderId==TYPE_NOT_REGISTERED_ON_MASSACRE){

        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_REGISTERED_ON_MASSACRE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        public int holderId;
        public CircleImageView imageView;
        public TextView friend_name;
        public TextView friend_status;
        public TextView user_profile;

        public MyHolder(View itemView,int viewType,Context context){
            super(itemView);
            if(viewType==TYPE_REGISTERED_ON_MASSACRE){
                holderId=TYPE_REGISTERED_ON_MASSACRE;
                imageView=(CircleImageView)itemView.findViewById(R.id.all_friend_profile);
                friend_name=(TextView)itemView.findViewById(R.id.all_friend_name);
                friend_status=(TextView)itemView.findViewById(R.id.all_friend_status);
                user_profile=(TextView)itemView.findViewById(R.id.user_profile);

            }else if(viewType==TYPE_NOT_REGISTERED_ON_MASSACRE){
                holderId=viewType;
            }

        }


    }
    public void swapData(ArrayList<UserProfile> data){
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }
}

