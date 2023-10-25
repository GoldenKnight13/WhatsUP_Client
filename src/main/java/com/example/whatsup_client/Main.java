package com.example.whatsup_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//Initializes the app
public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        //Creates a new java window and uploads the content of the log in window
        FXMLLoader logInWindowLoader = new FXMLLoader(getClass().getResource("LogInWindow.fxml"));
        Parent logInWindow = logInWindowLoader.load();
        stage.setTitle("WhatsUP");
        stage.setScene(new Scene( logInWindow, 600, 400));
        stage.show();
    }

    //Allows us to start the app
    public static void main(String[] args) {
        launch(args);
    }

}