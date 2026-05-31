package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.library.dao.DatabaseInitializer;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.init(); //thats my db and it has to be before fxml

        FXMLLoader fxmlLoader =
                new FXMLLoader(Main.class.getResource("/library/login.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
