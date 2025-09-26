package com.example.stage2_svitozar.app;

import com.example.stage2_svitozar.db_connection.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.example.stage2_svitozar.model.StoreModel;
import com.example.stage2_svitozar.controller.StoreController;
import com.example.stage2_svitozar.view.StoreView;


public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Initialize MVC components
        try {
            StoreModel model = StoreModel.getInstance();
            StoreController controller = new StoreController(null, model);
            StoreView view = new StoreView(controller, model, primaryStage);
            controller.setView(view);
            view.mainView(); // Show the view (window)
            controller.TestDBConnection(); // Test database connection
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}