package com.example;

import java.lang.reflect.Array;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//event driven programming

public class App extends Application {
   private Scene sceneFirst, sceneSec;
   private String password =("1234");
   private String name =("Can");

   @Override
   public void start(Stage stage){


    Label labelFirst = new Label("Please login or create an accound");
    labelFirst.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");


    TextField textFieldName = new TextField();
    textFieldName.setPromptText("Username");

    TextField textFieldPassword = new TextField();
    textFieldPassword.setPromptText("Password");
    
    Button buttonOK = new Button("Login");
    buttonOK.setStyle("-fx-background-color: #b77070; -fx-text-fill: white-fx-font-size: 14px; -fx-background-radius: 8px;-fx-cursor: hand;");

    Button buttonNotRegistered = new Button("Register");
    buttonNotRegistered.setStyle("-fx-background-color: #b77070; -fx-text-fill: white-fx-font-size: 14px; -fx-background-radius: 8px;-fx-cursor: hand;");

    HBox regisLog = new HBox(10);
    regisLog.setAlignment(Pos.CENTER);
    regisLog.getChildren().addAll(buttonOK,buttonNotRegistered);

    TextField[] textFields = {textFieldName,textFieldPassword};
    for(int i = 0;i<textFields.length;i++){
        textFields[i].setMaxWidth(150);
    }

    
       VBox vBoxFirst = new VBox();
       vBoxFirst.setStyle("-fx-background-color: #d1a7a7;");
       vBoxFirst.setAlignment(Pos.TOP_CENTER);
       vBoxFirst.setPadding(new Insets(15));
       //margins
       VBox.setMargin(textFieldName, new Insets(100,0,0,0));
       VBox.setMargin(textFieldPassword, new Insets(10,0,0,0));
       VBox.setMargin(regisLog, new Insets(20,0,0,0));
       VBox.setMargin(labelFirst, new Insets(69,0,0,0));
       vBoxFirst.getChildren().addAll(labelFirst,textFieldName,textFieldPassword,regisLog);
       sceneFirst = new Scene(vBoxFirst,600,600);
       //Second scene vbox
       VBox vBoxSec = new VBox();
       vBoxSec.setStyle("-fx-background-color: #d1a7a7;");
       sceneSec = new Scene(vBoxSec,600,600);

       Label labelLoging = new Label("Welcome to the system");
       labelLoging.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
       labelLoging.setAlignment(Pos.TOP_CENTER);

       //Staging
       stage.setTitle("Welcome Dog");
       stage.setScene(sceneFirst);
       stage.show();


     //Event Handling
     buttonOK.setOnAction(e ->{
        String nameInput = textFieldName.getText();
        String passwordInput = textFieldPassword.getText();

        
        

        Image welcomeImage = new Image("file:nays.png");
        ImageView welcoImageView = new ImageView(welcomeImage);
        welcoImageView.setFitWidth(200);
        welcoImageView.setPreserveRatio(true);
        VBox.setMargin(welcoImageView, new Insets(60,0,0,0));

        if(nameInput.equals(name) && passwordInput.equals(password)){
            stage.setScene(sceneSec);
            stage.show();

            Button buttonBack = new Button("Log out");
            buttonBack.setMaxWidth(150);
            buttonBack.setStyle("-fx-background-color: #b77070; -fx-text-fill: white-fx-font-size: 14px; -fx-background-radius: 8px;-fx-cursor: hand;");
            VBox.setMargin(buttonBack, new Insets(10,0,0,0));

            vBoxSec.getChildren().addAll(labelLoging,welcoImageView,buttonBack);
            vBoxSec.setAlignment(Pos.TOP_CENTER);
            VBox.setMargin(labelLoging, new Insets(160,0,0,0));

            //EventHadnleSec
            buttonBack.setOnAction(event->{
                stage.setScene(sceneFirst);
                stage.show();
            });            

        }else{
            labelFirst.setText("Incorrect username or password");
            
        }

     });

   }

   


    public static void main(String[] args) {
        launch(); 
    }
}