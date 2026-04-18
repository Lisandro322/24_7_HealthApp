package Controllers;

import Models.*;
import DAO.*;
import Views.UserMedView;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

public class UserMedController {
    private final UserMedView view;
    private User activeUser;

    private final UserDAO userDAO = new UserDAO();
    private final MedicationInfoDAO medDAO = new MedicationInfoDAO();
    private final MedicationScheduleDAO scheduleDAO = new MedicationScheduleDAO();

    public UserMedController(UserMedView view, User activeUser) {
        this.view = view;
        this.activeUser = activeUser;
        setupInitialState();
        setupActions();
    }

    private void setupInitialState() {
        refreshUserDisplay();
        refreshMedTable();
    }

    private void refreshUserDisplay() {
        view.userNameLabel.setText("User: " + activeUser.getFirstName() + " " + activeUser.getLastName());
        view.firstNameField.setText(activeUser.getFirstName());
        view.lastNameField.setText(activeUser.getLastName());
        view.weightField.setText(String.valueOf(activeUser.getWeight()));
        view.heightField.setText(String.valueOf(activeUser.getHeight()));
    }

    private void setupActions() {
        // --- 1. User Profile ---
        view.editUserBtn.setOnAction(e -> toggleVisibility(view.editUserContainer));
        view.saveUserChangesBtn.setOnAction(e -> handleUserUpdate());

        // --- 2. Medication Selection ---
        view.medTable.getSelectionModel().selectedItemProperty().addListener((obs, oldMed, newMed) -> {
            if (newMed != null) {
                view.detailText.setText("Dosage: " + newMed.getDosage() + "\nNotes: " + newMed.getNote());
                view.scheduleSection.setVisible(false);
                view.scheduleSection.setManaged(false);
            }
        });

        // --- 3. Medication Form Logic (ADD & EDIT) ---
        view.addMedBtn.setOnAction(e -> {
            clearMedForm();
            view.medFormTitle.setText("Add New Medication");
            view.saveNewMedBtn.setText("Confirm Add Medication");
            toggleVisibility(view.addMedForm);
        });

        view.editMedBtn.setOnAction(e -> {
            MedicationInfo selected = view.medTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Populate Form
                view.newMedName.setText(selected.getMedName());
                view.newMedDosage.setText(selected.getDosage());
                view.newMedDailyReq.getValueFactory().setValue(selected.getDailyReq());
                view.newMedNote.setText(selected.getNote());

                // Switch to Edit Mode
                view.medFormTitle.setText("Editing: " + selected.getMedName());
                view.saveNewMedBtn.setText("Update Medication");
                view.addMedForm.setVisible(true);
                view.addMedForm.setManaged(true);
            }
        });

        view.saveNewMedBtn.setOnAction(e -> handleMedPersistence());
        view.cancelMedBtn.setOnAction(e -> {
            clearMedForm();
            view.addMedForm.setVisible(false);
            view.addMedForm.setManaged(false);
        });

        view.deleteMedBtn.setOnAction(e -> {
            MedicationInfo selected = view.medTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                medDAO.deleteMedication(selected.getId());
                refreshMedTable();
                view.detailText.setText("Select a medication to see details.");
            }
        });

        // --- 4. Schedule Management ---
        view.viewScheduleBtn.setOnAction(e -> {
            MedicationInfo selected = view.medTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                refreshScheduleTable(selected);
                view.scheduleSection.setVisible(true);
                view.scheduleSection.setManaged(true);
            }
        });

        view.addScheduleBtn.setOnAction(e -> toggleVisibility(view.addScheduleForm));

        view.saveNewSchedBtn.setOnAction(e -> {
            MedicationInfo selectedMed = view.medTable.getSelectionModel().getSelectedItem();
            if (selectedMed != null) {
                String time = String.format("%02d:%02d", view.hourSpinner.getValue(), view.minuteSpinner.getValue());
                scheduleDAO.insertSchedule(new MedicationSchedule(0, activeUser.getId(), selectedMed.getId(), time, "Active"));
                refreshScheduleTable(selectedMed);
                view.addScheduleForm.setVisible(false);
                view.addScheduleForm.setManaged(false);
            }
        });

        view.toggleScheduleBtn.setOnAction(e -> {
            MedicationSchedule selectedSched = view.scheduleTable.getSelectionModel().getSelectedItem();
            if (selectedSched != null) {
                String nextStatus = selectedSched.getStatus().equals("Active") ? "Stopped" : "Active";
                scheduleDAO.updateScheduleStatus(selectedSched.getId(), nextStatus);
                refreshScheduleTable(view.medTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void handleMedPersistence() {
        MedicationInfo selected = view.medTable.getSelectionModel().getSelectedItem();
        boolean isEdit = view.saveNewMedBtn.getText().equals("Update Medication");

        if (isEdit && selected != null) {
            selected.setMedName(view.newMedName.getText());
            selected.setDosage(view.newMedDosage.getText());
            selected.setDailyReq(view.newMedDailyReq.getValue());
            selected.setNote(view.newMedNote.getText());
            medDAO.updateMedication(selected);
        } else {
            medDAO.insertMedication(new MedicationInfo(0, activeUser.getId(), view.newMedName.getText(),
                    view.newMedDailyReq.getValue(), view.newMedDosage.getText(), view.newMedNote.getText()));
        }

        refreshMedTable();
        clearMedForm();
        view.addMedForm.setVisible(false);
        view.addMedForm.setManaged(false);
    }

    private void handleUserUpdate() {
        String w = view.weightField.getText();
        String h = view.heightField.getText();
        if (!w.matches("\\d+(\\.\\d+)?") || !h.matches("\\d+(\\.\\d+)?")) {
            showError("Invalid Input", "Weight and Height must be numeric.");
            return;
        }
        activeUser.setfirstName(view.firstNameField.getText());
        activeUser.setlastName(view.lastNameField.getText());
        activeUser.setWeight(Double.parseDouble(w));
        activeUser.setHeight(Double.parseDouble(h));

        if (userDAO.updateUser(activeUser)) {
            refreshUserDisplay();
            view.editUserContainer.setVisible(false);
            view.editUserContainer.setManaged(false);
        }
    }

    private void toggleVisibility(javafx.scene.Node node) {
        node.setVisible(!node.isVisible());
        node.setManaged(node.isVisible());
    }

    private void clearMedForm() {
        view.newMedName.clear();
        view.newMedDosage.clear();
        view.newMedNote.clear();
        view.newMedDailyReq.getValueFactory().setValue(1);
    }

    private void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(message);
        a.showAndWait();
    }

    public void refreshMedTable() {
        view.medTable.setItems(FXCollections.observableArrayList(medDAO.getMedsByUserId(activeUser.getId())));
    }

    private void refreshScheduleTable(MedicationInfo med) {
        if (med != null) {
            view.scheduleTable.setItems(FXCollections.observableArrayList(scheduleDAO.getSchedulesByMedId(med.getId())));
        }
    }
}