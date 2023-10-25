package com.example.whatsup_client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    //Server important information
    private Socket socket;  //The socket it will use to connect to the server
    public String name; //The username
    private BufferedReader bufferedReader;  //Allows us to receive messages
    private BufferedWriter bufferedWriter;  //Allows us to send messages
    public ArrayList<String> onlineFriends = new ArrayList<>(); //Keeps the
    private Thread listener; //Help us to listen for new messages without interrupting the server connection

    private SharedData sharedData;


    //The client constructor, receives the username and a socket
    public Client(String name, Socket socket, SharedData sharedData){
        this.name = name;   //Set the client name to the received name
        this.sharedData = sharedData;
        try{
            //Creates the connection to the server and the ways to communicate with it
            this.socket = socket;
            this.bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream()));
        } catch (IOException e){
            stopClient();
        }
    }

    //Allows the server to know we are online
    public void sendName(String name){
        try{
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //Sends the user message to the server, that redirects it to the desired friend
    public void sendMessage(String friendIP, String message){
        try{
            bufferedWriter.write( friendIP + ";" + message);    //Creates a message containing our message and who is receiving it
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //Allows us to listen to the server messages
    public void receiveMessage(SelectChatController chatController, SharedData sharedData){

        //Specifies the process the thread is gonna to be doing
        listener = new Thread( () -> {

            //Runs the process while the client is still connected to the server
            while(socket.isConnected()) {

                try {
                    //Receives a message from the server
                    String receivedMessage = bufferedReader.readLine();

                    //If it's not receiving the 'sending friends list' code
                    if (!receivedMessage.equals("200")) {

                        String[] parts = receivedMessage.split(";", 2);

                        //Creates a new container for the message
                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.setPadding(new Insets(5, 5, 5, 5));

                        if( parts[0].equals("300") ){

                            showReceivedMessage(parts[1], hBox);

                        } else if (parts[0].equals("301")){

                            showReceivedMessage(parts[1], hBox);

                            Button decryptMessageButton = new Button("Decrypt message");
                            decryptMessageButton.setOnAction( actionEvent -> {

                                sharedData.setMyMessage("201;" + parts[1]);
                                keyWindow();

                            });
                            hBox.getChildren().add(decryptMessageButton);
                        } else if (parts[0].equals("302")){

                            showReceivedMessage(parts[1], hBox);

                            Button decryptMessageButton = new Button("Decrypt message");
                            decryptMessageButton.setOnAction( actionEvent -> {

                                sharedData.setMyMessage("202;" + parts[1]);
                                keyWindow();

                            });
                            hBox.getChildren().add(decryptMessageButton);

                        } else if(parts[0].equals("303")){

                            String[] signature = parts[1].split(";", 2);
                            showReceivedMessage(signature[1], hBox);

                            Button decryptMessageButton = new Button("Validate signature");
                            decryptMessageButton.setOnAction( actionEvent -> {

                                sharedData.setMyMessage("203;" + parts[1]);
                                keyWindow();

                            });
                            hBox.getChildren().add(decryptMessageButton);


                        } else if(parts[0].equals("304")){

                            showReceivedMessage("You received an envelope!!!", hBox);

                            Button validateEnvelope = new Button("Show envelope content");
                            validateEnvelope.setOnAction( actionEvent -> {

                                sharedData.setMyMessage("204;" + parts[1]);
                                envelopeKeyWindow();

                            });
                            hBox.getChildren().add(validateEnvelope);
                        }




                        Platform.runLater(() -> {
                            sharedData.chatWindowController.vbox_messages.getChildren().add(hBox);
                        });



                        //Else it will add the friends
                    } else {
                        onlineFriends.clear();
                        while (true) {
                            receivedMessage = bufferedReader.readLine();
                            if (!receivedMessage.equals("201")) {
                                onlineFriends.add(receivedMessage);
                            } else {
                                chatController.loadFriends();
                                break;
                            }
                        }

                    }

                } catch (IOException e) {
                    System.out.println("Error");
                    stopClient();
                    break;
                }
            }
        });
        listener.start();   //Starts the listener process

    }

    //Kills the client by closing the socket is uses to connect to the server
    public void stopClient(){

        try {

            //Tries to close the connection, along with the data input and output
            if ( socket.isConnected() ){ socket.close(); }
            if (bufferedReader != null) { bufferedReader.close(); }
            if (bufferedWriter != null) { bufferedWriter.close();}

        //If it cannot stop it, an error message is sent
        } catch (IOException e){
            System.out.println("Error");
        }
    }

    private void showReceivedMessage(String message, HBox container){
        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" + "-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 5, 5, 5));
        container.getChildren().add(textFlow);
    }

    private void keyWindow(){

        try {
            FXMLLoader keyInputLoader = new FXMLLoader(getClass().getResource("KeyInputWindow.fxml"));
            Parent keyInputWindow = keyInputLoader.load();
            KeyInputWindowController  keyInputWindowController = keyInputLoader.getController();
            keyInputWindowController.setSharedData(sharedData);

            Stage stage = new Stage();
            stage.setTitle("WhatsUP");
            stage.setScene(new Scene(keyInputWindow, 210, 130));
            stage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void envelopeKeyWindow(){
        try {

            FXMLLoader envelopeKeysLoader = new FXMLLoader(getClass().getResource("EnvelopeKeysWindow.fxml"));
            Parent envelopeKeysWindow = envelopeKeysLoader.load();
            EnvelopeKeysController envelopeKeysController = envelopeKeysLoader.getController();
            envelopeKeysController.setSharedData(this.sharedData);

            Stage stage = new Stage();
            stage.setTitle("WhatsUP");
            stage.setScene(new Scene(envelopeKeysWindow, 300, 175));
            stage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }


}