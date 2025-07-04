package org.example;
import org.example.app.models.User;
import org.example.app.models.UserDAO;
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
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByUsername("admin");

        if (user == null) {
            userDAO.insert(new User("admin", "admin", null));
        }

        LoginFrame login = new LoginFrame(screenWidth, screenHeight);
        login.setVisible(true);
    }
}