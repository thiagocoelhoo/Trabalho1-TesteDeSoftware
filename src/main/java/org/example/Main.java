package org.example;

import org.example.app.models.dao.DatabaseManager;
import org.example.app.models.dao.UserDAO;
import org.example.app.services.UserService;
import org.example.app.view.LoginFrame;
import java.awt.*;

/**
 * Classe responsável pela criação da janela e inicialização da parte gráfica da aplicação
 * (NÃO POSSUI A LÓGICA DA APLICAÇÃO)
 */

public class Main {
    public static void main(String[] args) {
        // Get current device screen dimensions
        final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        // DatabaseManager db = new DatabaseManager();
        // db.init();

        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

        LoginFrame login = new LoginFrame(600, 400, userService);
        login.setVisible(true);
    }
}