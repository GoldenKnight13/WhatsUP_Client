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

public class EnvelopeKeysController implements Initializable {

    //Connects the FXML components wit our variables, allowing us to control them
    @FXML TextField keyInput1;
    @FXML TextField keyInput2;
    @FXML Button confirmButton;
    @FXML Label warningMessage;

    private SharedData sharedData;


    //Function that runs when the window is created
    @Override public void initialize(URL location, ResourceBundle resources) {

        //Sets placeholders for the inputs
        keyInput1.setPromptText("Private Key");
        keyInput2.setPromptText("Public Key");

        //Confirm button action
        confirmButton.setOnAction( actionEvent -> {


            String input1 = keyInput1.getText();    //Private key
            String input2 = keyInput2.getText();    //Public key

            if(!input1.isEmpty() && !input2.isEmpty()){

                //Searches for numbers in the input area
                Matcher patternFinder1 = Pattern.compile("[1-9]([0-9]+)?").matcher(input1);
                Matcher patternFinder2 = Pattern.compile("[1-9]([0-9]+)?").matcher(input2);

                boolean numericKeys = patternFinder1.find() && patternFinder2.find();

                if(numericKeys){

                    int key1 = Integer.parseInt(input1);
                    int key2 = Integer.parseInt(input2);

                    String[] parts = sharedData.myMessage.split(";", 2);

                    if(parts[0].equals("304")){

                        String envelope = "304;" + sharedData.encryptionTool.digitalEnvelope(parts[1], key1, key2);
                        sharedData.chatWindowController.sendMessage( envelope);

                    } else {

                        String message;
                        String data = sharedData.encryptionTool.validateEnvelope(parts[1], key1, key2);

                        String[] openEnvelope = data.split(";", 2);
                        boolean validated = Boolean.parseBoolean(openEnvelope[0]);

                        if( validated ){
                            message = openEnvelope[1];
                        } else {
                            message = "Invalid envelope";
                        }

                        showMessage(message);

                    }

                    Stage currentStage = (Stage) confirmButton.getScene().getWindow();
                    currentStage.close();

                } else {
                    warningMessage.setText("Only numbers allowed");
                }

            } else {
                warningMessage.setText("Give me both keys!!");
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
