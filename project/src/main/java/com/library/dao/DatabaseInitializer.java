package com.library.dao;

import com.library.util.DBConnection;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void init() {

        String sql = """
            CREATE TABLE IF NOT EXISTS books(
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  title TEXT NOT NULL,
                  author TEXT NOT NULL,
                  isbn TEXT UNIQUE NOT NULL,
                  year INTEGER NOT NULL,
                  quantity INTEGER NOT NULL
              );
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS issued_books(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    book_id INTEGER,
                    book_title TEXT,
                    borrower_name TEXT,
                    issue_date TEXT,
                    FOREIGN KEY(book_id) REFERENCES books(id)
                );
            """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                );
            """);
            stmt.execute("""
                INSERT OR IGNORE INTO users (id, username, password, role)
                VALUES (1, 'admin', 'admin', 'ADMIN')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO users (id, username, password, role)
                VALUES (2, 'user', 'user', 'USER')
            """);
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
