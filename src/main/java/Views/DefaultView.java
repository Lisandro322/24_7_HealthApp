package Views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DefaultView {
    public VBox getView() {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Main Menu Content Goes Here"));
        return layout;
    }
}
