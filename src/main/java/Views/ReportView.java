package Views;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ReportView {
    public VBox getView() {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Daily Reports Content Goes Here"));
        return layout;
    }
}