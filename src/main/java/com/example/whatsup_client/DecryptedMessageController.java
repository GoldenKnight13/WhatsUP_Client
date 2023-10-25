package com.example.whatsup_client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DecryptedMessageController implements Initializable {

    @FXML VBox messageContainer;
    @FXML Button okButton;

    String decryptedMessage;

    @Override public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(()->{
            Text text = new Text(decryptedMessage);
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

            messageContainer.getChildren().add(text);

        });

        okButton.setOnAction(actionEvent -> {
            Stage currentStage = (Stage) okButton.getScene().getWindow();
            currentStage.close();
        });


    }

    public void setMessageToShow (String newMessage){
        this.decryptedMessage = newMessage;
    }


}
