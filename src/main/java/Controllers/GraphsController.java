package Controllers;

import Models.*;
import DAO.*;
import Views.GraphsView;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.*;

public class GraphsController {
    private final GraphsView view;
    private final User activeUser;
    private final DailyReportDAO reportDAO = new DailyReportDAO();
    private List<DailyReport> currentReports;
    private int currentDayRange = 7;

    public GraphsController(GraphsView view, User activeUser) {
        this.view = view;
        this.activeUser = activeUser;

        setupActions();
        setupSearchTableButtons();
        refresh();
    }

    private void setupActions() {
        view.lastWeekBtn.setOnAction(e -> { currentDayRange = 7; refresh(); });
        view.last4WeeksBtn.setOnAction(e -> { currentDayRange = 28; refresh(); });
        view.viewModeCombo.setOnAction(e -> refresh());
        view.resetToTodayBtn.setOnAction(e -> displayReportInSnapshot(findToday(currentReports), true));
        view.showHistoryBtn.setOnAction(e -> {
            view.searchContainer.setVisible(true);
            view.searchContainer.setManaged(true);
            view.showHistoryBtn.setVisible(false);
        });
        view.searchBtn.setOnAction(e -> handleSearch());
    }

    public void refresh() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(27); // Fetch enough to cover monthly view
        currentReports = reportDAO.getReportsInRange(activeUser.getId(), start.toString(), end.toString());

        displayReportInSnapshot(findToday(currentReports), true);

        String mode = view.viewModeCombo.getValue();
        if (currentDayRange == 28) updateChartMonthly(currentReports, mode);
        else updateChartDaily(currentReports.subList(Math.max(0, currentReports.size() - 7), currentReports.size()), mode);

