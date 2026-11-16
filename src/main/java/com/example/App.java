package com.example;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.example.testingZone.FlightIntel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {
    private Scene sceneFirst;
    private Scene sceneSec;
    private Scene sceneThrid;
    private Scene sceneFourth; // add
    private Scene addingConfirmation;
    private Scene addingFailOrSuccessScene;
    //backend
    public static String[] components;

    static{
        components = new String[6];
    }

    private static void refreshFlightList(ListView<FlightIntel> flightListView) {
        flightListView.getItems().setAll(testingZone.flightList);
    }

    private static boolean validateFlightFields(TextField[] fields, Label errorLabel) {
        

        for (TextField field : fields) {
            String value = field.getText().trim();
            if (value.isEmpty() || value.contains("/") || value.contains("\n") || value.contains("\r")) {
                errorLabel.setText("Complete every field. Values cannot contain '/'.");
                return false;
            }
        }
        try {
            LocalTime.parse(fields[3].getText().trim());
            LocalTime.parse(fields[4].getText().trim());
        } catch (DateTimeParseException ex) {
            errorLabel.setText("Enter departure and arrival times in HH:mm format.");
            return false;
        }
        errorLabel.setText("");
        return true;
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
        for (Button button : buttons) {
            button.setPrefHeight(40);
            button.setPrefWidth(160);
            button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");
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
        //Adding confirmation screen

        VBox confirmation= new VBox();
        confirmation.setStyle("-fx-background-color: #d1a7a7");
        confirmation.setAlignment(Pos.TOP_CENTER);
        confirmation.setPadding(new Insets(20));

        //Second confirmation screen
        VBox failSuccessBox = new VBox();
        failSuccessBox.setStyle("-fx-background-color: #d1a7a7");
        failSuccessBox.setAlignment(Pos.TOP_CENTER);
        failSuccessBox.setPadding(new Insets(20));

        //EXECUTE PAGE COMPONENTS (SECOND PAGE)
        ListView<FlightIntel> flightListView = new ListView<>();
        flightListView.setPrefWidth(560);
        flightListView.setPrefHeight(460);
        flightListView.setCellFactory(list -> new ListCell<FlightIntel>() {
            @Override
            protected void updateItem(FlightIntel flight, boolean empty) {
                super.updateItem(flight, empty);
                setText(empty || flight == null ? null : flight.getInfo());
            }
        });

        Button editFlightButton = new Button("Edit Selected Flight");
        editFlightButton.setDisable(true);
        editFlightButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");
        flightListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, previousFlight, selectedFlight) -> editFlightButton.setDisable(selectedFlight == null));

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        //Set Second Components
        secBox.getChildren().addAll(flightListView, editFlightButton, backButton);

        //SEARCH PAGE COMPONENTS(THIRD PAGE)
        Label labelSearch = new Label("Searh for your flight");
        labelSearch.setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");
        
        TextField searchForFlArea = new TextField();
        searchForFlArea.setPromptText("TK:Number");
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

         //CONFIRMATINO PAGE COMPONENTS(5TH PAGE)
        Button backToAddButton = new Button("Cancel");
        backToAddButton.setStyle("-fx-background-color:  #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");

        Button confirtAddingButton = new Button("OK");
        confirtAddingButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");
        //confirtAddingButton.maxWidthProperty().bind(backToAddButton.widthProperty());
        //confirtAddingButton.maxHeightProperty().bind(backToAddButton.heightProperty());

        Label areYouSure = new Label("Confirm flight");
        areYouSure.setStyle("-fx-background-color: transparent; -fx-padding: 8px; -fx-font-size: 17 px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent;");

        HBox allignConfirmButtons = new HBox(10);
        allignConfirmButtons.setAlignment(Pos.TOP_CENTER);
        allignConfirmButtons.getChildren().addAll(confirtAddingButton,backToAddButton);

        //FailOrSuccsesComponents(6th page)
        Label failOrSuccLabel = new Label("Flight has been added."); //LABEL WILL CHANGE DUE TO THE COND FROM BACKEND
        failOrSuccLabel.setStyle("-fx-background-color: transparent; -fx-padding:8px; -fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-border-color: transparent");

        Button buttonBackFromSuccess = new Button("Back");
        buttonBackFromSuccess.setStyle("-fx-background-color:  #e66767; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");
        
        Button seeFlightsSuccess = new Button("See Flights");
        seeFlightsSuccess.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand;");
        
        HBox allignSuccButtonsBox = new HBox(10);
        allignSuccButtonsBox.setAlignment(Pos.TOP_CENTER);
        allignSuccButtonsBox.getChildren().addAll(seeFlightsSuccess,buttonBackFromSuccess);


        //SetFailOrSuccessComponents(6th page)
        failSuccessBox.getChildren().addAll(failOrSuccLabel,allignSuccButtonsBox);
        //Set confirmation page(5th page)
        confirmation.getChildren().addAll(areYouSure,allignConfirmButtons);

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
        VBox.setMargin(flightListView, new Insets(10, 0, 10, 0));
        VBox.setMargin(editFlightButton, new Insets(0, 0, 10, 0));

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
        
        //Fifth Page Instets
        VBox.setMargin(allignConfirmButtons, new Insets(120,0,0,0));
        VBox.setMargin(areYouSure, new Insets(30,0,0,0));

        //Sixth Page Instets
        VBox.setMargin(allignSuccButtonsBox, new Insets(120,0,0,0));
        VBox.setMargin(failOrSuccLabel, new Insets(20,0,0,0));

        //Instantiate scenes
        sceneFirst = new Scene(firstBox, 600, 600);
        sceneSec = new Scene(secBox, 600, 600);
        sceneThrid = new Scene(thirdBox, 600, 600);
        sceneFourth = new Scene(fourthBox, 600, 600);
        addingConfirmation = new Scene(confirmation,300,300);
        addingFailOrSuccessScene = new Scene(failSuccessBox,300,300);
        

        
        //stages
        Stage confirmationStage = new Stage();
        confirmationStage.setScene(addingConfirmation);
        confirmationStage.initModality(Modality.WINDOW_MODAL);
        confirmationStage.initOwner(stage);

        Stage failOrSuccessStage = new Stage();
        failOrSuccessStage.setScene(addingFailOrSuccessScene);
        failOrSuccessStage.initModality(Modality.WINDOW_MODAL);
        failOrSuccessStage.initOwner(stage);




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
            refreshFlightList(flightListView);
        });

        editFlightButton.setOnAction(edit -> {
            FlightIntel selectedFlight = flightListView.getSelectionModel().getSelectedItem();
            if (selectedFlight == null) {
                return;
            }

            int selectedIndex = testingZone.flightList.indexOf(selectedFlight);
            Dialog<ButtonType> editor = new Dialog<>();
            editor.initOwner(stage);
            editor.setTitle("Edit Flight");
            editor.setHeaderText("Update flight details");

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            editor.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            String[] labels = {"Flight No:", "From:", "To:", "Departure:", "Arrival:", "Aircraft:"};
            String[] values = {
                selectedFlight.flight, selectedFlight.origin, selectedFlight.destination,
                selectedFlight.departure, selectedFlight.arrival, selectedFlight.airCraft
            };
            TextField[] editFields = new TextField[values.length];
            GridPane editGrid = new GridPane();
            editGrid.setHgap(10);
            editGrid.setVgap(10);
            editGrid.setPadding(new Insets(10));
            for (int i = 0; i < editFields.length; i++) {
                editFields[i] = new TextField(values[i]);
                editGrid.add(new Label(labels[i]), 0, i);
                editGrid.add(editFields[i], 1, i);
            }
            Label editError = new Label();
            editError.setStyle("-fx-text-fill: #c0392b;");
            editGrid.add(editError, 0, editFields.length, 2, 1);
            editor.getDialogPane().setContent(editGrid);

            editor.getDialogPane().lookupButton(saveButtonType).addEventFilter(ActionEvent.ACTION, save -> {
                if (!validateFlightFields(editFields, editError)) {
                    save.consume();
                    return;
                }
                try {
                    FlightIntel.UpdateFlight(
                        selectedIndex,
                        editFields[0].getText().trim(),
                        editFields[1].getText().trim(),
                        editFields[2].getText().trim(),
                        editFields[3].getText().trim(),
                        editFields[4].getText().trim(),
                        editFields[5].getText().trim()
                    );
                    refreshFlightList(flightListView);
                    flightListView.getSelectionModel().select(selectedIndex);
                } catch (IOException ex) {
                    editError.setText("Could not save flight changes: " + ex.getMessage());
                    save.consume();
                }
            });
            editor.showAndWait();
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
                    vieWSearchedFlight.setText(resulString);
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
            for (TextField addField : addFields) {
                for (TextField addfieFieldsSec1 : addfieFieldsSec) {
                    if (addField == null) {
                        return;
                    }
                    addField.clear();
                    if (addfieFieldsSec1 == null) {
                        return;
                    }
                    addfieFieldsSec1.clear();
                }
            }
        });

        addFlightButton.setOnAction(addFlightButtonAcc -> {
            //stage.setTitle("Standby");
            //stage.setScene(addingConfirmation);
            //stage.show();
            stage.setTitle("Confirm");
            confirmationStage.show();

            
        });

        //CONFIRMATION PAGE
        backToAddButton.setOnAction(backToAdd ->{
            confirmationStage.close();
        });

        confirtAddingButton.setOnAction(confirm ->{                                            
            testingZone.FlightIntel.AddFlight(
                addFields[0].getText(), 
                addFields[1].getText(), 
                addFields[2].getText(), 
                addfieFieldsSec[0].getText(), 
                addfieFieldsSec[1].getText(), 
                addfieFieldsSec[2].getText()
                
            );
            confirmationStage.close();
            failOrSuccessStage.show();
        });

        buttonBackFromSuccess.setOnAction(bacKSuccess ->{
            failOrSuccessStage.close();
            stage.setScene(sceneFourth);
            stage.show();
        });

        seeFlightsSuccess.setOnAction(seeFlightsSucc ->{
            failOrSuccessStage.close();
             stage.setTitle("All Depatures");
             stage.setScene(sceneSec);
             stage.show();

            refreshFlightList(flightListView);
            for (TextField addField : addFields) {
                for (TextField addfieFieldsSec1 : addfieFieldsSec) {
                    if (addField == null) {
                        return;
                    }
                    addField.clear();
                    if (addfieFieldsSec1 == null) {
                        return;
                    }
                    addfieFieldsSec1.clear();
                }
            }

        });


        adjustButton.setOnAction(closeApp -> {
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}