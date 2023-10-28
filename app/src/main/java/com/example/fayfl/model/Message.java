package com.example.fayfl.model;

import java.util.Date;

public class Message {

    public String phoneNumber;
    public String textMessage;
    private long messageTime;

    public Message(){}
    public Message(String phoneNumber, String textMessage){
        this.phoneNumber = phoneNumber;
        this.textMessage = textMessage;
        this.messageTime = new Date().getTime();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
