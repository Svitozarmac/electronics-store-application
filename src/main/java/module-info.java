module com.example.stage2_svitozar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


//    opens com.example.stage2_svitozar to javafx.fxml;
    opens com.example.stage2_svitozar.app to javafx.fxml;
    opens com.example.stage2_svitozar.controller to javafx.fxml;
    opens com.example.stage2_svitozar.view to javafx.fxml;

    exports com.example.stage2_svitozar.app;
    exports com.example.stage2_svitozar.controller;
    exports com.example.stage2_svitozar.model;
    exports com.example.stage2_svitozar.view;

}