package Views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UserMedView {
    public VBox getView() {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("User and Medication Content Goes Here"));
        return layout;
    }
}
