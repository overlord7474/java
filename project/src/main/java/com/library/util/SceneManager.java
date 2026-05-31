package com.library.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    public static void switchScene(
            ActionEvent event,
            String fxml) throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        SceneManager.class.getResource(fxml));

        Scene scene = new Scene(loader.load());

        Stage stage =
                (Stage)((Node)event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(scene);
    }
}
