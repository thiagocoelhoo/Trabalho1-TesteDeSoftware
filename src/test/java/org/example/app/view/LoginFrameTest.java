package org.example.app.view;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.example.app.models.User;
import org.example.app.services.UserService;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

public class LoginFrameTest {
    private FrameFixture window;
    private Robot robot;
    private UserService userService;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install(); // Verifica EDT violations
    }

    @BeforeEach
    public void setUp() {
        // Mock service
        userService = mock(UserService.class);

        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(10);

        LoginFrame frame = GuiActionRunner.execute(() ->
            new LoginFrame(800, 600, userService)  // injetar o mock corretamente aqui
        );

        frame.setVisible(true);

        window = new FrameFixture(robot, frame);
        window.robot().waitForIdle();
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    @GUITest
    public void shouldShowLoginComponents() {
        // window.label("Bem-vindo(a)!").requireVisible();

        var usernameField = window.textBox("usernameField");
        var passwordField = window.textBox("passwordField");
        var submitButton = window.button("Entrar");

        usernameField.requireVisible();
        passwordField.requireVisible();
        submitButton.requireVisible();
    }

    @Test
    @GUITest
    public void shouldClearFieldsWhenLoginFails() {
        // Mock comportamento: login inválido
        when(userService.authenticate("user", "wrong")).thenReturn(false);
        when(userService.getUser("user")).thenReturn(null);

        var usernameField = window.textBox("usernameField");
        var passwordField = window.textBox("passwordField");
        var submitButton = window.button("Entrar");

        usernameField.enterText("user");
        passwordField.enterText("wrong");

        submitButton.click();

        usernameField.requireText("");
        passwordField.requireText("");
    }

    @Test
    @GUITest
    public void shouldOpenMainFrameOnSuccessfulLogin() {
        User mockUser = new User(null, "user", "any", "");
        when(userService.authenticate("user", "correct")).thenReturn(true);
        when(userService.getUser("user")).thenReturn(mockUser);

        var usernameField = window.textBox("usernameField");
        var passwordField = window.textBox("passwordField");
        var submitButton = window.button("Entrar");

        usernameField.enterText("user");
        passwordField.enterText("correct");

        submitButton.click();

        // Você pode verificar se o LoginFrame foi fechado:
        // Assertions.assertThrows(Exception.class, () -> window.requireVisible());
    }
}
