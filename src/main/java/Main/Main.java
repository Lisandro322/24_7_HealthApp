package Main;

import Controllers.MenuController;
import Controllers.UserSelectionController;
import DAO.UserDAO;
import DB.DatabaseHelper;
import Models.User;
import Views.MainLayoutView;
import Views.UserSelectView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {
    private Stage primaryStage;
    private UserDAO userDAO = new UserDAO();

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        DatabaseHelper.initializeDatabase();

        //Window Icons
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/24_7_favicon.png")));
        stage.getIcons().add(appIcon);

        showUserSelection();
    }

    public void showUserSelection() {
        UserSelectView selectView = new UserSelectView();

        // The Controller now takes over the logic
        new UserSelectionController(selectView, this);

        Scene scene = new Scene(selectView.getRoot(), 800, 1000);
        primaryStage.setTitle("Select User");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // launchMainApp remains here to handle the primary stage scene swap
    public void launchMainApp(User user) {
        MainLayoutView mainLayout = new MainLayoutView();
        new MenuController(mainLayout, user, this);

        Scene scene = new Scene(mainLayout.getRoot(), 800, 1000);
        primaryStage.setTitle("Health Tracker - " + user.getFirstName());
        primaryStage.setScene(scene);
    }
}