package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.IssueDAO;
import com.library.util.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.event.ActionEvent;

public class StatisticsController {

    @FXML
    private PieChart pieChart;

    private final BookDAO bookDAO =
            new BookDAO();

    private final IssueDAO issueDAO =
            new IssueDAO();

    @FXML
    public void initialize() {

        int books =
                bookDAO.getTotalBooks();

        int issued =
                issueDAO.getIssuedCount();

        pieChart.setData(
                FXCollections.observableArrayList(

                        new PieChart.Data(
                                "Available Books",
                                books
                        ),

                        new PieChart.Data(
                                "Issued Books",
                                issued
                        )
                )
        );
    }

    @FXML
    private void goBack(ActionEvent event)
            throws Exception {

        SceneManager.switchScene(
                event,
                "/library/dashboard.fxml"
        );
    }
}
