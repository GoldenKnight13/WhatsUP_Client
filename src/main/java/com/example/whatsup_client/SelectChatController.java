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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectChatController implements Initializable {

    //Connects the window elements with the controller
    @FXML Label myNameLabel;
    @FXML ScrollPane AvailableChats;
    @FXML VBox Friends;
    @FXML Button LogOutButton;

    //Class variables
    private SharedData sharedData;  //Shared data among the different windows


    //Function that runs when the window is created
    public void initialize(URL location, ResourceBundle resources) {

        //When the available data is available
        Platform.runLater( ()->{

            loadFriends();  //Shows the online friends in the window
            sharedData.myClient.receiveMessage(this, this.sharedData);  //Allows to receive the clients list from the server

        });

        //Allows to see the most recent connected client automatically
        Friends.heightProperty().addListener((observableValue, number, t1) -> {
            AvailableChats.setVvalue( (Double) t1);
        });

        //Log Out button action
        LogOutButton.setOnAction(actionEvent -> {
            sharedData.closeClient();   //Deletes the shared data
            Stage currentStage = (Stage) LogOutButton.getScene().getWindow();   //Closes the SelectChat window
            currentStage.close();
        });

    }

    //Function that updates the online friends list
    public void loadFriends(){

        //For every friend online
        for (String friend: this.sharedData.myClient.onlineFriends){

            //Splits the info into the username and its IP
            String[] message = friend.split(";");

            //Creates a new element that contains the friend username
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text( message[1] );
            TextFlow textFlow = new TextFlow(text);
            hBox.getChildren().add(textFlow);

            //Creates a button that allows us to enter the chat of the selected friend
            Button actionButton = new Button("Enter");
            actionButton.setOnAction(actionEvent1 -> {
                enterFriendChat( message[1], message[0]);
            });
            actionButton.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().add(actionButton);

            //Adds the name and the button into the friends list
            Platform.runLater(()-> {
                Friends.getChildren().add(hBox);
            });
        }
    }

    //Charges the shared data into this class
    public void setSharedData(SharedData sharedData){
        this.sharedData = sharedData;
        myNameLabel.setText( sharedData.myClient.name );
    }

    //Function that allows us to enter a friend chat
    public void enterFriendChat(String friendName, String friendIP){

        try {

            //Gets the necessary data to open the ChatWindow
            FXMLLoader chatWindowLoader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
            Parent chatWindow = chatWindowLoader.load();
            ChatWindowController chatWindowController = chatWindowLoader.getController();   //Allows us to see the updates in the online friends list is real time even when we are in a chat
            chatWindowController.setSharedData(sharedData); //Shares the general data
            chatWindowController.setFriendInfo(friendName, friendIP, this);

            //Creates a new java window and loads the ChatWindow
            Stage stage = new Stage();
            stage.setTitle("WhatsUP");
            stage.setScene(new Scene(chatWindow, 500, 500));
            stage.show();

        //If an error occurs, it prints a warning
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Closes the actual window
        Stage currentStage = (Stage) LogOutButton.getScene().getWindow();
        currentStage.close();
    }

}