        updateMotivationalMessage(currentReports);
    }

    private void displayReportInSnapshot(DailyReport report, boolean isToday) {
        if (report != null) {
            view.todayDateLabel.setText(report.getDate());
            view.todayMoodLabel.setText(report.getMoodScore() + " / 10");
            view.todayBMILabel.setText(String.format("%.2f", report.getBmi()));
            view.todayJournalArea.setText(report.getJournalLog());

            // 1. Re-calculate Base Resting BMR
            double w = activeUser.getWeight(); double h = activeUser.getHeight(); int a = activeUser.getAge();
            double baseBmr = "Male".equalsIgnoreCase(activeUser.getGender()) ?
                    (10 * w) + (6.25 * h) - (5 * a) + 5 :
                    (10 * w) + (6.25 * h) - (5 * a) - 161;
            view.todayBaseBMRLabel.setText(String.format("%.2f kcal", baseBmr));

            // 2. Handle Energy vs Goal (TDEE)
            double intake = report.getCaloricIntake();
            double energyGoal = report.getBmr(); // Database saved value is TDEE
            double percentage = (energyGoal > 0) ? (intake / energyGoal) * 100 : 0;

            view.todayCalorieComparisonLabel.setText(String.format("%.0f / %.0f kcal (%.1f%%)", intake, energyGoal, percentage));
            view.todayCalorieComparisonLabel.setStyle(intake < energyGoal ? "-fx-text-fill: #D32F2F; -fx-font-weight: bold;" : "-fx-text-fill: #388E3C; -fx-font-weight: bold;");

            view.snapshotHeaderLabel.setText(isToday ? "Today's Progress Snapshot" : "Viewing Historical Report: " + report.getDate());
            view.resetToTodayBtn.setVisible(!isToday);
        } else {
            clearSnapshot();
        }
    }

    private void setupSearchTableButtons() {
        TableColumn<DailyReport, Void> actionCol = (TableColumn<DailyReport, Void>) view.searchResultsTable.getColumns().get(1);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("View Details");
            {
                btn.setStyle("-fx-background-color: #FBC02D; -fx-text-fill: white;");
                btn.setOnAction(event -> {
                    DailyReport data = getTableView().getItems().get(getIndex());
                    displayReportInSnapshot(data, data.getDate().equals(LocalDate.now().toString()));
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void updateChartDaily(List<DailyReport> reports, String mode) {
        view.barChart.getData().clear();
        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        XYChart.Series<String, Number> s2 = new XYChart.Series<>();
        s1.setName(mode);

        double total = 0;
        for (DailyReport r : reports) {
            double val1 = getVal(r, mode, true);
            s1.getData().add(new XYChart.Data<>(r.getDate(), val1));
            total += val1;
            if (mode.equals("BMR vs Calories")) {
                s1.setName("Daily Goal"); s2.setName("Intake");
                s2.getData().add(new XYChart.Data<>(r.getDate(), r.getCaloricIntake()));
            }
        }
        if (!reports.isEmpty() && !mode.equals("BMR vs Calories")) {
            s1.getData().add(new XYChart.Data<>("AVG (" + reports.size() + "d)", total / reports.size()));
            view.barChart.getData().add(s1);
        } else if (mode.equals("BMR vs Calories")) {
            view.barChart.getData().addAll(s1, s2);
        }
    }

    private void updateChartMonthly(List<DailyReport> reports, String mode) {
        view.barChart.getData().clear();
        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        XYChart.Series<String, Number> s2 = new XYChart.Series<>();
        for (int i = 0; i < 4; i++) {
            int start = i * 7; int end = Math.min(start + 7, reports.size());
            if (start >= reports.size()) break;
            List<DailyReport> week = reports.subList(start, end);
            double avg1 = week.stream().mapToDouble(r -> getVal(r, mode, true)).average().orElse(0);
            double avg2 = week.stream().mapToDouble(r -> getVal(r, mode, false)).average().orElse(0);
            s1.getData().add(new XYChart.Data<>("Week " + (i + 1), avg1));
            if (mode.equals("BMR vs Calories")) s2.getData().add(new XYChart.Data<>("Week " + (i + 1), avg2));
        }
        s1.setName(mode.equals("BMR vs Calories") ? "Avg Goal" : "Avg " + mode);
        s2.setName("Avg Intake");
        if (mode.equals("BMR vs Calories")) view.barChart.getData().addAll(s1, s2);
        else view.barChart.getData().add(s1);
    }

    private double getVal(DailyReport r, String mode, boolean primary) {
        if (mode.equals("Mood Score")) return r.getMoodScore();
        if (mode.equals("BMI")) return r.getBmi();
        return primary ? r.getBmr() : r.getCaloricIntake();
    }

    private DailyReport findToday(List<DailyReport> reports) {
        String today = LocalDate.now().toString();
        return reports.stream().filter(r -> r.getDate().equals(today)).findFirst().orElse(null);
    }

    private void handleSearch() {
        if (view.startPicker.getValue() != null && view.endPicker.getValue() != null) {
            List<DailyReport> results = reportDAO.getReportsInRange(activeUser.getId(), view.startPicker.getValue().toString(), view.endPicker.getValue().toString());
            view.searchResultsTable.setItems(FXCollections.observableArrayList(results));
        }
    }

    private void updateMotivationalMessage(List<DailyReport> reports) {
        if (reports.isEmpty()) { view.graphMessage.setText("No data logged for this period."); return; }
        DailyReport last = reports.get(reports.size() - 1);
        view.graphMessage.setText(last.getCaloricIntake() < last.getBmr() ? "Heads up! Your intake is below your daily goal." : "Great job! You're meeting your caloric needs.");
    }

    private void clearSnapshot() {
        view.todayDateLabel.setText("--"); view.todayMoodLabel.setText("No log found");
        view.todayBMILabel.setText("--"); view.todayBaseBMRLabel.setText("--");
        view.todayCalorieComparisonLabel.setText("--");
        view.todayJournalArea.setText("Select a report from history or log today's progress.");
        view.resetToTodayBtn.setVisible(false);
    }
}