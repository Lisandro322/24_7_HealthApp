package Views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class MainLayoutView {
    private BorderPane root;
    private StackPane centerContent;
    private ScrollPane scrollPane;

    // Bottom Nav Buttons
    public Button userInfoBtn = new Button("User Info");
    public Button reportInfoBtn = new Button("Report Info");
    public Button editReportBtn = new Button("Edit Report");
    public Button returnBtn = new Button("Return");

    // Top Bar Buttons
    public Button switchUserBtn = new Button("Switch User");
    public Button exitBtn = new Button("Exit");

    public MainLayoutView() {
        root = new BorderPane();
        centerContent = new StackPane();
        scrollPane = new ScrollPane();

        setupLayout();
    }

    private void setupLayout() {
        String softYellow = "#FFF9C4";
        String borderGray = "#E0E0E0";

        // --- TOP BAR SETUP ---
        Label dateLabel = new Label("Date: " + LocalDate.now());
        dateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #5D4037;");

        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topButtons = new HBox(10, switchUserBtn, exitBtn);
        topButtons.setAlignment(Pos.CENTER_RIGHT);

        HBox topBar = new HBox(dateLabel, spacer, topButtons);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: " + softYellow + "; " +
                "-fx-border-color: " + borderGray + "; " +
                "-fx-border-width: 0 0 1 0;");

        // --- BOTTOM BAR SETUP ---
        HBox bottomBar = new HBox(15, userInfoBtn, reportInfoBtn, editReportBtn, returnBtn);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(15));
        bottomBar.setStyle("-fx-background-color: " + softYellow + "; " +
                "-fx-border-color: " + borderGray + "; " +
                "-fx-border-width: 1 0 0 0;");

        // --- CENTER AREA ---
        centerContent.setStyle("-fx-background-color: " + softYellow + ";");
        centerContent.setPadding(new Insets(10));

        scrollPane.setContent(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + softYellow + "; -fx-border-color: transparent;");

        root.setTop(topBar);
        root.setCenter(scrollPane);
        root.setBottom(bottomBar);

        // Apply styling to all buttons
        applyButtonStyle(userInfoBtn);
        applyButtonStyle(reportInfoBtn);
        applyButtonStyle(editReportBtn);
        applyButtonStyle(returnBtn);
        applyButtonStyle(switchUserBtn);
        applyButtonStyle(exitBtn);

        // Quick action for Exit
        exitBtn.setOnAction(e -> Platform.exit());
    }

    private void applyButtonStyle(Button btn) {
        btn.setStyle("-fx-background-color: #FFFFFF; " +
                "-fx-border-color: #D4E157; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;");
    }

    public void setCenterView(Node view) {
        centerContent.getChildren().setAll(view);
    }

    public Parent getRoot() {
        return root;
    }
}