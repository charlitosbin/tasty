package com.example.tasty.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.tasty.Components.MessageAdapter;
import com.example.tasty.Models.Message;
import com.example.tasty.R;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity{

    private RecyclerView rVMessagesView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mInputMessageView;
    private ImageButton msendButton;

    private List<Message> mMessages = new ArrayList<Message>();

    private Socket socket;
    {
        try{
            socket = IO.socket("http://192.168.1.67:3000");
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        socket.connect();
        socket.on(getResources().getString(R.string.server_message),handleIncomingMessages);
        setVariables();
        addEventHandlers();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        socket.disconnect();
    }

    private void addEventHandlers(){
        msendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void setVariables(){

        mInputMessageView = (EditText)findViewById(R.id.message_input);
        msendButton = (ImageButton) findViewById(R.id.send_button);

        rVMessagesView = (RecyclerView)findViewById(R.id.messages);
        rVMessagesView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MessageAdapter(mMessages);
        rVMessagesView.setAdapter(mAdapter);
    }

    private void sendMessage(){
        String message = mInputMessageView.getText().toString().trim();
        mInputMessageView.setText("");
        addMessage(message);
        socket.emit(getResources().getString(R.string.server_message),message);
    }

    private void addMessage(String message){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .message(message).build());
        mAdapter = new MessageAdapter(mMessages);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();

    }

    private void scrollToBottom(){
        rVMessagesView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            JSONObject data = (JSONObject)args[0];
            String message;
            try{
                message = data.getString("text").toString();
                addMessage(message);
            }catch (JSONException e){
                //return;
            }
        }
    };
}
