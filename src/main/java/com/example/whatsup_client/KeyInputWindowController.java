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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KeyInputWindowController implements Initializable {

    //Connects the FXML components wit our variables, allowing us to control them
    @FXML TextField keyInput;
    @FXML Button confirmButton;
    @FXML Label warningMessage;

    private SharedData sharedData;

    //Function that runs when the window is created
    @Override public void initialize(URL location, ResourceBundle resources) {

        //Confirm button action
        confirmButton.setOnAction( actionEvent -> {

            String selectedKey = keyInput.getText();    //Gets the text from the text area

            //If the text area is not empty
            if(!selectedKey.isEmpty()){

                Matcher patternFinder = Pattern.compile("[1-9]([0-9]+)?").matcher(selectedKey);    //Searches for numbers in the input area

                if (patternFinder.find()) {

                    //Process to send encrypted message
                    String[] parts = sharedData.myMessage.split(";", 2);
                    int key = Integer.parseInt(selectedKey);

                    //Decryption
                    if(parts[0].equals("201")){
                        showMessage( sharedData.encryptionTool.symmetricDecryption(parts[1], key) );
                    } else if ( parts[0].equals("202")){
                        showMessage( sharedData.encryptionTool.encryptMessage(parts[1], key) );
                    } else if ( parts[0].equals("203")){

                        boolean validation = sharedData.encryptionTool.validateSignature(parts[1], key);
                        String validationMessage = (validation) ? "Message is authentic" : "Intercepted communications";

                        showMessage( validationMessage );
                    } else if ( parts[0].equals("204")){

                        boolean validation = sharedData.encryptionTool.validateSignature(parts[1], key);
                        String validationMessage = (validation) ? "Message is authentic" : "Intercepted communications";

                        showMessage( validationMessage );
                    }

                    //Encryption
                    if(parts[0].equals("303")){
                        parts[1] = sharedData.encryptionTool.digitalSignature(parts[1], key);
                        String encryptedMessage = parts[0] + ';' + parts[1];
                        sharedData.chatWindowController.sendMessage(encryptedMessage);
                    } else if (parts[0].equals("301") || parts[0].equals("302")) {
                        parts[1] = sharedData.encryptionTool.encryptMessage(parts[1], key );
                        String encryptedMessage = parts[0] + ';' + parts[1];
                        sharedData.chatWindowController.sendMessage(encryptedMessage);
                    }

                    Stage currentStage = (Stage) confirmButton.getScene().getWindow();
                    currentStage.close();

                } else {
                    warningMessage.setText("Only numbers");   //Else it throws a warning message
                }

            //If the text area is empty
            } else {
                warningMessage.setText("Enter a number");
            }

        });
    }

    public void setSharedData(SharedData sharedData){
        this.sharedData = sharedData;
    }

    private void showMessage(String message){
        try {

            FXMLLoader showDecryptedMessageLoader = new FXMLLoader(getClass().getResource("ShowDecryptedMessage.fxml"));
            Parent showDecryptedMessageWindow = showDecryptedMessageLoader.load();
            DecryptedMessageController showDecryptedMessageController = showDecryptedMessageLoader.getController();
            showDecryptedMessageController.setMessageToShow(message);

            Stage stage = new Stage();
            stage.setTitle("WhatsUP");
            stage.setScene(new Scene(showDecryptedMessageWindow, 600, 200));
            stage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
