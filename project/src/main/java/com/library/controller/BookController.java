package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.model.User;
import com.library.util.SceneManager;
import com.library.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class BookController {

    @FXML
    public Button addButton;
    public Button updateButton;
    public Button deleteButton;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Integer> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book,String> authorColumn;

    @FXML
    private TableColumn<Book,String> isbnColumn;

    @FXML
    private TableColumn<Book,Integer> yearColumn;

    @FXML
    private TableColumn<Book,Integer> quantityColumn;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField searchField;

    private final BookDAO dao = new BookDAO();

    private Book selectedBook;

    private ObservableList<Book> masterData =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        User currentUser =
                SessionManager.getCurrentUser();

        if(currentUser != null &&
                currentUser.getRole().equals("USER"))
        {
            addButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        }

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        authorColumn.setCellValueFactory(
                new PropertyValueFactory<>("author"));

        isbnColumn.setCellValueFactory(
                new PropertyValueFactory<>("isbn"));

        yearColumn.setCellValueFactory(
                new PropertyValueFactory<>("year"));

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        //selector event
        bookTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {

                    selectedBook = newVal;

                    if(newVal != null) {

                        titleField.setText(newVal.getTitle());
                        authorField.setText(newVal.getAuthor());
                        isbnField.setText(newVal.getIsbn());
                        yearField.setText(String.valueOf(newVal.getYear()));
                        quantityField.setText(String.valueOf(newVal.getQuantity()));
                    }
                });

        refreshTable();

        FilteredList<Book> filteredData =
                new FilteredList<>(masterData, b -> true);
        //search events
        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("SEARCH: " + newValue);

                    filteredData.setPredicate(book -> {

                        if(newValue == null || newValue.isBlank())
                            return true;

                        String search =
                                newValue.toLowerCase();

                        return book.getTitle()
                                .toLowerCase()
                                .contains(search)

                                ||

                                book.getAuthor()
                                        .toLowerCase()
                                        .contains(search)

                                ||

                                book.getIsbn()
                                        .toLowerCase()
                                        .contains(search);
                    });
                });

        bookTable.setItems(filteredData);
    }

    private void refreshTable() {

        masterData.setAll(dao.getAllBooks());

        bookTable.setItems(masterData);
    }
    private void showAlert(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setContentText(message);

        alert.showAndWait();
    }

    @FXML
    private void handleAddBook() {

        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();

        if (title == null || title.isBlank() || author == null || author.isBlank() || isbn == null || isbn.isBlank()) {
            showAlert("Empty input");
            return;
        }

        Integer year = Integer.parseInt(yearField.getText());
        Integer quantity = Integer.parseInt(quantityField.getText());

        if (year>2026){
            showAlert("Invalid year");
            return;
        }
        if (quantity < 0){
            showAlert("Invalid quantity");
            return;
        }


        Book book = new Book(
                0,
                title,
                author,
                isbn,
                year,
                quantity
        );

        dao.addBook(book);

        clearFields();

        refreshTable();
    }

    @FXML
    private void handleUpdateBook() {

        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();

        if (title == null || title.isBlank() || author == null || author.isBlank() || isbn == null || isbn.isBlank()) {
            showAlert("Empty input");
            return;
        }

        Integer year = Integer.parseInt(yearField.getText());
        Integer quantity = Integer.parseInt(quantityField.getText());

        if (year>2026){
            showAlert("Invalid year");
            return;
        }
        if (quantity < 0){
            showAlert("Invalid quantity");
            return;
        }


        Book updatedBook = new Book(
                0,
                title,
                author,
                isbn,
                year,
                quantity
        );

        dao.updateBook(updatedBook);

        refreshTable();
    }

    @FXML
    private void handleDeleteBook() {

        if(selectedBook == null)
            return;

        dao.deleteBook(selectedBook.getId());

        refreshTable();

        clearFields();

        selectedBook = null;
    }

    private void clearFields() {

        titleField.clear();
        authorField.clear();
        isbnField.clear();
        yearField.clear();
        quantityField.clear();
    }

    public void returnPage(ActionEvent event)
            throws Exception {

        SceneManager.switchScene(
                event,
                "/library/dashboard.fxml");
    }
}