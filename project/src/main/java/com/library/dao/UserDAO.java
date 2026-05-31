package com.library.dao;

import com.library.model.User;
import com.library.util.DBConnection;

import java.sql.*;

public class UserDAO {

    public User login(
            String username,
            String password) {

        String sql =
                """
                SELECT *
                FROM users
                WHERE username=?
                AND password=?
                """;

        try(Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                );
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}