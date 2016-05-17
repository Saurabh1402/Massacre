package com.massacre.massacre;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by saurabh on 15/5/16.
 */
public class ChatMessageHolderAdapter extends RecyclerView.Adapter<ChatMessageHolderAdapter.ChatHolder> {
    public static final int TYPE_MESSAGE_SEND=1;
    public static final int TYPE_MESSAGE_RECEIVED=2;
    Context context;
    ArrayList<Message> messageList;
    public ChatMessageHolderAdapter(Context context, ArrayList<Message> messageList){
        this.context=context;
        this.messageList=messageList;
    }
    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_MESSAGE_SEND){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.send_message_container,parent,false);
            ChatMessageHolderAdapter.ChatHolder myHolder=new ChatHolder(view,viewType,context);
            return myHolder;
        }else if(viewType==TYPE_MESSAGE_RECEIVED){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message_container,parent,false);
            ChatMessageHolderAdapter.ChatHolder myHolder=new ChatMessageHolderAdapter.ChatHolder(view,viewType,context);
            return myHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        if(holder.holderId==TYPE_MESSAGE_SEND){
            Message message=messageList.get(position);
            holder.messageTv.setText(message.getMessage());
            String time=MyApplication.changeDateFormat(message.getTime());
            holder.dateTv.setText(time);
            //Log.e("SAURABH","messageId: "+message.getMessageId());
            //holder.messageIdTv.setText(message.getMessageId());
            if(message.getSendOrReceived()==ChatDbHelper.SEND_MESSAGE)
                holder.pendingOrSendIv.setImageResource(R.drawable.ic_done_black_36dp);
            else if(message.getSendOrReceived()==ChatDbHelper.PENDING_MESSAGE)
                holder.pendingOrSendIv.setImageResource(R.drawable.ic_hourglass_empty_black_36dp);

        }
        else if (holder.holderId==TYPE_MESSAGE_RECEIVED){
            Message message=messageList.get(position);
            holder.messageTv.setText(message.getMessage());
            String time=MyApplication.changeDateFormat(message.getTime());
            holder.dateTv.setText(time);
        }
    }

    public static class ChatHolder extends RecyclerView.ViewHolder{
        public int holderId;
        public TextView messageTv;
        public TextView dateTv;
        public TextView messageIdTv;
        public ImageView pendingOrSendIv;
        public ChatHolder( View itemView, int viewType,Context context){
            super(itemView);
            if(viewType==TYPE_MESSAGE_SEND) {
                holderId = viewType;
                messageTv=(TextView)itemView.findViewById(R.id.send_message_container_message);
                dateTv=(TextView)itemView.findViewById(R.id.send_message_container_time);
                pendingOrSendIv=(ImageView)itemView.findViewById(R.id.pending_or_sent_imageView);
                messageIdTv=(TextView)itemView.findViewById(R.id.send_messageId_text_view);
            }else if(viewType==TYPE_MESSAGE_RECEIVED){
                holderId=viewType;
                messageTv=(TextView)itemView.findViewById(R.id.received_message_container_message);
                dateTv=(TextView)itemView.findViewById(R.id.received_message_container_time);
                messageIdTv=(TextView)itemView.findViewById(R.id.received_messageId_text_view);
            }

        }
    }
    @Override
    public int getItemCount() {

//        Log.e("SAURABH",messageList.size()+" items");

        return messageList.size();

    }

    @Override
    public int getItemViewType(int position) {
        Message message=messageList.get(position);
        if(message.getSendOrReceived()==ChatDbHelper.PENDING_MESSAGE|| message.getSendOrReceived()==ChatDbHelper.SEND_MESSAGE){
            //Log.e("SAURABH","RIGHT");
            return TYPE_MESSAGE_SEND;
        }else if(message.getSendOrReceived()==ChatDbHelper.RECEIVED_MESSAGE|| message.getSendOrReceived()==ChatDbHelper.READ_MESSAGE){
            //Log.e("SAURABH","Left");
            return TYPE_MESSAGE_RECEIVED;
        }
        return TYPE_MESSAGE_SEND;
    }
    public void swapData(ArrayList<Message> data){
        this.messageList.clear();
        messageList.addAll(data);
        notifyDataSetChanged();
    }
}
