package com.example;

import com.example.testingZone.FlightIntel;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {

    public static String[] components = new String[6];

    Stage stage, confirmStage, resultStage, errorStage;
    Scene homeScene, listScene, searchScene, addScene;

    Label total = new Label("0");
    VBox recent = new VBox(10);
    TextArea flightsArea = new TextArea();
    TextField[] fields = new TextField[6];

    String planeSvg = "M3 11.5L21 3l-5 18-4.5-7.5L3 11.5z M11.5 13.5L21 3";
    String searchSvg = "M10.5 4a6.5 6.5 0 1 0 0 13a6.5 6.5 0 0 0 0-13z M20 20l-4.8-4.8";
    String plusSvg = "M12 5v14M5 12h14";
    String checkSvg = "M5 12.5l4.5 4.5L19 7.5";
    String crossSvg = "M6 6l12 12M18 6L6 18";

    String[] prompts = {"TK Number", "Airport", "Airport", "Time", "Time", "Plane No"};
    String[] names = {"Flight No:", "From:", "To:", "Departure:", "Arrival:", "Aircraft:"};

    // label with a css class
    Label lbl(String text, String css) {
        Label l = new Label(text);
        l.getStyleClass().add(css);
        l.setWrapText(true);
        return l;
    }

    Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        VBox.setVgrow(r, Priority.ALWAYS);
        return r;
    }

    StackPane icon(String svg) {
        SVGPath p = new SVGPath();
        p.setContent(svg);
        p.getStyleClass().add("icon");
        StackPane sp = new StackPane(p);
        sp.getStyleClass().add("icon-chip");
        return sp;
    }

    void columns(GridPane g) {
        for (int i = 0; i < 3; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 3);
            g.getColumnConstraints().add(cc);
        }
    }

    HBox topBar(Button extra) {
        StackPane logo = new StackPane(new Label("TK"));
        logo.getStyleClass().add("logo");

        VBox brand = new VBox(lbl("Turkish Airlines", "brand-name"), lbl("Staff Dashboard", "brand-sub"));
        brand.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane(new Label("ST"));
        avatar.getStyleClass().add("avatar");
        VBox who = new VBox(lbl("Staff", "user-name"), lbl("Signed in", "user-role"));
        who.setAlignment(Pos.CENTER_LEFT);
        HBox user = new HBox(10, avatar, who);
        user.setAlignment(Pos.CENTER_LEFT);

        HBox bar = new HBox(16, logo, brand, spacer(), user);
        if (extra != null) bar.getChildren().add(extra);
        bar.setAlignment(Pos.CENTER_LEFT);
        return bar;
    }

    BorderPane layout(Node heading, Node center, Button extra) {
        VBox top = new VBox(30, topBar(extra), heading);
        top.getStyleClass().add("header-zone");
        BorderPane bp = new BorderPane();
        bp.setTop(top);
        bp.setCenter(center);
        bp.getStyleClass().add("page");
        bp.setFocusTraversable(true);
        return bp;
    }

    Scene newScene(Region root) {
        Scene s = new Scene(root, 1120, 740);
        s.getStylesheets().add(getClass().getResource("/com/example/style.css").toExternalForm());
        return s;
    }

    void goTo(String title, Scene s) {
        stage.setTitle(title);
        stage.setScene(s);
        if (s == homeScene) updateHome();
    }

    Scene homeScreen() {
        Button flightsBtn = new Button("Adjust/See Flights");
        flightsBtn.getStyleClass().addAll("btn-primary", "btn-lg");
        flightsBtn.setOnAction(e -> showFlights());

        Button logOut = new Button("Log Out");
        logOut.getStyleClass().add("btn-dark");
        logOut.setOnAction(e -> Platform.exit());

        LocalDate today = LocalDate.now();
        StackPane badge = new StackPane(lbl(Integer.toString(today.getDayOfMonth()), "date-number"));
        badge.getStyleClass().add("date-badge");

        VBox dateBox = new VBox(
            lbl(today.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ",", "date-text"),
            lbl(today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), "date-text"));
        dateBox.setAlignment(Pos.CENTER_LEFT);

        Region line = new Region();
        line.getStyleClass().add("divider");

        VBox welcome = new VBox(lbl("Welcome to the staff ", "hero-title"),
                                lbl("What would you like to do today?", "hero-sub"));
        welcome.setAlignment(Pos.CENTER_LEFT);

        HBox hero = new HBox(18, badge, dateBox, line, flightsBtn, spacer(), welcome);
        hero.setAlignment(Pos.CENTER_LEFT);

        // plane picture, rounded corners
        ImageView pic = new ImageView(new Image("file:plane.jpg"));
        pic.setPreserveRatio(true);
        pic.setFitWidth(300);
        Rectangle round = new Rectangle();
        round.setArcWidth(36);
        round.setArcHeight(36);
        pic.layoutBoundsProperty().addListener((obs, o, b) -> {
            round.setWidth(b.getWidth());
            round.setHeight(b.getHeight());
        });
        pic.setClip(round);
        StackPane picCard = new StackPane(pic);
        picCard.getStyleClass().add("card-image");

        total.getStyleClass().add("stat-number");
        VBox statCard = new VBox(8, icon(planeSvg), lbl("Total departures", "card-text"), total,
                                 lbl("flights on the schedule", "card-text"));
        statCard.getStyleClass().add("card");

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("btn-dark");
        searchBtn.setOnAction(e -> goTo("Search for Flight", searchScene));
        VBox searchCard = new VBox(8, icon(searchSvg), lbl("Find a flight", "card-title"),
                lbl("Look up a flight by its TK number.", "card-text"), spacer(), searchBtn);
        searchCard.getStyleClass().add("card");

        Button addBtn = new Button("Add");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> goTo("Add Flight", addScene));
        VBox addCard = new VBox(8, icon(plusSvg), lbl("Add a flight", "card-title"),
                lbl("Enter the details of a new departure.", "card-text"), spacer(), addBtn);
        addCard.getStyleClass().add("card");

        VBox recentCard = new VBox(16, lbl("Recent departures", "card-title"), recent);
        recentCard.getStyleClass().add("card");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.getStyleClass().add("body");
        columns(grid);
        RowConstraints r1 = new RowConstraints();
        RowConstraints r2 = new RowConstraints();
        r2.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().addAll(r1, r2);

        grid.add(statCard, 0, 0);
        grid.add(searchCard, 1, 0);
        grid.add(addCard, 2, 0);
        grid.add(recentCard, 0, 1, 2, 1);
        grid.add(picCard, 2, 1);

        return newScene(layout(hero, grid, logOut));
    }

    Scene listScreen() {
        Button back = new Button("Back");
        back.getStyleClass().add("btn-secondary");
        back.setOnAction(e -> goTo("Turkish Airlines", homeScene));

        flightsArea.setWrapText(true);
        VBox.setVgrow(flightsArea, Priority.ALWAYS);

        VBox card = new VBox(flightsArea);
        card.getStyleClass().add("card");
        VBox.setVgrow(card, Priority.ALWAYS);

        VBox body = new VBox(card);
        body.getStyleClass().add("body");
        body.setAlignment(Pos.TOP_CENTER);

        VBox heading = new VBox(lbl("All departures", "hero-title"), lbl("Every scheduled flight in one place", "hero-sub"));
        return newScene(layout(heading, body, back));
    }

    Scene searchScreen() {
        TextField input = new TextField();
        input.setPromptText("TK:Number");
        input.getStyleClass().add("search-field");
        input.setFocusTraversable(false);
        HBox.setHgrow(input, Priority.ALWAYS);

        Button go = new Button("Look for flights!");
        go.getStyleClass().add("btn-primary");
        Button back = new Button("Back");
        back.getStyleClass().add("btn-secondary");

        TextArea result = new TextArea();
        VBox.setVgrow(result, Priority.ALWAYS);

        go.setOnAction(e -> {
            try {
                FlightIntel f = FlightIntel.SearchForFlight(input.getText());
                if (f == null) result.setText("No flight found");
                else result.setText(String.valueOf(f));
            } catch (Exception ex) {
                System.out.println("Error");
            }
        });
        back.setOnAction(e -> {
            input.clear();
            result.clear();
            goTo("Turkish Airlines", homeScene);
        });

        HBox row = new HBox(12, input, go);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(18, lbl("Search for your flight", "heading-label"), row, result);
        card.getStyleClass().add("card");
        card.setMaxWidth(760);
        VBox.setVgrow(card, Priority.ALWAYS);

        VBox body = new VBox(card);
        body.getStyleClass().add("body");
        body.setAlignment(Pos.TOP_CENTER);

        VBox heading = new VBox(lbl("Search", "hero-title"), lbl("Find a flight by its number", "hero-sub"));
        return newScene(layout(heading, body, back));
    }

    Scene addScreen() {
        GridPane form = new GridPane();
        form.setHgap(18);
        form.setVgap(22);
        columns(form);

        for (int i = 0; i < fields.length; i++) {
            fields[i] = new TextField();
            fields[i].setPromptText(prompts[i]);
            fields[i].setMaxWidth(Double.MAX_VALUE);
            form.add(new VBox(8, lbl(names[i], "field-label"), fields[i]), i % 3, i / 3);
        }

        Button back = new Button("Back");
        back.getStyleClass().add("btn-secondary");
        Button add = new Button("Add Flight");
        add.getStyleClass().add("btn-primary");

        back.setOnAction(e -> {
            clearFields();
            goTo("Turkish Airlines", homeScene);
        });
        add.setOnAction(e -> {
            stage.setTitle("Confirm");
            confirmStage.show();
        });

        HBox btns = new HBox(12, back, add);
        btns.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(26, lbl("Please enter the intels that belong to your flight plan.", "heading-label"), form, btns);
        card.getStyleClass().add("card");
        card.setMaxWidth(860);

        VBox body = new VBox(card);
        body.getStyleClass().add("body");
        body.setAlignment(Pos.TOP_CENTER);

        VBox heading = new VBox(lbl("Add a flight", "hero-title"), lbl("Fill in the details and confirm", "hero-sub"));
        return newScene(layout(heading, body, null));
    }

    Stage popup(String svg, String title, String msg, Button... btns) {
        Label t = lbl(title, "heading-label");
        t.setTextAlignment(TextAlignment.CENTER);

        HBox row = new HBox(10, btns);
        row.setAlignment(Pos.CENTER);

        VBox box = new VBox(14, icon(svg), t);
        if (msg != null) {
            Label m = lbl(msg, "card-text");
            m.setTextAlignment(TextAlignment.CENTER);
            box.getChildren().add(m);
        }
        box.getChildren().add(row);
        box.getStyleClass().add("dialog");
        box.setAlignment(Pos.CENTER);
        box.setFocusTraversable(true);

        Scene s = new Scene(box, 360, 280);
        s.getStylesheets().add(getClass().getResource("/com/example/style.css").toExternalForm());

        Stage st = new Stage();
        st.setScene(s);
        st.initModality(Modality.WINDOW_MODAL);
        st.initOwner(stage);
        return st;
    }

    void showFlights() {
        FlightIntel.ReadFile();
        flightsArea.clear();
        for (int i = 0; i < testingZone.flightList.size(); i++) {
            flightsArea.appendText((i + 1) + "- " + testingZone.flightList.get(i).getInfo() + "\n");
        }
        goTo("All Departures", listScene);
    }

    void updateHome() {
        int n = testingZone.flightList.size();
        total.setText(String.valueOf(n));
        recent.getChildren().clear();
        for (int i = 0; i < n && i < 4; i++) {
            Label l = new Label(String.valueOf(testingZone.flightList.get(i).getInfo()));
            l.getStyleClass().add("row-text");
            HBox row = new HBox(l);
            row.getStyleClass().add("row-card");
            recent.getChildren().add(row);
        }
        if (n == 0) recent.getChildren().add(lbl("No flights loaded yet.", "card-text"));
    }

    void clearFields() {
        for (TextField f : fields) f.clear();
    }

    @Override
    public void start(Stage primary) {
        stage = primary;
        FlightIntel.ReadFile();

        homeScene = homeScreen();
        listScene = listScreen();
        searchScene = searchScreen();
        addScene = addScreen();

        Button ok = new Button("OK");
        ok.getStyleClass().add("btn-primary");
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("btn-secondary");
        confirmStage = popup(planeSvg, "Confirm flight", "This flight will be added to the schedule.", ok, cancel);

        Button seeFlights = new Button("See Flights");
        seeFlights.getStyleClass().add("btn-primary");
        Button back = new Button("Back");
        back.getStyleClass().add("btn-secondary");
        resultStage = popup(checkSvg, "Flight has been added.", null, seeFlights, back);

        Button errBack = new Button("Back");
        errBack.getStyleClass().add("btn-secondary");
        errorStage = popup(crossSvg, "Invalid user input", null, errBack);

        cancel.setOnAction(e -> confirmStage.close());

        ok.setOnAction(e -> {
            FlightIntel.AddFlight(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    fields[3].getText(), fields[4].getText(), fields[5].getText());
            confirmStage.close();
            if (testingZone.cond[0] && testingZone.cond[1] && testingZone.cond[2] && testingZone.cond[3])
                resultStage.show();
            else
                errorStage.show();
        });

        back.setOnAction(e -> {
            resultStage.close();
            goTo("Add Flight", addScene);
        });

        seeFlights.setOnAction(e -> {
            resultStage.close();
            showFlights();
            clearFields();
        });

        errBack.setOnAction(e -> {
            errorStage.close();
            goTo("Add Flight", addScene);
        });

        goTo("Turkish Airlines", homeScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
