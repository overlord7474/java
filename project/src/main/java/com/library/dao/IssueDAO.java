package com.library.dao;

import com.library.model.IssuedBook;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {

    public void issueBook(IssuedBook issue) {

        String sql = """
            INSERT INTO issued_books
            (book_id, book_title, borrower_name, issue_date)
            VALUES (?,?,?,?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, issue.getBookId());
            ps.setString(2, issue.getBookTitle());
            ps.setString(3, issue.getBorrowerName());
            ps.setString(4, issue.getIssueDate());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<IssuedBook> getAll() {

        List<IssuedBook> list = new ArrayList<>();

        String sql = "SELECT * FROM issued_books";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new IssuedBook(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("borrower_name"),
                        rs.getString("issue_date")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getIssuedCount() {

        String sql =
                "SELECT COUNT(*) FROM issued_books";

        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            if(rs.next()) {
                return rs.getInt(1);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
