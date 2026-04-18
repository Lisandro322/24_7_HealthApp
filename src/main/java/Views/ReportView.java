package Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportView {

    // These are public so the Controller can easily read/set their values
    public Label nameLabel = new Label("User: Loading...");
    public Label dateLabel = new Label();
    public Slider moodSlider = new Slider(1, 10, 5);
    public Label moodValueLabel = new Label("5");
    public Label bmiDisplay = new Label("0.0");
    public Label bmrDisplay = new Label("0.0");
    public Label caloricTotalLabel = new Label("0.0");
    public TextField caloricInput = new TextField();
    public Button addCalorieBtn = new Button("Add");
    public TextArea journalArea = new TextArea();
    public Button saveBtn = new Button("Save Report");
    public Button cancelBtn = new Button("Cancel");

    public VBox getView() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #FFFDE7;");

        // --- SECTION 1: HEADER (Name & Date) ---
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);

        // Format date: "April 18"
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d"));
        dateLabel.setText(formattedDate);
        dateLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #5D4037;");

        header.getChildren().addAll(nameLabel, dateLabel);

        // --- SECTION 2: THE FORM ---
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setAlignment(Pos.CENTER);

        // Mood Row
        form.add(new Label("Mood Score (1-10):"), 0, 0);
        moodSlider.setShowTickLabels(true);
        moodSlider.setShowTickMarks(true);
        moodSlider.setMajorTickUnit(1);
        moodSlider.setSnapToTicks(true);
        HBox moodBox = new HBox(10, moodSlider, moodValueLabel);
        form.add(moodBox, 1, 0);

        // BMI & BMR (Read Only)
        form.add(new Label("Calculated BMI:"), 0, 1);
        form.add(bmiDisplay, 1, 1);
        form.add(new Label("Calculated BMR:"), 0, 2);
        form.add(bmrDisplay, 1, 2);

        // Caloric Intake Row
        form.add(new Label("Total Calories:"), 0, 3);
        HBox calDisplayBox = new HBox(5, caloricTotalLabel, new Label("kcal"));
        form.add(calDisplayBox, 1, 3);

        form.add(new Label("Add Calories:"), 0, 4);
        HBox calInputBox = new HBox(10, caloricInput, addCalorieBtn);
        caloricInput.setPromptText("Enter amount");
        caloricInput.setPrefWidth(100);
        form.add(calInputBox, 1, 4);

        // Journal Section
        VBox journalBox = new VBox(10);
        journalBox.getChildren().add(new Label("Daily Journal:"));
        journalArea.setPromptText("How was your day? Any specific notes on symptoms or activities?");
        journalArea.setPrefRowCount(8);
        journalArea.setWrapText(true);
        bmiDisplay.setStyle("-fx-font-weight: bold; -fx-text-fill: #2E7D32;");
        bmrDisplay.setStyle("-fx-font-weight: bold; -fx-text-fill: #1565C0;");
        caloricTotalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        journalArea.setStyle("-fx-control-inner-background: #FFFFFF; -fx-border-color: #FDD835;");

        // --- SECTION 3: FOOTER BUTTONS ---
        HBox actionButtons = new HBox(20, saveBtn, cancelBtn);
        actionButtons.setAlignment(Pos.CENTER);
        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");


        // Assemble all parts
        mainLayout.getChildren().addAll(header, new Separator(), form, journalBox, actionButtons);

        return mainLayout;
    }
}