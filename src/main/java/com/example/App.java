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

    private static final String PLANE  = "M3 11.5L21 3l-5 18-4.5-7.5L3 11.5z M11.5 13.5L21 3";
    private static final String SEARCH = "M10.5 4a6.5 6.5 0 1 0 0 13a6.5 6.5 0 0 0 0-13z M20 20l-4.8-4.8";
    private static final String PLUS   = "M12 5v14M5 12h14";
    private static final String CHECK  = "M5 12.5l4.5 4.5L19 7.5";
    private static final String CROSS  = "M6 6l12 12M18 6L6 18";

    private static final String[] PROMPTS = {"TK Number", "Airport", "Airport", "Time", "Time", "Plane No"};
    private static final String[] FIELD_NAMES = {"Flight No:", "From:", "To:", "Departure:", "Arrival:", "Aircraft:"};

    private Stage stage, confirmStage, resultStage, errorStage;
    private Scene homeScene, listScene, searchScene, addScene;

    private final Label statNumber = label("0", "stat-number");
    private final VBox previewBox = new VBox(10);
    private final TextArea flightText = new TextArea();
    private final TextField[] addFields = new TextField[6];

    private Label label(String text, String style) {
        Label l = new Label(text);
        l.getStyleClass().add(style);
        l.setWrapText(true);
        return l;
    }

    private Button button(String text, String... styles) {
        Button b = new Button(text);
        b.getStyleClass().addAll(styles);
        return b;
    }

    private Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        VBox.setVgrow(r, Priority.ALWAYS);
        return r;
    }

    private StackPane iconChip(String path) {
        SVGPath icon = new SVGPath();
        icon.setContent(path);
        icon.getStyleClass().add("icon");
        StackPane chip = new StackPane(icon);
        chip.getStyleClass().add("icon-chip");
        return chip;
    }

    private VBox card(double gap, Node... children) {
        VBox c = new VBox(gap, children);
        c.getStyleClass().add("card");
        return c;
    }

    private VBox actionCard(String icon, String title, String text, Button button) {
        return card(8, iconChip(icon), label(title, "card-title"), label(text, "card-text"), spacer(), button);
    }

    private VBox body(VBox card) {
        VBox b = new VBox(card);
        b.getStyleClass().add("body");
        b.setAlignment(Pos.TOP_CENTER);
        return b;
    }

    private VBox titleBlock(String title, String sub) {
        return new VBox(label(title, "hero-title"), label(sub, "hero-sub"));
    }

    private void threeColumns(GridPane grid) {
        for (int i = 0; i < 3; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(100.0 / 3);
            grid.getColumnConstraints().add(c);
        }
    }

    private HBox header(Node... extra) {
        StackPane logo = new StackPane(new Label("TK"));
        logo.getStyleClass().add("logo");
        VBox brand = new VBox(label("Turkish Airlines", "brand-name"), label("Staff Dashboard", "brand-sub"));
        brand.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane(new Label("ST"));
        avatar.getStyleClass().add("avatar");
        VBox who = new VBox(label("Staff", "user-name"), label("Signed in", "user-role"));
        who.setAlignment(Pos.CENTER_LEFT);
        HBox user = new HBox(10, avatar, who);
        user.setAlignment(Pos.CENTER_LEFT);

        HBox h = new HBox(16, logo, brand, spacer(), user);
        h.getChildren().addAll(extra);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }

    private BorderPane page(Node heading, Node body, Node... headerButtons) {
        VBox zone = new VBox(30, header(headerButtons), heading);
        zone.getStyleClass().add("header-zone");
        BorderPane p = new BorderPane(body, zone, null, null, null);
        p.getStyleClass().add("page");
        p.setFocusTraversable(true); 
        return p;
    }

    private Scene scene(Region root, double w, double h) {
        Scene s = new Scene(root, w, h);
        s.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        return s;
    }

    private void show(String title, Scene s) {
        stage.setTitle(title);
        stage.setScene(s);
        if (s == homeScene) refreshHome();
    }

    private Scene buildHome() {
        Button seeFlights = button("Adjust/See Flights", "btn-primary", "btn-lg");
        Button search = button("Search", "btn-dark");
        Button add = button("Add", "btn-primary");
        Button logOut = button("Log Out", "btn-dark");

        seeFlights.setOnAction(e -> showFlights());
        search.setOnAction(e -> show("Search for Flight", searchScene));
        add.setOnAction(e -> show("Add Flight", addScene));
        logOut.setOnAction(e -> Platform.exit());

        LocalDate today = LocalDate.now();
        StackPane dateBadge = new StackPane(label(String.valueOf(today.getDayOfMonth()), "date-number"));
        dateBadge.getStyleClass().add("date-badge");
        VBox dateText = new VBox(
                label(today.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ",", "date-text"),
                label(today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), "date-text"));
        dateText.setAlignment(Pos.CENTER_LEFT);
        Region divider = new Region();
        divider.getStyleClass().add("divider");
        VBox welcome = titleBlock("Welcome to the staff ", "What would you like to do today?");
        welcome.setAlignment(Pos.CENTER_LEFT);
        HBox hero = new HBox(18, dateBadge, dateText, divider, seeFlights, spacer(), welcome);
        hero.setAlignment(Pos.CENTER_LEFT);

        ImageView photo = new ImageView(new Image("file:plane.jpg"));
        photo.setPreserveRatio(true);
        photo.setFitWidth(300);
        Rectangle clip = new Rectangle();
        clip.setArcWidth(36);
        clip.setArcHeight(36);
        photo.layoutBoundsProperty().addListener((obs, old, b) -> {
            clip.setWidth(b.getWidth());
            clip.setHeight(b.getHeight());
        });
        photo.setClip(clip);
        StackPane imageCard = new StackPane(photo);
        imageCard.getStyleClass().add("card-image");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.getStyleClass().add("body");
        threeColumns(grid);
        RowConstraints bottom = new RowConstraints();
        bottom.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().addAll(new RowConstraints(), bottom);

        grid.add(card(8, iconChip(PLANE), label("Total departures", "card-text"), statNumber,
                label("flights on the schedule", "card-text")), 0, 0);
        grid.add(actionCard(SEARCH, "Find a flight", "Look up a flight by its TK number.", search), 1, 0);
        grid.add(actionCard(PLUS, "Add a flight", "Enter the details of a new departure.", add), 2, 0);
        grid.add(card(16, label("Recent departures", "card-title"), previewBox), 0, 1, 2, 1);
        grid.add(imageCard, 2, 1);

        return scene(page(hero, grid, logOut), 1120, 740);
    }

    private Scene buildList() {
        Button back = button("Back", "btn-secondary");
        back.setOnAction(e -> show("Turkish Airlines", homeScene));

        flightText.setWrapText(true);
        VBox.setVgrow(flightText, Priority.ALWAYS);
        VBox card = card(0, flightText);
        VBox.setVgrow(card, Priority.ALWAYS);

        return scene(page(titleBlock("All departures", "Every scheduled flight in one place"), body(card), back), 1120, 740);
    }

    private Scene buildSearch() {
        TextField input = new TextField();
        input.setPromptText("TK:Number");
        input.getStyleClass().add("search-field");
        input.setFocusTraversable(false);
        HBox.setHgrow(input, Priority.ALWAYS);

        Button go = button("Look for flights!", "btn-primary");
        Button back = button("Back", "btn-secondary");
        TextArea output = new TextArea();
        VBox.setVgrow(output, Priority.ALWAYS);

        go.setOnAction(e -> {
            try {
                FlightIntel found = FlightIntel.SearchForFlight(input.getText());
                output.setText(found == null ? "No flight found" : String.valueOf(found));
            } catch (Exception ex) {
                System.out.println("Error");
            }
        });
        back.setOnAction(e -> {
            input.clear();
            output.clear();
            show("Turkish Airlines", homeScene);
        });

        HBox row = new HBox(12, input, go);
        row.setAlignment(Pos.CENTER_LEFT);
        VBox card = card(18, label("Search for your flight", "heading-label"), row, output);
        card.setMaxWidth(760);
        VBox.setVgrow(card, Priority.ALWAYS);

        return scene(page(titleBlock("Search", "Find a flight by its number"), body(card), back), 1120, 740);
    }

    private Scene buildAdd() {
        GridPane form = new GridPane();
        form.setHgap(18);
        form.setVgap(22);
        threeColumns(form);
        for (int i = 0; i < addFields.length; i++) {
            addFields[i] = new TextField();
            addFields[i].setPromptText(PROMPTS[i]);
            addFields[i].setMaxWidth(Double.MAX_VALUE);
            form.add(new VBox(8, label(FIELD_NAMES[i], "field-label"), addFields[i]), i % 3, i / 3);
        }

        Button back = button("Back", "btn-secondary");
        Button add = button("Add Flight", "btn-primary");
        back.setOnAction(e -> {
            clearForm();
            show("Turkish Airlines", homeScene);
        });
        add.setOnAction(e -> {
            stage.setTitle("Confirm");
            confirmStage.show();
        });

        HBox buttons = new HBox(12, back, add);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        VBox card = card(26, label("Please enter the intels that belong to your flight plan.", "heading-label"), form, buttons);
        card.setMaxWidth(860);

        return scene(page(titleBlock("Add a flight", "Fill in the details and confirm"), body(card)), 1120, 740);
    }

    private Stage dialog(String icon, String title, String text, Button... buttons) {
        Label heading = label(title, "heading-label");
        heading.setTextAlignment(TextAlignment.CENTER);
        HBox row = new HBox(10, buttons);
        row.setAlignment(Pos.CENTER);

        VBox box = new VBox(14, iconChip(icon), heading);
        if (text != null) {
            Label sub = label(text, "card-text");
            sub.setTextAlignment(TextAlignment.CENTER);
            box.getChildren().add(sub);
        }
        box.getChildren().add(row);
        box.getStyleClass().add("dialog");
        box.setAlignment(Pos.CENTER);
        box.setFocusTraversable(true);

        Stage s = new Stage();
        s.setScene(scene(box, 360, 280));
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(stage);
        return s;
    }

    // ---------- actions ----------

    private void showFlights() {
        FlightIntel.ReadFile(); // always list the latest data from the file
        flightText.clear();
        for (int i = 0; i < testingZone.flightList.size(); i++)
            flightText.appendText((i + 1) + "- " + testingZone.flightList.get(i).getInfo() + "\n");
        show("All Departures", listScene);
    }

    private void refreshHome() {
        int n = testingZone.flightList.size();
        statNumber.setText(String.valueOf(n));
        previewBox.getChildren().clear();
        for (int i = 0; i < Math.min(4, n); i++) {
            Label text = new Label(String.valueOf(testingZone.flightList.get(i).getInfo()));
            text.getStyleClass().add("row-text");
            HBox row = new HBox(text);
            row.getStyleClass().add("row-card");
            previewBox.getChildren().add(row);
        }
        if (n == 0) previewBox.getChildren().add(label("No flights loaded yet.", "card-text"));
    }

    private void clearForm() {
        for (TextField f : addFields) f.clear();
    }

    @Override
    public void start(Stage primary) {
        stage = primary;
        FlightIntel.ReadFile();

        homeScene = buildHome();
        listScene = buildList();
        searchScene = buildSearch();
        addScene = buildAdd();

        Button ok = button("OK", "btn-primary");
        Button cancel = button("Cancel", "btn-secondary");
        confirmStage = dialog(PLANE, "Confirm flight", "This flight will be added to the schedule.", ok, cancel);

        Button seeFlights = button("See Flights", "btn-primary");
        Button back = button("Back", "btn-secondary");
        resultStage = dialog(CHECK, "Flight has been added.", null, seeFlights, back);

        Button errorBack = button("Back", "btn-secondary");
        errorStage = dialog(CROSS, "Invalid user input", null, errorBack);

        cancel.setOnAction(e -> confirmStage.close());
        ok.setOnAction(e -> {
            FlightIntel.AddFlight(addFields[0].getText(), addFields[1].getText(), addFields[2].getText(),
                    addFields[3].getText(), addFields[4].getText(), addFields[5].getText());
            confirmStage.close();
            boolean valid = testingZone.cond[0] && testingZone.cond[1] && testingZone.cond[2] && testingZone.cond[3];
            if (valid) resultStage.show();
            else errorStage.show();
        });
        back.setOnAction(e -> {
            resultStage.close();
            show("Add Flight", addScene);
        });
        seeFlights.setOnAction(e -> {
            resultStage.close();
            showFlights();
            clearForm();
        });
        errorBack.setOnAction(e -> {
            errorStage.close();
            show("Add Flight", addScene);
        });

        show("Turkish Airlines", homeScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
