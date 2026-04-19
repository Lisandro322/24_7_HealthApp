package Views;

import Models.DailyReport;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class GraphsView {
    private VBox mainLayout;

    // --- NAVIGATION & MODE COMPONENTS ---
    public Button lastWeekBtn = new Button("Last Week");
    public Button last4WeeksBtn = new Button("Last 4 Weeks");
    public ComboBox<String> viewModeCombo = new ComboBox<>(
            FXCollections.observableArrayList("Mood Score", "BMR vs Calories", "BMI")
    );

    // --- CHART COMPONENTS ---
    public BarChart<String, Number> barChart;
    public Label graphMessage = new Label("Showing data for the selected period.");

    // --- DYNAMIC SNAPSHOT VIEWER ---
    public VBox todaySnapshotBox = new VBox(10);
    public Label snapshotHeaderLabel = new Label("Today's Progress Snapshot");
    public Button resetToTodayBtn = new Button("Back to Today");

    public Label todayDateLabel = new Label("--");
    public Label todayMoodLabel = new Label("--");
    public Label todayBMILabel = new Label("--");
    public Label todayBaseBMRLabel = new Label("--"); // Added for Resting BMR
    public Label todayCalorieComparisonLabel = new Label("--"); // Energy vs Goal
    public TextArea todayJournalArea = new TextArea();

    // --- SEARCH HISTORY COMPONENTS ---
    public Button showHistoryBtn = new Button("Search Historical Reports");
    public VBox searchContainer = new VBox(15);
    public DatePicker startPicker = new DatePicker();
    public DatePicker endPicker = new DatePicker();
    public Button searchBtn = new Button("Search");
    public TableView<DailyReport> searchResultsTable = new TableView<>();

    public GraphsView() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time Period");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");
        barChart = new BarChart<>(xAxis, yAxis);

        buildUI();
    }

    private void buildUI() {
        mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: #FFFDE7;"); // Soft Yellow

        // 1. Navigation
        viewModeCombo.setValue("Mood Score");
        HBox graphNav = new HBox(15, lastWeekBtn, last4WeeksBtn, new Label("View:"), viewModeCombo);
        graphNav.setAlignment(Pos.CENTER);

        // 2. Chart
        barChart.setTitle("Health Progress Tracking");
        barChart.setPrefHeight(300);
        barChart.setAnimated(false);
        graphMessage.setStyle("-fx-font-style: italic; -fx-text-fill: #5D4037;");

        // 3. Snapshot Viewer
        setupSnapshotViewer();

        // 4. Search Section
        setupSearchSection();

        mainLayout.getChildren().addAll(
                graphNav, barChart, graphMessage,
                new Separator(),
                todaySnapshotBox,
                showHistoryBtn, searchContainer
        );
    }

    private void setupSnapshotViewer() {
        todaySnapshotBox.setPadding(new Insets(15));
        todaySnapshotBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #FBC02D; " +
                "-fx-border-radius: 10; -fx-background-radius: 10;");

        snapshotHeaderLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #E65100;");

        resetToTodayBtn.setVisible(false);
        resetToTodayBtn.setStyle("-fx-background-color: #FFECB3; -fx-text-fill: #5D4037; -fx-font-size: 11px;");

        HBox headerRow = new HBox(10, snapshotHeaderLabel, new Region(), resetToTodayBtn);
        HBox.setHgrow(headerRow.getChildren().get(1), Priority.ALWAYS);

        GridPane grid = new GridPane();
        grid.setHgap(30); grid.setVgap(8);
        grid.add(new Label("Date:"), 0, 0); grid.add(todayDateLabel, 1, 0);
        grid.add(new Label("Mood Score:"), 0, 1); grid.add(todayMoodLabel, 1, 1);
        grid.add(new Label("BMI:"), 0, 2); grid.add(todayBMILabel, 1, 2);
        grid.add(new Label("Base BMR (Resting):"), 0, 3); grid.add(todayBaseBMRLabel, 1, 3);
        grid.add(new Label("Energy vs Daily Goal:"), 0, 4); grid.add(todayCalorieComparisonLabel, 1, 4);

        todayJournalArea.setEditable(false);
        todayJournalArea.setWrapText(true);
        todayJournalArea.setPrefRowCount(3);
        todayJournalArea.setStyle("-fx-control-inner-background: #FFFDE7;");

        todaySnapshotBox.getChildren().addAll(headerRow, grid, new Label("Journal Entry:"), todayJournalArea);
    }

    private void setupSearchSection() {
        searchContainer.setVisible(false);
        searchContainer.setManaged(false);
        searchContainer.setStyle("-fx-border-color: #FBC02D; -fx-padding: 15; " +
                "-fx-background-color: #FFF9C4; -fx-background-radius: 10; -fx-border-radius: 10;");

        HBox controls = new HBox(10, new Label("From:"), startPicker, new Label("To:"), endPicker, searchBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        TableColumn<DailyReport, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<DailyReport, Void> actionCol = new TableColumn<>("Details");

        searchResultsTable.getColumns().setAll(dateCol, actionCol);
        searchResultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        searchResultsTable.setPrefHeight(200);

        searchContainer.getChildren().addAll(new Label("Historical Search"), controls, searchResultsTable);
    }

    public VBox getView() { return mainLayout; }
}