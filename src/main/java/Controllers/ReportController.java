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
    private MenuController menuController;

    private DailyReport currentReport;
    private double totalCalories = 0.0;

    public ReportController(ReportView view, User activeUser) {
        this.view = view;
        this.activeUser = activeUser;
        initializeTodayReport();
        setupActions();
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

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
            currentReport.setMoodScore(5);
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
            currentReport.setBmi(Math.round(bmi * 100.0) / 100.0);
        }

        // 2. Base BMR Calculation (Mifflin-St Jeor)
        double baseBmr;
        double w = activeUser.getWeight();
        double h = activeUser.getHeight();
        int a = activeUser.getAge();

        if ("Male".equalsIgnoreCase(activeUser.getGender())) {
            baseBmr = (10 * w) + (6.25 * h) - (5 * a) + 5;
        } else {
            baseBmr = (10 * w) + (6.25 * h) - (5 * a) - 161;
        }

        // 3. Apply Multiplier (TDEE) and save as "BMR"
        double multiplier = getActivityMultiplier(view.activityCombo.getValue());
        double tdee = baseBmr * multiplier;
        currentReport.setBmr(Math.round(tdee * 100.0) / 100.0);

        // Update display
        view.bmrDisplay.setText(String.format("%.2f kcal", currentReport.getBmr()));
    }

    private double getActivityMultiplier(String level) {
        if (level.contains("Lightly")) return 1.375;
        if (level.contains("Moderately")) return 1.55;
        if (level.contains("Very Active")) return 1.725;
        if (level.contains("Extra Active")) return 1.9;
        return 1.2; // Sedentary (Default)
    }

    private void refreshView() {
        view.nameLabel.setText("User: " + activeUser.getFirstName() + " " + activeUser.getLastName());
        view.bmiDisplay.setText(String.format("%.2f", currentReport.getBmi()));
        view.caloricTotalLabel.setText(String.format("%.2f", totalCalories));
        view.moodSlider.setValue(currentReport.getMoodScore());
        view.moodValueLabel.setText(String.valueOf(currentReport.getMoodScore()));
        view.journalArea.setText(currentReport.getJournalLog());
    }

    private void setupActions() {
        // Sync mood label with slider
        view.moodSlider.valueProperty().addListener((obs, old, newVal) ->
                view.moodValueLabel.setText(String.valueOf(newVal.intValue())));

        // Recalculate BMR/TDEE on dropdown change
        view.activityCombo.setOnAction(e -> calculateAndSetHealthMetrics());

        // Calorie Input
        view.addCalorieBtn.setOnAction(e -> {
            String input = view.caloricInput.getText();
            if (input.matches("\\d+(\\.\\d+)?")) {
                double added = Double.parseDouble(input);
                totalCalories += added;
                totalCalories = Math.round(totalCalories * 100.0) / 100.0;
                view.caloricTotalLabel.setText(String.format("%.2f", totalCalories));
                view.caloricInput.clear();
            }
        });

        // Save and Navigate Home
        view.saveBtn.setOnAction(e -> {
            currentReport.setMoodScore((int) view.moodSlider.getValue());
            currentReport.setCaloricIntake(totalCalories);
            currentReport.setJournalLog(view.journalArea.getText());
            reportDAO.upsertReport(currentReport);
            if (menuController != null) menuController.switchMenu(MenuState.DEFAULT);
        });

        // Cancel and Navigate Home
        view.cancelBtn.setOnAction(e -> {
            if (menuController != null) menuController.switchMenu(MenuState.DEFAULT);
        });
    }
}