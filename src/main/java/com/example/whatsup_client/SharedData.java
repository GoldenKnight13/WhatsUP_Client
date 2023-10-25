package com.example.whatsup_client;

import java.io.IOException;
import java.net.Socket;


//This class helps to share data between windows
public class SharedData {

    private final String serverIP = "localhost";    //The server IP
    private final int serverPort = 1000;    //The port of the IP in which the server is running
    public Client myClient; //A unique client for the entire app
    public ChatWindowController chatWindowController;   //Allows to control the ChatWindow from other windows
    public EncryptionTool encryptionTool;   //Needed to encrypt messages along the app
    public String myMessage;




    //The class constructor, receives the name for the new client
    public SharedData(String username){
        try{
            //Creates a new client, unique for the app
            myClient = new Client(username, new Socket(serverIP, serverPort), this);
            myClient.sendName(username);

            //Creates an encryption tool for the app, else it will not allow us to use its methods
            this.encryptionTool = new EncryptionTool();

        //If an error occurs, the client is not created and prints a message that let us know it
        } catch (IOException e){
            System.out.println("Error creating client");
        }
    }

    //Allows us to kill the client when needed
    public void closeClient(){
        myClient.stopClient();
    }

    //Sets an universal ChatWindow controller for the app
    public void setChatWindowController(ChatWindowController chatWindowController){
        this.chatWindowController = chatWindowController;
    }

    //Sets the message to send f
    public void setMyMessage(String myMessage) {
        this.myMessage = myMessage;
    }

}