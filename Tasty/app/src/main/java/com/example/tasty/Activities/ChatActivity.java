package com.example.tasty.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tasty.Components.MessageAdapter;
import com.example.tasty.Models.Message;
import com.example.tasty.R;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity{

    private RecyclerView rVMessagesView;
    private RecyclerView.Adapter mAdapter;

    private List<Message> mMessages = new ArrayList<Message>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setVariables();
    }

    private void setVariables(){

        rVMessagesView = (RecyclerView)findViewById(R.id.messages);
        rVMessagesView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MessageAdapter(mMessages);

        rVMessagesView.setAdapter(mAdapter);
    }

}
