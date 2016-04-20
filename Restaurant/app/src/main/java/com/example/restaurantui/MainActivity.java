package com.example.restaurantui;

import android.os.Bundle;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.restaurantui.Components.MessageAdapter;
import com.example.restaurantui.Utils.Util;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.restaurantui.Models.Message;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends Activity {

    private RecyclerView rVMessagesView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mInputMessageView;
    private ImageButton msendButton;

    private String ipAddress2 = "http://192.168.0.107:3000";

    private String nickname;

    private List<Message> mMessages = new ArrayList<Message>();

    private Socket socket;
    {
        try{
            socket = IO.socket(ipAddress2);
        }catch (URISyntaxException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socket.connect();
        socket.emit("init_restaurant", "restaurant," + Util.getDeviceId(this));
        socket.on("message", handleIncomingMessages);
        setVariables();
        addEventHandlers();
    }

    private void setVariables(){
        mInputMessageView = (EditText)findViewById(R.id.message_input);
        msendButton = (ImageButton) findViewById(R.id.send_button);

        rVMessagesView = (RecyclerView)findViewById(R.id.messages);
        rVMessagesView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MessageAdapter(mMessages);
        rVMessagesView.setAdapter(mAdapter);

    }

    private void addEventHandlers(){
        msendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage();
            }
        });
    }

    private void sendMessage(){
        Log.d("Android ID>>>>", Util.getDeviceId(this));
        String message = mInputMessageView.getText().toString().trim();
        if(message != "") {
            message = nickname + ": " + message;

            mInputMessageView.setText("");
            addMessage(message, false);
            socket.emit("message", message);
        }
    }

    private void addMessage(String message, boolean remoteMessage){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .message(message, remoteMessage).build());
        mAdapter = new MessageAdapter(mMessages);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();
    }

    private void scrollToBottom(){rVMessagesView.scrollToPosition(mAdapter.getItemCount()-1);}

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
