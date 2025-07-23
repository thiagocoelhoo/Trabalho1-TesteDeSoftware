package org.example.app.models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//TODO testes de conexão

public class DbManager {
    private static final String DB_URL = "jdbc:h2:./data/users";
    private static final String USER = "sa";
    private static final String PASS = "sa";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void databaseStart(){
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

        try (Connection conn = getConnection();
        Statement statement = conn.createStatement()){
            statement.execute(sql);
            System.out.println("Database started/created");
        } catch(SQLException e){
            System.out.println("Database could not be created" + e.getMessage());
            e.printStackTrace();
        }
    }
}
