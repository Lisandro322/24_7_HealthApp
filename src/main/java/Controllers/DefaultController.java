package Controllers;

import Models.*;
import DAO.*;
import Views.DefaultView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import java.util.List;

public class DefaultController {
    private final DefaultView view;
    private final User activeUser;

    private final MedicationScheduleDAO scheduleDAO = new MedicationScheduleDAO();
    private final MedicationInfoDAO medInfoDAO = new MedicationInfoDAO();

    private MedicationSchedule activeAlertSchedule;

    public DefaultController(DefaultView view, User activeUser) {
        this.view = view;
        this.activeUser = activeUser;

        setupInitialState();
        setupTableColumns(); // Added to handle name mapping
        setupActions();
    }

    private void setupInitialState() {
        view.welcomeLabel.setText("Welcome, " + activeUser.getFirstName() + "!");
        refreshScheduleTable();
    }

    /**
     * Maps the Medication ID column to show the Medication Name instead.
     */
    private void setupTableColumns() {
        // We target the second column (index 1), which is the Medication column
        javafx.scene.control.TableColumn<MedicationSchedule, String> medNameCol =
                (javafx.scene.control.TableColumn<MedicationSchedule, String>) view.todayScheduleTable.getColumns().get(1);

        medNameCol.setCellValueFactory(cellData -> {
            int medId = cellData.getValue().getMedicationId();
            MedicationInfo info = medInfoDAO.getMedById(medId);

            // Return the name if found, otherwise return the ID as a string
            String displayName = (info != null) ? info.getMedName() : "ID: " + medId;
            return new SimpleStringProperty(displayName);
        });
    }

    public void refreshScheduleTable() {
        List<MedicationSchedule> todaySchedules = scheduleDAO.getSchedulesByUserId(activeUser.getId());
        view.todayScheduleTable.setItems(FXCollections.observableArrayList(todaySchedules));
    }

    private void setupActions() {
        view.takenBtn.setOnAction(e -> {
            if (activeAlertSchedule != null) {
                scheduleDAO.updateScheduleStatus(activeAlertSchedule.getId(), "Taken");
                hideAlert();
                refreshScheduleTable();
            }
        });

        view.silenceBtn.setOnAction(e -> hideAlert());

        view.triggerTestAlertBtn.setOnAction(e -> {
            List<MedicationSchedule> items = view.todayScheduleTable.getItems();
            if (!items.isEmpty()) {
                triggerMedicationAlert(items.get(0));
            }
        });
    }

    public void triggerMedicationAlert(MedicationSchedule schedule) {
        this.activeAlertSchedule = schedule;

        MedicationInfo info = medInfoDAO.getMedById(schedule.getMedicationId());
        String displayName = (info != null) ? info.getMedName() : "Unknown Medication";

        view.alertMedName.setText(displayName);
        view.alertBox.setVisible(true);
        view.alertBox.setManaged(true);
    }

    private void hideAlert() {
        view.alertBox.setVisible(false);
        view.alertBox.setManaged(false);
        activeAlertSchedule = null;
    }
}