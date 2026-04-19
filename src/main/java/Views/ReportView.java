package Views;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportView {
    private VBox mainLayout;

    // Header Components
    public Label nameLabel = new Label("User: Loading...");
    public Label dateLabel = new Label();

    // Form Components
    public Slider moodSlider = new Slider(1, 10, 5);
    public Label moodValueLabel = new Label("5");
    public Label bmiDisplay = new Label("0.0");
    public Label bmrDisplay = new Label("0.0"); // Displays the activity-adjusted BMR

    public ComboBox<String> activityCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Sedentary (Little to no exercise)",
            "Lightly Active (1-3 days/week)",
            "Moderately Active (3-5 days/week)",
            "Very Active (6-7 days/week)",
            "Extra Active (Physical job/Training)"
    ));

    public Label caloricTotalLabel = new Label("0.0");
    public TextField caloricInput = new TextField();
    public Button addCalorieBtn = new Button("Add");

    public TextArea journalArea = new TextArea();

    public Button saveBtn = new Button("Save Report");
    public Button cancelBtn = new Button("Cancel");

    public ReportView() {
        buildUI();
    }

    private void buildUI() {
        mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #FFFDE7;"); // Soft Yellow

        // --- SECTION 1: HEADER ---
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d")));
        dateLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #5D4037;");
        header.getChildren().addAll(nameLabel, dateLabel);

        // --- SECTION 2: THE FORM ---
        GridPane form = new GridPane();
        form.setHgap(15); form.setVgap(15);
        form.setAlignment(Pos.CENTER);

        // Mood
        moodSlider.setShowTickLabels(true);
        moodSlider.setShowTickMarks(true);
        moodSlider.setMajorTickUnit(1);
        moodSlider.setSnapToTicks(true);
        form.add(new Label("Mood Score (1-10):"), 0, 0);
        form.add(new HBox(10, moodSlider, moodValueLabel), 1, 0);

        // Health Stats
        form.add(new Label("Calculated BMI:"), 0, 1);
        form.add(bmiDisplay, 1, 1);
        form.add(new Label("Calculated BMR:"), 0, 2);
        form.add(bmrDisplay, 1, 2);

        // Activity Level
        form.add(new Label("Activity Level:"), 0, 3);
        activityCombo.setValue("Sedentary (Little to no exercise)");
        form.add(activityCombo, 1, 3);

        // Calorie Intake
        form.add(new Label("Total Calories Intake:"), 0, 4);
        form.add(new HBox(5, caloricTotalLabel, new Label("kcal")), 1, 4);
        form.add(new Label("Add Calories:"), 0, 5);
        form.add(new HBox(10, caloricInput, addCalorieBtn), 1, 5);

        // Styling Stats
        bmiDisplay.setStyle("-fx-font-weight: bold; -fx-text-fill: #2E7D32;");
        bmrDisplay.setStyle("-fx-font-weight: bold; -fx-text-fill: #1565C0;");
        caloricTotalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // --- SECTION 3: JOURNAL ---
        VBox journalBox = new VBox(10);
        journalBox.setAlignment(Pos.CENTER_LEFT);
        journalArea.setPromptText("How was your day? Write notes here...");
        journalArea.setWrapText(true);
        journalArea.setPrefRowCount(6);
        journalArea.setMinHeight(120);
        journalArea.setStyle("-fx-control-inner-background: #FFFFFF; -fx-border-color: #FDD835;");
        journalBox.getChildren().addAll(new Label("Daily Journal:"), journalArea);

        // --- SECTION 4: FOOTER ---
        HBox actionButtons = new HBox(20, saveBtn, cancelBtn);
        actionButtons.setAlignment(Pos.CENTER);
        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-pref-width: 120;");

        mainLayout.getChildren().addAll(header, new Separator(), form, journalBox, actionButtons);
    }

    public VBox getView() { return mainLayout; }
}