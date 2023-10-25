package com.example.whatsup_client;

public class CyphredMessage {

    private String myMessage;

    public CyphredMessage(String message){
        this.myMessage = message;
    }

    public void setMessage(String newMessage){
        this.myMessage = newMessage;
    }

    public String getMessage(){
        return this.myMessage;
    }


}
