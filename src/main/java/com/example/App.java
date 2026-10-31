package com.example;
import com.example.testingZone.FlightIntel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class App extends Application {
    private Scene sceneFirst;
    private Scene sceneSec;
    private Scene sceneThrid;
    private Scene sceneFourth; // add
    //backend
    public static String[] components;

    static{
        components = new String[6];
    }

    @Override
    public void start(Stage stage){
        //backend part
        testingZone.FlightIntel.ReadFile();

        //Fx part
        Image loginImage = new Image("file:plane.jpg");
        ImageView loginView = new ImageView(loginImage); 
        loginView.setPreserveRatio(true);
        loginView.setFitWidth(400);
        
        Label welcomeLabel = new Label("Welcome to the staff ");
        welcomeLabel.setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
        
        Button testButton = new Button("Adjust/See Flights");
        Button searcButton = new Button("Search");
        Button addButton = new Button("Add"); //addFlight

        Button adjustButton = new Button("Log Out");
        adjustButton.setPrefHeight(40);
        adjustButton.setPrefWidth(160);
        adjustButton.setStyle("-fx-background-color:  #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        Button buttons[] ={testButton,searcButton,addButton};
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
        
        //VBoxes
        VBox firstBox = new VBox();
        firstBox.setStyle("-fx-background-color: #d1a7a7");
        firstBox.setAlignment(Pos.TOP_CENTER);
        firstBox.setPadding(new Insets(20));

        //secBox 
        VBox secBox = new VBox();
        secBox.setStyle("-fx-background-color: #d1a7a7");
        secBox.setAlignment(Pos.TOP_CENTER);
        secBox.setPadding(new Insets(20)); // Düzeltildi (firstBox.setPadding yerine secBox)

        //thirdBox
        VBox thirdBox = new VBox();
        thirdBox.setStyle("-fx-background-color: #d1a7a7");
        thirdBox.setAlignment(Pos.TOP_CENTER);
        thirdBox.setPadding(new Insets(20));

        VBox fourthBox = new VBox();
        fourthBox.setStyle("-fx-background-color: #d1a7a7");
        fourthBox.setAlignment(Pos.TOP_CENTER);
        fourthBox.setPadding(new Insets(20));

        firstBox.getChildren().addAll(welcomeLabel, firstAlign, secondAllignment, loginView);

        //EXECUTE PAGE COMPONENTS (SECOND PAGE)
        TextArea flightResult = new TextArea();
        flightResult.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8px; -fx-font-size: 14px; -fx-font-family: 'Consolas';");
        flightResult.setPrefWidth(500);
        flightResult.setPrefHeight(500);
        flightResult.setWrapText(true);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        //Set Second Components
        secBox.getChildren().addAll(flightResult, backButton);

        //SEARCH PAGE COMPONENTS(THIRD PAGE)
        Label labelSearch = new Label("Searh for your flight");
        labelSearch.setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
        
        TextField searchForFlArea = new TextField();
        searchForFlArea.setPromptText("Enter the flight");
        searchForFlArea.setStyle("-fx-background-color: white; -fx-padding: 8px; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
        searchForFlArea.setFocusTraversable(false);

        Button backButtonSearch = new Button("Back");
        backButtonSearch.setStyle("-fx-background-color: #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        Button searchFliExecute = new Button("Look for flights!");
        searchFliExecute.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");
        searchFliExecute.setMaxWidth(200);

        TextArea vieWSearchedFlight = new TextArea(); //Outcome
        vieWSearchedFlight.setStyle("-fx-background-color: transparent; -fx-border-color: #bdc3c7; -fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8px; -fx-font-size: 14px; -fx-font-family: 'Consolas';");
        vieWSearchedFlight.setMaxHeight(400);
        vieWSearchedFlight.setMaxWidth(600);
        
        //Set Third Components
        thirdBox.getChildren().addAll(labelSearch, searchForFlArea, searchFliExecute, vieWSearchedFlight, backButtonSearch);

        //ADD PAGE COMPONENTS(FOURTH PAGE)
        Label addLabel = new Label("Please enter the intels that belong to your flight plan.");
        addLabel.setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");      
        
        //HBoxes for textfields
        HBox textFieldHBoxFirst = new HBox();
        textFieldHBoxFirst.setAlignment(Pos.TOP_CENTER);
        textFieldHBoxFirst.setSpacing(20);

        HBox textFieldBoxSec = new HBox();
        textFieldBoxSec.setAlignment(Pos.TOP_CENTER);
        textFieldBoxSec.setSpacing(20);

        Button exitButtonInAdd = new Button("Back");
        exitButtonInAdd.setStyle("-fx-background-color: #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        Button addFlightButton = new Button("Add Flight");
        addFlightButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        HBox allignButtonsForthPage = new HBox();
        allignButtonsForthPage.setAlignment(Pos.TOP_CENTER);
        allignButtonsForthPage.setSpacing(30);
        allignButtonsForthPage.getChildren().addAll(addFlightButton, exitButtonInAdd);
        
        //first 3 row text
        TextField[] addFields = new TextField[3];
        for(int i = 0; i < addFields.length; i++){
            addFields[i] = new TextField();
            addFields[i].setStyle("-fx-background-color: white; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
            textFieldHBoxFirst.getChildren().addAll(addFields[i]);
        }
        addFields[0].setPromptText("TK Number");
        addFields[1].setPromptText("Airport");
        addFields[2].setPromptText("Airport");

        //sec 3 row text
        TextField[] addfieFieldsSec = new TextField[3];
        for(int i = 0; i < addfieFieldsSec.length; i++){
            addfieFieldsSec[i] = new TextField();
            addfieFieldsSec[i].setStyle("-fx-background-color: white; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
            textFieldBoxSec.getChildren().addAll(addfieFieldsSec[i]);
        }
        addfieFieldsSec[0].setPromptText("Time");
        addfieFieldsSec[1].setPromptText("Time");
        addfieFieldsSec[2].setPromptText("Plane No");
        
        HBox labelsForText = new HBox();
        labelsForText.setAlignment(Pos.TOP_CENTER);
        labelsForText.setSpacing(125);

        HBox labelsForSec = new HBox();
        labelsForSec.setAlignment(Pos.TOP_CENTER);
        labelsForSec.setSpacing(115);
    
        Label[] labelsForTextArray = new Label[3];
        for(int i = 0; i < labelsForTextArray.length; i++){
            labelsForTextArray[i] = new Label();
            labelsForTextArray[i].setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
            labelsForText.getChildren().addAll(labelsForTextArray[i]);
        }
        labelsForTextArray[0].setText("Flight No:");
        labelsForTextArray[1].setText("From:");
        labelsForTextArray[2].setText("To:");

        //Second Row Labels
        Label[] labelsForSecArray = new Label[3];
        for(int i = 0; i < labelsForSecArray.length; i++){
            labelsForSecArray[i] = new Label();
            labelsForSecArray[i].setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
            labelsForSec.getChildren().addAll(labelsForSecArray[i]);
        }
        labelsForSecArray[0].setText("Departure:");
        labelsForSecArray[1].setText("Arrival:");
        labelsForSecArray[2].setText("Aircraft:");
        
        // 4. Sayfa bileşenleri başlangıçta bir kez kutuya eklenir
        fourthBox.getChildren().addAll(addLabel, labelsForText, textFieldHBoxFirst, labelsForSec, textFieldBoxSec, allignButtonsForthPage);
        fourthBox.setFocusTraversable(true);

        //Insets 
        VBox.setMargin(firstAlign, new Insets(20, 0, 0, 0)); 
        VBox.setMargin(secondAllignment, new Insets(50, 0, 0, 0));
        VBox.setMargin(loginView, new Insets(80, 0, 0, 0));

        //Second Page Insets
        VBox.setMargin(flightResult, new Insets(20, 0, 0, 0));
        VBox.setMargin(backButton, new Insets(-60, 0, 0, 0)); 

        //Third page insets
        VBox.setMargin(backButtonSearch, new Insets(240, 0, 0, 0));
        VBox.setMargin(searchFliExecute, new Insets(20, 0, 0, 0));
        VBox.setMargin(vieWSearchedFlight, new Insets(50, 0, 0, 0));

        //Forth Page Inslets
        VBox.setMargin(textFieldBoxSec, new Insets(10, 0, 0, 0));
        VBox.setMargin(labelsForText, new Insets(40, 48, 0, 0));
        VBox.setMargin(labelsForSec, new Insets(70, 10, 0, 0));
        VBox.setMargin(textFieldHBoxFirst, new Insets(15, 0, 0, 0));
        VBox.setMargin(allignButtonsForthPage, new Insets(140, 0, 0, 0));

        sceneFirst = new Scene(firstBox, 600, 600);
        sceneSec = new Scene(secBox, 600, 600);
        sceneThrid = new Scene(thirdBox, 600, 600);
        sceneFourth = new Scene(fourthBox, 600, 600);

        stage.setTitle("Turkis Air Lines");
        stage.setScene(sceneFirst);
        stage.show();

        /////Events
    
        //Execute second page(show all flights)
        testButton.setOnAction(e -> {
            stage.setTitle("All Depatures");
            stage.setScene(sceneSec);
            stage.show();

            // secBox.getChildren().addAll(...) buradan kaldırıldı (Çakışma hatasını önlemek için)

            flightResult.clear();
            int index = 1;
            for(int i = 0; i < testingZone.flightList.size(); i++){
                String text = index + "- " + testingZone.flightList.get(i).getInfo();
                flightResult.appendText(text + "\n");
                index += 1;
            }
        });

        backButton.setOnAction(back -> {
            stage.setTitle("Turkis Air Lines");
            stage.setScene(sceneFirst);
            stage.show();
        });

        //Execute Search Page
        searcButton.setOnAction(search -> {
             stage.setTitle("Search for Flight");
             stage.setScene(sceneThrid);
             stage.show();
             // thirdBox.getChildren().addAll(...) buradan kaldırıldı
        });

        backButtonSearch.setOnAction(backToMain -> { 
            stage.setTitle("Turkish Airlines");
            stage.setScene(sceneFirst);
            stage.show();
            searchForFlArea.clear();
            vieWSearchedFlight.clear();
        });

        searchFliExecute.setOnAction(search -> {
             try {
                String answer = searchForFlArea.getText();
                FlightIntel result = testingZone.FlightIntel.SearchForFlight(answer);
                if(result == null) vieWSearchedFlight.setText("No flight found");
                else {
                    String resulString = String.valueOf(result);
                    vieWSearchedFlight.setText(resulString.toString());
                }
             } catch (Exception e) {
                System.out.println("Error");
             }
        });

        //Forth page actions
        addButton.setOnAction(add -> {
            stage.setTitle("Add Flight");
            stage.setScene(sceneFourth);
            stage.show();
            // fourthBox.getChildren().addAll(...) buradan kaldırıldı
        });

        exitButtonInAdd.setOnAction(exitButtonInAddAc -> {
            stage.setTitle("Turkish Airlines");
            stage.setScene(sceneFirst);
            stage.show();
            for(int i = 0; i < addFields.length; i++){
                for(int j = 0; j < addfieFieldsSec.length; j++){
                    if(addFields[i] == null) return;
                    addFields[i].clear();
                    if(addfieFieldsSec[j] == null) return;
                    addfieFieldsSec[j].clear();
                }
            }
        });

        addFlightButton.setOnAction(addFlightButtonAcc -> {
            testingZone.FlightIntel.AddFlight(
                addFields[0].getText(), 
                addFields[1].getText(), 
                addFields[2].getText(), 
                addfieFieldsSec[0].getText(), 
                addfieFieldsSec[1].getText(), 
                addfieFieldsSec[2].getText()
            );
        });

        adjustButton.setOnAction(closeApp -> {
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}