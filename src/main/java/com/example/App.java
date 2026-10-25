package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class App extends Application {
    private Scene sceneFirst;
    private Scene sceneSec;
    private Scene sceneThrid;

    @Override
    public void start(Stage stage){
        //backend part
        testingZone.FlightIntel.ReadFile();

        //Fx part

        Label welcomeLabel = new Label("Welcome to the staff ");
        welcomeLabel.setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
        Button testButton = new Button("Adjust/See Flights");

        Button searcButton = new Button("Search");

        Button addButton = new Button("Add");

        Button adjustButton = new Button("Log Out");

        Button buttons[] ={testButton,searcButton,addButton,adjustButton};
        for(int i = 0; i<buttons.length;i++){
            buttons[i].setPrefHeight(40);
            buttons[i].setPrefWidth(160);
            buttons[i].setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        }
        

        HBox firstAlign = new HBox();
        firstAlign.setAlignment(Pos.TOP_CENTER);
        firstAlign.setSpacing(40);
        firstAlign.getChildren().addAll(testButton, searcButton);

        HBox secondAllignment = new HBox();
        secondAllignment.setAlignment(Pos.TOP_CENTER);
        secondAllignment.setSpacing(40);
        secondAllignment.getChildren().addAll(addButton,adjustButton);
        

        VBox firstBox = new VBox();
        firstBox.setStyle("-fx-background-color: #d1a7a7");
        firstBox.setAlignment(Pos.TOP_CENTER);
        firstBox.setPadding(new Insets(20));

        //secVox 
        VBox secBox = new VBox();
        secBox.setStyle("-fx-background-color: #d1a7a7");
        secBox.setAlignment(Pos.TOP_CENTER);
        firstBox.setPadding(new Insets(20));

        //thirdBox
        VBox thirdBox = new VBox();
        thirdBox.setStyle("-fx-background-color: #d1a7a7");
        thirdBox.setAlignment(Pos.TOP_CENTER);
        thirdBox.setPadding(new Insets(20));

        
        firstBox.getChildren().addAll(welcomeLabel,firstAlign,secondAllignment); //,flightResult);

        //Insets 
        VBox.setMargin(firstAlign, new Insets(20, 0, 0, 0)); 
        VBox.setMargin(secondAllignment, new Insets(50,0,0,0));

        sceneFirst = new Scene(firstBox, 600, 600);
        sceneSec = new Scene(secBox, 600, 600);
        sceneThrid = new Scene(thirdBox, 600, 600);

        stage.setTitle("Turkis Air Lines");
        stage.setScene(sceneFirst);
        stage.show();

        //EXECUTE PAGE COMPONENTS (SECOND PAGE)
         TextArea flightResult = new TextArea();
        flightResult.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8px; -fx-font-size: 14px; -fx-font-family: 'Consolas';");
        flightResult.setPrefWidth(500);
        flightResult.setPrefHeight(500);
        flightResult.setWrapText(true);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");


        //SEARCH PAGE COMPONENTS(THIRD PAGE)
        Label labelSearch = new Label("Searh for your flight");
        labelSearch.setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
        

        //Second Page Insets
        VBox.setMargin(flightResult, new Insets(20, 0, 0, 0));
        VBox.setMargin(backButton, new Insets(-60,0,0,0)); 


         /////Events
    
        //Execute second page(show all flights)
        testButton.setOnAction(e -> {
            stage.setTitle("All Depatures");
            stage.setScene(sceneSec);
            stage.show();

            secBox.getChildren().addAll(flightResult,backButton);
            

            flightResult.clear();
            int index = 1;
            
            for(int i = 0; i < testingZone.flightList.size(); i++){
                String text = index + "- " + testingZone.flightList.get(i).getInfo();
                flightResult.appendText(text + "\n");
                index += 1;
            }
        });

        backButton.setOnAction(back->{
            stage.setTitle("Turkis Air Lines");
            stage.setScene(sceneFirst);
            stage.show();
        });

        //Execute Search Page
        searcButton.setOnAction(search ->{
             stage.setTitle("Search for Flight");
             stage.setScene(sceneThrid);
             stage.show();
             thirdBox.getChildren().addAll(labelSearch);
        });
        
    }

    public static void main(String[] args) {
        launch();
    }
}