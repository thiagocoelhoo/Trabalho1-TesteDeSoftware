package org.example;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.example.app.controllers.LoginController;
import org.example.app.models.dao.UserDAO;
import org.example.app.services.UserService;
import org.example.app.view.LoginFrame;
import org.example.page.LoginPage;
import org.example.page.MainPage;
import org.example.page.NewUserPage;
import org.example.page.SimulationPage;
import org.junit.Test;

public class UserJourneyTest extends AssertJSwingJUnitTestCase {
    private FrameFixture loginWindow;
    private FrameFixture newUserWindow;
    private FrameFixture mainWindow;
    private FrameFixture simulationWindow;
    private UserService userService;

    @Override
    protected void onSetUp() {
        userService = new UserService(new UserDAO());
        // userService.clearAllUsers();

        GuiActionRunner.execute(() -> {
            LoginController loginController = new LoginController(userService, 600, 400);
            LoginFrame loginFrame = new LoginFrame(600, 400, loginController);
            loginFrame.setVisible(true);
            return loginFrame;
        });

        loginWindow = new FrameFixture(robot(), "Login");
        loginWindow.show();
    }

    @Test
    public void testCompleteUserJourney() {
        // 1. Criar usuário
        LoginPage loginPage = new LoginPage(loginWindow);
        loginPage.clickNewUser();

        // Aguardar e capturar a nova janela
        newUserWindow = new FrameFixture(robot(), "newUserFrame");
        newUserWindow.requireVisible();

        NewUserPage newUserPage = new NewUserPage(newUserWindow);
        newUserPage.createUser("testuser", "password", "guardian 1");

        // 2. Logar no sistema
        loginPage.login("testuser", "password");

        // Aguardar e capturar a main window
        mainWindow = new FrameFixture(robot(), "MainFrame");
        mainWindow.requireVisible();

        // 3. Iniciar simulação
        MainPage mainPage = new MainPage(mainWindow);
        mainPage.clickStartSimulation();

        simulationWindow = new FrameFixture(robot(), "QuantitySimulation");
        simulationWindow.requireVisible();

        SimulationPage simulationPage = new SimulationPage(simulationWindow);
        simulationPage.setCreatureQuantity(3);

        // 4. Esperar simulação finalizar
        robot().waitForIdle();
        try {
            robot().wait(180000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 5. Verificar pontuação
        mainPage.verifyUserScore("testuser", 1);
    }

    @Override
    protected void onTearDown() {
        // Fechar todas as janelas
        if (loginWindow != null) loginWindow.cleanUp();
        if (newUserWindow != null) newUserWindow.cleanUp();
        if (mainWindow != null) mainWindow.cleanUp();
        if (simulationWindow != null) simulationWindow.cleanUp();
    }
}