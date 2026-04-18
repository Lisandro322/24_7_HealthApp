package Views;

import Models.MedicationSchedule;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class DefaultView {
    private VBox mainLayout;

    // Components exposed for the Controller
    public Label welcomeLabel = new Label("Welcome, User!");
    public TableView<MedicationSchedule> todayScheduleTable = new TableView<>();

    // Alert components
    public VBox alertBox = new VBox(15);
    public Label alertMedName = new Label("[Medication Name]");
    public Button takenBtn = new Button("Taken");
    public Button silenceBtn = new Button("Silence");

    // DEBUG: Trigger Test Alert
    public Button triggerTestAlertBtn = new Button("DEBUG: Trigger Alert");

    public DefaultView() {
        // Build the UI exactly once when the class is instantiated
        buildUI();
    }

    private void buildUI() {
        mainLayout = new VBox(25);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // Match the Soft Yellow Theme of other views
        mainLayout.setStyle("-fx-background-color: #FFFDE7;");

        // --- SECTION 1: WELCOME HEADER ---
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        welcomeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");
        header.getChildren().add(welcomeLabel);

        // --- SECTION 2: MEDICATION ALERT (Contextual) ---
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setPadding(new Insets(20));
        alertBox.setStyle("-fx-background-color: #FFF176; -fx-border-color: #FBC02D; " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label alertTitle = new Label("MEDICATION REMINDER");
        alertTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #E65100;");

        alertMedName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox alertActions = new HBox(20, takenBtn, silenceBtn);
        alertActions.setAlignment(Pos.CENTER);
        takenBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-pref-width: 100;");
        silenceBtn.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-pref-width: 100;");

        alertBox.getChildren().addAll(alertTitle, alertMedName, alertActions);

        // Hide by default
        alertBox.setVisible(false);
        alertBox.setManaged(false);

        // --- SECTION 3: DAILY SCHEDULE TABLE ---
        VBox tableSection = new VBox(10);
        Label tableTitle = new Label("Your Schedule for Today");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableColumn<MedicationSchedule, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<MedicationSchedule, String> medCol = new TableColumn<>("Medication Name");
        medCol.setCellValueFactory(new PropertyValueFactory<>("medicationName"));

        TableColumn<MedicationSchedule, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        todayScheduleTable.getColumns().setAll(timeCol, medCol, statusCol);
        todayScheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        todayScheduleTable.setPrefHeight(250);
        todayScheduleTable.setStyle("-fx-selection-bar: #FFF59D;");

        tableSection.getChildren().addAll(tableTitle, todayScheduleTable);

        // Debug Button Container
        HBox debugBox = new HBox(triggerTestAlertBtn);
        debugBox.setAlignment(Pos.CENTER);

        // Assemble all components into the mainLayout field
        mainLayout.getChildren().addAll(header, debugBox, alertBox, tableSection);
    }

    /**
     * Returns the pre-built layout.
     * Crucial: Does NOT call addAll() again.
     */
    public VBox getView() {
        return mainLayout;
    }
}