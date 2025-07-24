package org.example.app.models.dao;

import org.example.app.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// classe de acesso e manipulação dos dados referentes a usuários
// integração com banco de dados
public class UserDAO {
    private final Connection conn;

    public UserDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:./userDB", "sa", "");
            Statement stmt = conn.createStatement();
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(255),
                    password VARCHAR(255),
                    partidas_totais INT,
                    partidas_ganhas INT,
                    avatar VARCHAR(255)
                )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar com o banco de dados");
        }
    }

    // adicionar novo usuário ao bd
    public void insert(User user) {
        String sql = "INSERT INTO users (username, password, partidas_totais, partidas_ganhas, avatar) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getSimulationCount());
            stmt.setInt(4, user.getSuccesfulSimulations());
            stmt.setString(5, user.getAvatar());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // buscar no bd por nome de usuário
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("avatar")
                );
                user.setSimulationCount(rs.getInt("partidas_totais"));
                user.setSuccessfulSimulations(rs.getInt("partidas_ganhas"));
                return user;
            }
        }
        return null;
    }

    // listar todos os usuários da base de dados
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY partidas_ganhas DESC";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("avatar")
                );

                user.setSimulationCount(rs.getInt("partidas_totais"));
                user.setSuccessfulSimulations(rs.getInt("partidas_ganhas"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // atualizar informações de usuário na base de dados
    public void update(User user) {
        String sql = "UPDATE users SET password = ?, partidas_totais = ?, partidas_ganhas = ?, avatar = ? WHERE username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getPassword());
            stmt.setInt(2, user.getSimulationCount());
            stmt.setInt(3, user.getSuccesfulSimulations());
            stmt.setString(4, user.getAvatar());
            stmt.setString(5, user.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // remover usuário da base de dados
    public void delete(String username) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }

    // recebe a soma da quantidade de simulações de todos os usuários registrados
    public int getTotalSimulations() throws SQLException {
        String sql = "SELECT SUM(partidas_totais) AS total FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    // recebe a média de simulações concluídas de todos os usuários registrados
    public double getAverageWins() throws SQLException {
        String sql = "SELECT AVG(partidas_ganhas) AS avg FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble("avg") : 0.0;
        }
    }
}
