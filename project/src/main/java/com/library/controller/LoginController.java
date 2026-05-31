package com.library.controller;

import com.library.dao.UserDAO;
import com.library.model.User;
import com.library.util.SceneManager;
import com.library.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final UserDAO userDAO =
            new UserDAO();

    @FXML
    private void handleLogin(ActionEvent event)
            throws Exception {

        User user =
                userDAO.login(
                        usernameField.getText(),
                        passwordField.getText()
                );

        if(user == null) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setContentText(
                    "Invalid credentials"
            );

            alert.showAndWait();

            return;
        }

        SessionManager.login(user);

        SceneManager.switchScene(
                event,
                "/library/dashboard.fxml"
        );
    }
}