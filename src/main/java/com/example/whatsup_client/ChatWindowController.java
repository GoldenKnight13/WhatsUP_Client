package com.example.whatsup_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatWindowController implements Initializable {


    //Linking the window components to FXML variables, allowing their manipulation
    @FXML Label Friend_name;
    @FXML Button returnButton;
    @FXML ScrollPane Messages;
    @FXML VBox vbox_messages;
    @FXML Button symmetricButton;
    @FXML Button asymmetricButton;
    @FXML Button signatureButton;
    @FXML Button envelopeButton;
    @FXML TextField message_input;
    @FXML Button sendButton;

    //Class variables
    private SharedData sharedData;  //The shared data of the app
    private String friendName;  //The selected friend name
    private String friendIP;    //Selected friend IP
    private SelectChatController selectChatController;  //Allows us to add new connections even when we are chatting


    //Function that runs when the window is created
    @Override public void initialize(URL location, ResourceBundle resources) {

        //When the data is available, sets the ChatWindowController
        Platform.runLater(()->{
            this.sharedData.setChatWindowController(this);
        });

        //Lets us see the last message automatically without scrolling
        vbox_messages.heightProperty().addListener((observableValue, number, t1) -> {
            Messages.setVvalue( (Double) t1);
        });

        //Return button action
        returnButton.setOnAction(actionEvent -> {

            try {

                //Charges the necessary data to show SelectChatWindow
                FXMLLoader selectChatLoader = new FXMLLoader(getClass().getResource("SelectChatWindow.fxml"));
                Parent selectChatWindow = selectChatLoader.load();
                SelectChatController selectChatController = selectChatLoader.getController();
                selectChatController.setSharedData(sharedData);

                //Creates a new java window and loads SelectCharWindow components
                Stage stage = new Stage();
                stage.setTitle("WhatsUP");
                stage.setScene(new Scene(selectChatWindow, 600, 600));
                stage.show();

            //If an error occurs
            } catch (IOException e){
                e.printStackTrace();
            }

            //Closes the current window anyway
            Stage currentStage = (Stage) returnButton.getScene().getWindow();
            currentStage.close();

        });

        //Symmetric encryption button action
        symmetricButton.setOnAction(actionEvent -> {

            String message = message_input.getText();

            if( !message.isEmpty() ) {
                sharedData.setMyMessage("301;" + message);
                keyWindow();
                showMyMessage(message);
            }

        });

        //Asymmetric encryption button action
        asymmetricButton.setOnAction(actionEvent -> {

            String message = message_input.getText();

            if( !message.isEmpty() ) {
                sharedData.setMyMessage("302;" + message);
                keyWindow();
                showMyMessage(message);
            }

        });

        //Digital signature button action
        signatureButton.setOnAction( actionEvent -> {

            String message = message_input.getText();

            if( !message.isEmpty() ) {
                sharedData.setMyMessage("303;" + message);
                keyWindow();
                showMyMessage(message);
            }

        });

        //Digital envelope button action
        envelopeButton.setOnAction(actionEvent -> {

            String message = message_input.getText();

            if( !message.isEmpty() ) {
                sharedData.setMyMessage("304;" + message);
                envelopeKeyWindow();
                showMyMessage(message);
            }
        });

        //Send message button action
        sendButton.setOnAction( actionEvent -> {

            String message = message_input.getText();   //Gets the user message

            //If the message is not empty
            if( !message.isEmpty() ) {
                sendMessage("300;" + message);
                showMyMessage(message);
            }
        });

    }

    public void sendMessage(String message){
        sharedData.myClient.sendMessage(friendIP, message); //Sends the message to the server, it will redirect it to the selected friend
        message_input.clear();  //Clears the text input
    }

    //Function that allows to load the shared data
    public void setSharedData(SharedData sharedData){ this.sharedData = sharedData; }

    //Function that loads the selected friend info
    public void setFriendInfo(String friendName, String friendIP, SelectChatController chatController){
        this.friendName = friendName;   //Receives the username
        Friend_name.setText(friendName);    //Places the username in the window to let us know who are we texting
        this.friendIP = friendIP;   //Receives the friend IP
        this.selectChatController = chatController; //Allows us to update connections
    }

    //Function that adds the user's message into the chat
    private void showMyMessage(String message){

        //Creates the message container and inserts it in there
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(message);
        text.setFill(Color.WHITE);
        text.setFont(Font.font(12));
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(19,19,208);" + "-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5, 5, 5, 5));

        //Adds it to the chat log
        hBox.getChildren().add(textFlow);
        vbox_messages.getChildren().add(hBox);
    }

    //Function that opens up a window to enter the selected key
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