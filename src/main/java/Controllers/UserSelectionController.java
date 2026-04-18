package Controllers;

import DAO.UserDAO;
import Main.Main;
import Models.User;
import Views.UserSelectView;
import javafx.collections.FXCollections;

public class UserSelectionController {
    private final UserSelectView view;
    private final UserDAO userDAO;
    private final Main mainApp;

    public UserSelectionController(UserSelectView view, Main mainApp) {
        this.view = view;
        this.mainApp = mainApp;
        this.userDAO = new UserDAO();

        setupActions();
        refreshUserTable();
    }

    private void setupActions() {
        // Switch to creation form
        view.createNewUserBtn.setOnAction(e -> view.showCreation());

        // Back to list
        view.cancelCreateBtn.setOnAction(e -> view.showSelection());

        // Select User and Launch App
        view.selectUserBtn.setOnAction(e -> {
            User selected = view.userTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                mainApp.launchMainApp(selected);
            }
        });

        // Save New User Logic
        view.saveUserBtn.setOnAction(e -> handleUserCreation());
    }

    private void handleUserCreation() {
        try {
            // Basic validation
            String first = view.firstNameField.getText();
            String last = view.lastNameField.getText();
            if (first.isEmpty() || last.isEmpty()) return;

            User newUser = new User();
            newUser.setfirstName(first);
            newUser.setlastName(last);
            newUser.setAge(view.ageSpinner.getValue());
            newUser.setGender(view.genderBox.getValue());
            newUser.setWeight(Double.parseDouble(view.weightField.getText()));
            newUser.setHeight(Double.parseDouble(view.heightField.getText()));

            userDAO.insertUser(newUser);

            // Success: Clear fields, go back to table, and refresh
            refreshUserTable();
            view.showSelection();

        } catch (NumberFormatException ex) {
            System.err.println("Invalid input for weight or height");
        }
    }

    private void refreshUserTable() {
        view.userTable.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
    }
}