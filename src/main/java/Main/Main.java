package Main;

import Controllers.MenuController;
import Views.MainLayoutView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // 1. Run Database Initiation
        DatabaseHelper.initializeDatabase();

        // 2. Setup UI
        MainLayoutView mainLayout = new MainLayoutView();
        new MenuController(mainLayout);

        Scene scene = new Scene(mainLayout.getRoot(), 400, 600);
        stage.setTitle("Health App");
        stage.setScene(scene);
        stage.show();
    }
}