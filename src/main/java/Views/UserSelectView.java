package Views;

import Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class UserSelectView {
    private StackPane root;
    private VBox selectionContainer;
    private VBox creationContainer;

    // --- Selection Components ---
    public TableView<User> userTable = new TableView<>();
    public Button createNewUserBtn = new Button("Create New Profile");
    public Button selectUserBtn = new Button("Select User");

    // --- Creation Form Components ---
    public TextField firstNameField = new TextField();
    public TextField lastNameField = new TextField();
    public Spinner<Integer> ageSpinner = new Spinner<>(0, 120, 25);
    public ComboBox<String> genderBox = new ComboBox<>();
    public TextField weightField = new TextField();
    public TextField heightField = new TextField();
    public Button saveUserBtn = new Button("Save Profile");
    public Button cancelCreateBtn = new Button("Back to Selection");

    public UserSelectView() {
        root = new StackPane();
        root.setStyle("-fx-background-color: #FFF9C4;");

        setupSelectionContainer();
        setupCreationContainer();

        root.getChildren().addAll(selectionContainer, creationContainer);

        // Start with the table visible and form hidden
        showSelection();
    }

    private void setupSelectionContainer() {
        selectionContainer = new VBox(20);
        selectionContainer.setPadding(new Insets(40));
        selectionContainer.setAlignment(Pos.CENTER);

        Label title = new Label("Who is tracking today?");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");

        TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        userTable.getColumns().addAll(firstNameCol, lastNameCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setPrefHeight(250);

        HBox controls = new HBox(15, selectUserBtn, createNewUserBtn);
        controls.setAlignment(Pos.CENTER);

        selectionContainer.getChildren().addAll(title, userTable, controls);
    }

    private void setupCreationContainer() {
        creationContainer = new VBox(15);
        creationContainer.setPadding(new Insets(40));
        creationContainer.setAlignment(Pos.CENTER);

        Label title = new Label("Create New Profile");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        // Inputs
        genderBox.getItems().addAll("Male", "Female");
        genderBox.getSelectionModel().selectFirst();

        form.add(new Label("First Name:"), 0, 0);
        form.add(firstNameField, 1, 0);
        form.add(new Label("Last Name:"), 0, 1);
        form.add(lastNameField, 1, 1);
        form.add(new Label("Age:"), 0, 2);
        form.add(ageSpinner, 1, 2);
        form.add(new Label("Gender:"), 0, 3);
        form.add(genderBox, 1, 3);
        form.add(new Label("Weight (kg):"), 0, 4);
        form.add(weightField, 1, 4);
        form.add(new Label("Height (cm):"), 0, 5);
        form.add(heightField, 1, 5);

        HBox actions = new HBox(15, saveUserBtn, cancelCreateBtn);
        actions.setAlignment(Pos.CENTER);

        creationContainer.getChildren().addAll(title, form, actions);
    }

    public void showCreation() {
        selectionContainer.setVisible(false);
        selectionContainer.setManaged(false);
        creationContainer.setVisible(true);
        creationContainer.setManaged(true);
    }

    public void showSelection() {
        creationContainer.setVisible(false);
        creationContainer.setManaged(false);
        selectionContainer.setVisible(true);
        selectionContainer.setManaged(true);
    }

    public StackPane getRoot() {
        return root;
    }
}