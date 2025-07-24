package org.example.app.models.dao;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseIntegrationTestBase {
    protected Connection connection;
    protected UserDAO dao;

    @BeforeEach
    void openConnectionAndCleanup() throws SQLException {
        // Conecta ao banco H2 em memória, para sempre limpar ao rodar os testes
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");

        // Cria a tabela users
        Statement stmt = connection.createStatement();
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
        connection.commit();

        // Limpa a tabela antes de cada teste
        Statement cleanTable = connection.createStatement();
        cleanTable.execute("TRUNCATE TABLE users");
        connection.commit();



        // Cria uma instância do DAO que usa essa conexão
        dao = new UserDAO(connection);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        connection.close();
    }
}
