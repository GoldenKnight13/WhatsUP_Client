package com.example.whatsup_client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    //Allows us to manipulate and use FXML components
    @FXML Label Message;
    @FXML TextField Username;
    @FXML TextField Password;
    @FXML Button Login_Button;

    //Standard messages
    private final String warning = "You must enter a";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Username.setPromptText("Username");
        Password.setPromptText("Password");

        //If Login_Button is clicked
        Login_Button.setOnAction( actionEvent -> {

            //Checks if all fields have been fulled
            if (Username.getText().isEmpty()) {
                Message.setText(warning + "n username"); //Throws a warning message asking for an username
            } else if (Password.getText().isEmpty()) {
                Message.setText(warning + " password"); //Throws a warning message asking for a password
            } else {
                openMainController(Username.getText()); //Opens the selectChatWindow with our username
            }

        });
    }

    private void openMainController (String username) {

        SharedData sharedData = new SharedData(username);   //Creates a new client and allows us to share it between all the windows
        try{

            //Creates the SelectChatWindow
            FXMLLoader selectChatLoader = new FXMLLoader(getClass().getResource("SelectChatWindow.fxml"));
            Parent selectChatWindow = selectChatLoader.load();
            SelectChatController selectChatController = selectChatLoader.getController();
            selectChatController.setSharedData(sharedData);

            Stage stage = new Stage();
            stage.setTitle("WhatsUP");
            stage.setScene(new Scene( selectChatWindow, 600, 600));
            stage.show();

            //Closes the current window
            Stage currentStage = (Stage) Login_Button.getScene().getWindow();
            currentStage.close();

        } catch(IOException e){
            e.printStackTrace();
        }
        //Closes the current window
        Stage currentStage = (Stage) Login_Button.getScene().getWindow();
        currentStage.close();
    }


}