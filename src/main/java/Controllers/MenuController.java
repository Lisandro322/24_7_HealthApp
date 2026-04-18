package Controllers;

import Views.*; // Imports DefaultView, GraphsView, etc.
import javafx.scene.Node;

public class MenuController {
    private final MainLayoutView mainLayout; // The Shell

    // Partial Views
    private final DefaultView defaultView;
    private final UserMedView userMedView;
    private final ReportView reportsView;
    private final GraphsView historyView;

    public MenuController(MainLayoutView mainLayout) {
        this.mainLayout = mainLayout;

        // Initialize partial views
        this.defaultView = new DefaultView();
        this.userMedView = new UserMedView();
        this.reportsView = new ReportView();
        this.historyView = new GraphsView();

        // Attach listeners to the buttons in the MainLayout
        setupNavigation();

        // Set the initial screen
        switchMenu(MenuState.DEFAULT);
    }

    private void setupNavigation() {
        // Mapping buttons from the View to the Controller logic
        mainLayout.reportBtn.setOnAction(e -> switchMenu(MenuState.REPORTS));
        mainLayout.userInfoBtn.setOnAction(e -> switchMenu(MenuState.USER_MEDICATION));
        mainLayout.modifyBtn.setOnAction(e -> switchMenu(MenuState.GRAPHS_HISTORY));
        // Note: You can add a specific button for 'Default' or 'Home' if needed
    }

    public void switchMenu(MenuState newState) {
        Node nextView;

        // Instead of .display(), we ask the View for its UI Node
        switch (newState) {
            case USER_MEDICATION -> nextView = userMedView.getView();
            case REPORTS -> nextView = reportsView.getView();
            case GRAPHS_HISTORY -> nextView = historyView.getView();
            default -> nextView = defaultView.getView();
        }

        // Tell the Main Shell to update its center area
        mainLayout.setCenterView(nextView);
    }
}