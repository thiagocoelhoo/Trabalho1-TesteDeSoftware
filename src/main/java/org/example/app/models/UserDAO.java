package org.example.app.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:./userDB", "sa", "");
            Statement stmt = conn.createStatement();
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) PRIMARY KEY,
                    password VARCHAR(255),
                    partidas_totais INT,
                    partidas_ganhas INT,
                    avatar VARCHAR(255)
                )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(User user) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getPartidas_totais());
            stmt.setInt(4, user.getPartidas_ganhas());
            stmt.setString(5, user.getAvatar());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findByUsername(String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("avatar")
                );
                user.setPartidas_totais(rs.getInt("partidas_totais"));
                user.setPartidas_ganhas(rs.getInt("partidas_ganhas"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                User u = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("avatar")
                );
                u.setPartidas_totais(rs.getInt("partidas_totais"));
                u.setPartidas_ganhas(rs.getInt("partidas_ganhas"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("avatar")
                );
                user.setPartidas_totais(rs.getInt("partidas_totais"));
                user.setPartidas_ganhas(rs.getInt("partidas_ganhas"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void update(User user) {
        String sql = "UPDATE users SET password = ?, partidas_totais = ?, partidas_ganhas = ?, avatar = ? WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword());
            stmt.setInt(2, user.getPartidas_totais());
            stmt.setInt(3, user.getPartidas_ganhas());
            stmt.setString(4, user.getAvatar());
            stmt.setString(5, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
