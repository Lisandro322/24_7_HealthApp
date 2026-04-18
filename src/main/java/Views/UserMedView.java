package Views;

import Models.MedicationInfo;
import Models.MedicationSchedule;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class UserMedView {

    private VBox mainLayout;

    // --- USER SECTION ---
    public Label userNameLabel = new Label("User: Loading...");
    public Button editUserBtn = new Button("Edit User Info");
    public VBox editUserContainer = new VBox(10);
    public TextField firstNameField = new TextField(), lastNameField = new TextField();
    public TextField weightField = new TextField(), heightField = new TextField();
    public Button saveUserChangesBtn = new Button("Save Changes");

    // --- MEDICATION SECTION ---
    public TableView<MedicationInfo> medTable = new TableView<>();
    public Button addMedBtn = new Button("Add New Medication");

    // Medication Form (Used for both ADD and EDIT)
    public VBox addMedForm = new VBox(10);
    public Label medFormTitle = new Label("Add New Medication");
    public TextField newMedName = new TextField();
    public TextField newMedDosage = new TextField();
    public Spinner<Integer> newMedDailyReq = new Spinner<>(1, 10, 1);
    public TextArea newMedNote = new TextArea();
    public Button saveNewMedBtn = new Button("Confirm Add Medication");
    public Button cancelMedBtn = new Button("Cancel");

    // --- DETAILS SECTION ---
    public Label detailsTitle = new Label("Medication Details");
    public Label detailText = new Label("Select a medication to see details.");
    public Button editMedBtn = new Button("Edit Med"), deleteMedBtn = new Button("Delete Med");
    public Button viewScheduleBtn = new Button("View Schedule");

    // --- SCHEDULE SECTION ---
    public VBox scheduleSection = new VBox(10);
    public TableView<MedicationSchedule> scheduleTable = new TableView<>();
    public Button addScheduleBtn = new Button("Add Time");
    public Button toggleScheduleBtn = new Button("Enable/Disable"), deleteScheduleBtn = new Button("Delete Time");

    // Schedule Creation Form
    public VBox addScheduleForm = new VBox(10);
    public Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 12);
    public Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0);
    public Button saveNewSchedBtn = new Button("Confirm Add Time");

    public UserMedView() {
        buildUI();
    }

    private void buildUI() {
        mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #FFFDE7;"); // Soft Yellow

        // 1. User Section
        setupUserSection();
        userNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");
        editUserBtn.setStyle("-fx-background-color: #FBC02D; -fx-text-fill: white;");

        // 2. Medication Header & Form
        HBox medHeader = new HBox(new Label("Prescribed Medications"), new Region(), addMedBtn);
        ((Label)medHeader.getChildren().get(0)).setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox.setHgrow(medHeader.getChildren().get(1), Priority.ALWAYS);
        setupMedForm();
        setupMedTable();

        // 3. Details Container
        VBox detailsContainer = new VBox(10, detailsTitle, detailText, new HBox(10, editMedBtn, deleteMedBtn, viewScheduleBtn));
        detailsContainer.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #FDD835; -fx-padding: 15; -fx-border-radius: 5;");

        // 4. Schedule Section
        setupScheduleForm();
        setupScheduleTable();
        HBox schedActions = new HBox(10, addScheduleBtn, toggleScheduleBtn, deleteScheduleBtn);
        scheduleSection.getChildren().addAll(new Label("Associated Schedules"), addScheduleForm, scheduleTable, schedActions);
        scheduleSection.setVisible(false);
        scheduleSection.setManaged(false);

        mainLayout.getChildren().addAll(
                userNameLabel, editUserBtn, editUserContainer,
                new Separator(),
                medHeader, addMedForm, medTable,
                detailsContainer, scheduleSection
        );
    }

    private void setupMedForm() {
        addMedForm.setVisible(false);
        addMedForm.setManaged(false);
        addMedForm.setStyle("-fx-background-color: #E8F5E9; -fx-padding: 15; -fx-border-color: #4CAF50; -fx-border-radius: 5;");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0); grid.add(newMedName, 1, 0);
        grid.add(new Label("Dosage:"), 0, 1); grid.add(newMedDosage, 1, 1);
        grid.add(new Label("Daily Req:"), 0, 2); grid.add(newMedDailyReq, 1, 2);
        grid.add(new Label("Notes:"), 0, 3); grid.add(newMedNote, 1, 3);
        newMedNote.setPrefRowCount(3);

        HBox formButtons = new HBox(10, saveNewMedBtn, cancelMedBtn);
        addMedForm.getChildren().addAll(medFormTitle, grid, formButtons);
    }

    private void setupUserSection() {
        editUserContainer.setVisible(false);
        editUserContainer.setManaged(false);
        editUserContainer.setStyle("-fx-background-color: #FFF9C4; -fx-padding: 15; -fx-border-color: #FBC02D; -fx-border-radius: 5;");
        GridPane userForm = new GridPane();
        userForm.setHgap(10); userForm.setVgap(10);
        userForm.add(new Label("First Name:"), 0, 0); userForm.add(firstNameField, 1, 0);
        userForm.add(new Label("Last Name:"), 0, 1); userForm.add(lastNameField, 1, 1);
        userForm.add(new Label("Weight (kg):"), 0, 2); userForm.add(weightField, 1, 2);
        userForm.add(new Label("Height (cm):"), 0, 3); userForm.add(heightField, 1, 3);
        editUserContainer.getChildren().addAll(new Label("Modify User Profile"), userForm, saveUserChangesBtn);
    }

    private void setupMedTable() {
        medTable.setEditable(false); // TABLE EDITING DISABLED
        TableColumn<MedicationInfo, String> nameCol = new TableColumn<>("Medication");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("medName"));
        TableColumn<MedicationInfo, String> dosageCol = new TableColumn<>("Dosage");
        dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));

        medTable.getColumns().setAll(nameCol, dosageCol);
        medTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        medTable.setPrefHeight(180);
    }

    private void setupScheduleForm() {
        addScheduleForm.setVisible(false);
        addScheduleForm.setManaged(false);
        addScheduleForm.setStyle("-fx-background-color: #E3F2FD; -fx-padding: 10; -fx-border-color: #2196F3; -fx-border-radius: 5;");
        hourSpinner.setPrefWidth(70); minuteSpinner.setPrefWidth(70);
        HBox timePicker = new HBox(5, new Label("Time:"), hourSpinner, new Label(":"), minuteSpinner);
        timePicker.setAlignment(Pos.CENTER_LEFT);
        addScheduleForm.getChildren().addAll(new Label("Add New Schedule Entry"), new HBox(15, timePicker, saveNewSchedBtn));
    }

    private void setupScheduleTable() {
        TableColumn<MedicationSchedule, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<MedicationSchedule, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        scheduleTable.getColumns().setAll(timeCol, statusCol);
        scheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        scheduleTable.setPrefHeight(120);
    }

    public VBox getView() { return mainLayout; }
}