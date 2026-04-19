package Controllers;

import Main.Main;
import Views.*;
import javafx.scene.Node;
import Models.User;

public class MenuController {
    private final MainLayoutView mainLayout;
    private final Main mainApp;
    private User activeUser;

    // Views
    private final DefaultView defaultView;
    private final UserMedView userMedView;
    private final ReportView reportsView;
    private final GraphsView historyView;

    // Sub-Controllers
    private final UserMedController userMedController;
    private final ReportController reportController;
    private final DefaultController defaultController;
    private final GraphsController graphsController;

    public MenuController(MainLayoutView mainLayout, User activeUser, Main mainApp) {
        this.mainLayout = mainLayout;
        this.mainApp = mainApp;
        this.activeUser = activeUser;

        // 1. Initialize Views
        this.defaultView = new DefaultView();
        this.userMedView = new UserMedView();
        this.reportsView = new ReportView();
        this.historyView = new GraphsView();

        // 2. Initialize Sub-Controllers
        this.userMedController = new UserMedController(userMedView, activeUser);
        this.reportController = new ReportController(reportsView, activeUser);
        this.defaultController = new DefaultController(defaultView, activeUser);
        this.graphsController = new GraphsController(historyView, activeUser);

        // --- CRITICAL LINK FOR NAVIGATION ---
        // This allows ReportController to call switchMenu(MenuState.DEFAULT)
        this.reportController.setMenuController(this);

        setupNavigation();

        // Start on the home screen
        switchMenu(MenuState.DEFAULT);
    }

    private void setupNavigation() {
        mainLayout.userInfoBtn.setOnAction(e -> switchMenu(MenuState.USER_MEDICATION));
        mainLayout.reportInfoBtn.setOnAction(e -> switchMenu(MenuState.GRAPHS_HISTORY));
        mainLayout.editReportBtn.setOnAction(e -> switchMenu(MenuState.REPORTS));
        mainLayout.returnBtn.setOnAction(e -> switchMenu(MenuState.DEFAULT));

        // Switch User: Returns to the selection screen in Main
        mainLayout.switchUserBtn.setOnAction(e -> mainApp.showUserSelection());
    }

    public void switchMenu(MenuState newState) {
        Node nextView;

        switch (newState) {
            case USER_MEDICATION -> {
                userMedController.refreshMedTable();
                nextView = userMedView.getView();
            }
            case REPORTS -> {
                reportController.initializeTodayReport();
                nextView = reportsView.getView();
            }
            case GRAPHS_HISTORY -> {
                // Refresh graph data whenever entering the history screen
                graphsController.refresh();
                nextView = historyView.getView();
            }
            default -> {
                // Refresh dashboard table when returning home
                defaultController.refreshScheduleTable();

                // Update welcome message
                defaultView.welcomeLabel.setText("Welcome, " + activeUser.getFirstName() + "!");
                nextView = defaultView.getView();
            }
        }

        // Push the selected view into the scrollable center area
        mainLayout.setCenterView(nextView);
    }
}