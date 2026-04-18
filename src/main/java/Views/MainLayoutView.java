package Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class MainLayoutView {
    private BorderPane root;
    private StackPane centerContent;

    // Buttons exposed so the Controller can add listeners
    public Button reportBtn = new Button("Report");
    public Button userInfoBtn = new Button("User Info");
    public Button modifyBtn = new Button("+");

    public MainLayoutView() {
        root = new BorderPane();
        centerContent = new StackPane();

        setupLayout();
    }

    private void setupLayout() {
        // Top Bar
        Label dateLabel = new Label("Date: " + LocalDate.now());
        HBox topBar = new HBox(dateLabel);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");

        // Bottom Bar (Nav)
        HBox bottomBar = new HBox(20, reportBtn, modifyBtn, userInfoBtn);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setStyle("-fx-border-color: lightgray; -fx-border-width: 1 0 0 0;");

        root.setTop(topBar);
        root.setCenter(centerContent);
        root.setBottom(bottomBar);
    }

    // This method allows the Controller to swap the middle view
    public void setCenterView(Node view) {
        centerContent.getChildren().setAll(view);
    }

    public Parent getRoot() {
        return root;
    }
}