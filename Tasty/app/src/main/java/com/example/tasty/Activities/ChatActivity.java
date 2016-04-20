package com.example.tasty.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.tasty.Utils.Encryption;
import com.example.tasty.Utils.Util;
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

    private Encryption encryption;
    //22
   // private String ipAddress = "http://192.168.1.67:3000";
    private String ipAddress2 = "http://192.168.0.107:3000";
    private String ipRestaurant = "";

    private List<Message> mMessages = new ArrayList<Message>();
    private String nickname;
    private String restaurantName;

    private Socket socket;
    {
        try{
            socket = IO.socket(ipAddress2);
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        nickname = intent.getExtras().getString(getResources().getString(R.string.nickname_id));
        restaurantName = intent.getExtras().getString(getResources().getString(R.string.restaurant_id));

        try {
            encryption = new Encryption(new byte[16]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.connect();
        socket.emit("init_client", "restaurant_name:"+ restaurantName +","
                +"client:" + Util.getDeviceId(this));
        socket.on(getResources().getString(R.string.server_message), handleIncomingMessages);

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
        Log.d("ANDROID_ID>>>>", Util.getDeviceId(this));
        String message = mInputMessageView.getText().toString().trim();
        if(message != "") {
            message = nickname+": "+message;
            if(encryption != null){
                byte[] encrypted = new byte[0];
                try {
                    encrypted = encryption.encrypt(message.getBytes("UTF-8"));
                    byte[] decrypted = encryption.decrypt(encrypted);
                    Log.d("tag>>", new String(decrypted, "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mInputMessageView.setText("");
            addMessage(message, false);
            socket.emit(getResources().getString(R.string.server_message), message);
        }
    }

    private void addMessage(String message, boolean remoteMessage){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .message(message,remoteMessage).build());
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
            final String message;
            try{
                message = data.getString("message").toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addMessage(message, true);
                    }
                });
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
}
