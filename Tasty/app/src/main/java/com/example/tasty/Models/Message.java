package com.example.tasty.Models;


public class Message {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    private int mType;
    private String mMessage;

    private Message(){}

    public int getTypeMessage(){
        return mType;
    }

    public String getMessage(){
        return mMessage;
    }

    public static class Builder{
        private final int mType;
        private String mMessage;

        public Builder(int type){
            mType = type;
        }

        public Builder message(String message){
            mMessage = message;
            return this;
        }

        public Message build(){
            Message message = new Message();
            message.mType = mType;
            message.mMessage = mMessage;

            return message;
        }

    }
}
