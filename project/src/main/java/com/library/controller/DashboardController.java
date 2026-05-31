package com.library.controller;

import com.library.util.SceneManager;
import com.library.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class DashboardController {

    @FXML
    private Button dummy;

    @FXML
    private void openBooks(ActionEvent event)
            throws Exception {

        SceneManager.switchScene(
                event,
                "/library/books.fxml");
    }

    @FXML
    private void openIssues(ActionEvent event)
            throws Exception {

        SceneManager.switchScene(
                event,
                "/library/issue.fxml");
    }

    @FXML
    private void openStatistics(ActionEvent event)
            throws Exception {

        SceneManager.switchScene(
                event,
                "/library/statistics.fxml"
        );
    }

    @FXML
    private void logout(ActionEvent event)
            throws Exception {

        SessionManager.logout();

        SceneManager.switchScene(
                event,
                "/library/login.fxml"
        );
    }
}