package Controllers;

import Models.*;
import DAO.*;
import Views.ReportView;
import javafx.scene.control.Alert;
import java.time.LocalDate;

public class ReportController {
    private final ReportView view;
    private final User activeUser;
    private final DailyReportDAO reportDAO = new DailyReportDAO();

    private DailyReport currentReport;
    private double totalCalories = 0.0;

    public ReportController(ReportView view, User activeUser) {
        this.view = view;
        this.activeUser = activeUser;

        initializeTodayReport();
        setupActions();
    }

    /**
     * Resets or loads the report for the current date.
     * Public so MenuController can trigger a refresh when switching tabs.
     */
    public void initializeTodayReport() {
        String today = LocalDate.now().toString();
        DailyReport existing = reportDAO.getReportByDate(activeUser.getId(), today);

        if (existing != null) {
            currentReport = existing;
            this.totalCalories = existing.getCaloricIntake();
        } else {
            currentReport = new DailyReport();
            currentReport.setUserId(activeUser.getId());
            currentReport.setDate(today);
            currentReport.setMoodScore(5); // Default middle value
            currentReport.setJournalLog("");
            this.totalCalories = 0.0;
        }

        calculateAndSetHealthMetrics();
        refreshView();
    }

    private void calculateAndSetHealthMetrics() {
        // 1. BMI Calculation
        double heightMeters = activeUser.getHeight() / 100.0;
        if (heightMeters > 0) {
            double bmi = activeUser.getWeight() / (heightMeters * heightMeters);
            currentReport.setBmi(bmi);
        }

        // 2. BMR Calculation (Mifflin-St Jeor Equation)
        double bmr;
        double w = activeUser.getWeight();
        double h = activeUser.getHeight();
        int a = activeUser.getAge();

        if ("Male".equalsIgnoreCase(activeUser.getGender())) {
            bmr = (10 * w) + (6.25 * h) - (5 * a) + 5;
        } else {
            bmr = (10 * w) + (6.25 * h) - (5 * a) - 161;
        }
        currentReport.setBmr(bmr);
    }

    private void refreshView() {
        // Header
        view.nameLabel.setText("User: " + activeUser.getFirstName() + " " + activeUser.getLastName());

        // Stats
        view.bmiDisplay.setText(String.format("%.2f", currentReport.getBmi()));
        view.bmrDisplay.setText(String.format("%.2f", currentReport.getBmr()));
        view.caloricTotalLabel.setText(String.format("%.1f", totalCalories));

        // Form Fields
        view.moodSlider.setValue(currentReport.getMoodScore());
        view.moodValueLabel.setText(String.valueOf(currentReport.getMoodScore()));
        view.journalArea.setText(currentReport.getJournalLog());
    }

    private void setupActions() {
        // Update the mood number as the slider moves
        view.moodSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            view.moodValueLabel.setText(String.valueOf(newVal.intValue()));
        });

        // Add Calories Logic
        view.addCalorieBtn.setOnAction(e -> {
            String input = view.caloricInput.getText();
            if (input.matches("\\d+(\\.\\d+)?")) {
                double addedCals = Double.parseDouble(input);
                totalCalories += addedCals;
                view.caloricTotalLabel.setText(String.format("%.1f", totalCalories));
                view.caloricInput.clear();
            } else {
                showWarning("Invalid Input", "Please enter a numeric value for calories.");
            }
        });

        // Save Logic
        view.saveBtn.setOnAction(e -> {
            currentReport.setMoodScore((int) view.moodSlider.getValue());
            currentReport.setCaloricIntake(totalCalories);
            currentReport.setJournalLog(view.journalArea.getText());

            reportDAO.upsertReport(currentReport);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Daily report saved for " + currentReport.getDate());
            alert.showAndWait();
        });

        // Cancel Logic - Reverts changes to the last saved state
        view.cancelBtn.setOnAction(e -> initializeTodayReport());
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}