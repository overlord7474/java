package com.library.dao;

import com.library.model.Book;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAllBooks() {

        List<Book> list = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("year"),
                        rs.getInt("quantity")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addBook(Book book) {

        String sql = "INSERT INTO books\n" +
                "(title,author,isbn,year,quantity)\n" +
                "VALUES(?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getYear());
            ps.setInt(5, book.getQuantity());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {

        String sql = """
        UPDATE books
        SET title=?,
            author=?,
            isbn=?,
            year=?,
            quantity=?
        WHERE id=?
    """;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getYear());
            ps.setInt(5, book.getQuantity());
            ps.setInt(6, book.getId());

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Book findByTitle(String title) {

        String sql = "SELECT * FROM books WHERE title = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("year"),
                        rs.getInt("quantity")
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void decreaseQuantity(int bookId) {

        String sql =
                "UPDATE books " +
                        "SET quantity = quantity - 1 " +
                        "WHERE id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalBooks() {

        String sql =
                "SELECT COALESCE(SUM(quantity),0) FROM books";

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
