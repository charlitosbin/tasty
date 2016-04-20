package com.example.restaurantui.Components;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.restaurantui.Models.Message;
import com.example.restaurantui.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;

    public MessageAdapter(List<Message> messages){
        mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_message, parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setTextSide(message.getRemoteMessage());
        viewHolder.setMessage(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mMessageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
        }

        public void setMessage(String message){
            if(null == mMessageView) return;
            if(null == message) return;

            mMessageView.setText(message);

        }

        public void setTextSide(boolean remoteMessage){
            if(remoteMessage){
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                param.gravity = Gravity.RIGHT;
                mMessageView.setLayoutParams(param);
                mMessageView.setGravity(Gravity.RIGHT);
            }
        }
    }
}
