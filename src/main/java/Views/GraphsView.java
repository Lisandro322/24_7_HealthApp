package Views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GraphsView {
    public VBox getView() {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Graphing and History search Content Goes Here"));
        return layout;
    }
}
