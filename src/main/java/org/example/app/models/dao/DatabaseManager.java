package org.example.app.models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:./data/users";
    private static final String USER = "sa";
    private static final String PASS = "sa";

    // Método para obter conexão
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Método de inicialização do banco
    public static void init() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(255) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                avatar VARCHAR(255),
                simulationCount INT DEFAULT 0,
                succesfulSimulations INT DEFAULT 0
            );
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Banco de dados iniciado.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
