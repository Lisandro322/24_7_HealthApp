module org.example._4_7_healthapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;



    exports Models;
    opens Models to javafx.fxml;
    exports Main;
    opens Main to javafx.fxml;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports Views;
    opens Views to javafx.fxml;
}