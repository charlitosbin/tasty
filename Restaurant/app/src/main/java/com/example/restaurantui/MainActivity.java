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

public class MainActivity extends Activity {

    private RecyclerView rVMessagesView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mInputMessageView;
    private ImageButton msendButton;

    private String ipAddress = "http://192.168.1.67:3000";
    //private String ipAddress = "http://192.168.0.107:3000";
    private String clientIpAddress = "";

    private String nickname;

    private List<Message> mMessages = new ArrayList<Message>();

    private Socket clientSocket;

    private Socket serverSocket;
    {
        try{
            serverSocket = IO.socket(ipAddress);
        }catch (URISyntaxException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverSocket.connect();
        serverSocket.emit("init_restaurant", Util.getDeviceId(this));
        serverSocket.on("client", handleIncomingClients);
        serverSocket.on("message", handleIncomingMessages);

        setVariables();
        addEventHandlers();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        serverSocket.disconnect();
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

            //if(clientSocket != null){
            //    clientSocket.emit("message", message);
           // }
            serverSocket.emit("message", message);
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

    private Emitter.Listener handleIncomingClients = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            JSONObject data = (JSONObject)args[0];
            try{
                String ipAndNickName = data.getString("client").toString();
                clientIpAddress = ipAndNickName.split(",")[0];
                nickname = ipAndNickName.split(",")[1];

                if(clientIpAddress != null || clientIpAddress != ""){
                    clientIpAddress = "http://"+ clientIpAddress +":3000";
                    Log.d("restaurantIp>>>", clientIpAddress);
                    Log.d("restaurantName>>>>>>", nickname);
                   // try{
                   //     clientSocket = IO.socket(clientIpAddress);
                   //     clientSocket.connect();
                   //     clientSocket.on("message", handleIncomingMessages);

                       // clientSocket.emit("message", "hola compadre");
                    //}catch (URISyntaxException e){
                    //    throw  new RuntimeException(e);
                   // }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

}
