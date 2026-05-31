package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.IssueDAO;
import com.library.model.Book;
import com.library.model.IssuedBook;

import com.library.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class IssueController {

    @FXML
    private TextField bookTitleField;

    @FXML
    private TextField borrowerField;

    @FXML
    private TableView<IssuedBook> issueTable;

    @FXML
    private TableColumn<IssuedBook,Integer> idColumn;

    @FXML
    private TableColumn<IssuedBook,String> bookColumn;

    @FXML
    private TableColumn<IssuedBook,String> borrowerColumn;

    @FXML
    private TableColumn<IssuedBook,String> dateColumn;

    private final IssueDAO issueDAO = new IssueDAO();
    private final BookDAO bookDAO = new BookDAO();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        bookColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookTitle"));

        borrowerColumn.setCellValueFactory(
                new PropertyValueFactory<>("borrowerName"));

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("issueDate"));

        refreshTable();
    }
    private void refreshTable() {

        issueTable.getItems().setAll(
                issueDAO.getAll()
        );
    }

    @FXML
    private void handleIssue() {

        String title =
                bookTitleField.getText();

        String borrower =
                borrowerField.getText();

        if(title.isBlank() || borrower.isBlank())
            return;

        Book book =
                bookDAO.findByTitle(title);

        if(book == null) {

            showAlert(
                    "Book not found."
            );

            return;
        }

        if(book.getQuantity() <= 0) {

            showAlert(
                    "No copies available."
            );

            return;
        }

        IssuedBook issue =
                new IssuedBook(
                        0,
                        book.getId(),
                        book.getTitle(),
                        borrower,
                        java.time.LocalDate.now()
                                .toString()
                );

        issueDAO.issueBook(issue);

        bookDAO.decreaseQuantity(
                book.getId()
        );

        refreshTable();

        bookTitleField.clear();
        borrowerField.clear();
    }
    private void showAlert(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setContentText(message);

        alert.showAndWait();
    }

    public void returnDashboard(ActionEvent actionEvent) throws Exception {
        SceneManager.switchScene(
                actionEvent,
                "/library/dashboard.fxml");
    }
}
