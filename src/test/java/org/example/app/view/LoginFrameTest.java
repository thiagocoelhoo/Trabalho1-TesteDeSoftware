package org.example.app.view;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.example.app.controllers.LoginController;
import org.example.app.models.User;
import org.example.app.services.UserService;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.assertj.core.api.Assertions.assertThat;
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
            new LoginFrame(800, 600, new LoginController(userService, 800, 600))  // injetar o mock corretamente aqui
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
        var submitButton = window.button("loginButton");

        usernameField.requireVisible();
        passwordField.requireVisible();
        submitButton.requireVisible();
    }


    @Test
    @GUITest
    public void shouldOpenMainFrameOnSuccessfulLogin() {
        User mockUser = new User(null, "user", "any", "");
        when(userService.authenticate("user", "correct")).thenReturn(true);
        when(userService.getUser("user")).thenReturn(mockUser);

        var usernameField = window.textBox("usernameField");
        var passwordField = window.textBox("passwordField");
        var submitButton = window.button("loginButton");

        usernameField.enterText("user");
        passwordField.enterText("correct");

        submitButton.click();

        // Você pode verificar se o LoginFrame foi fechado:
        // Assertions.assertThrows(Exception.class, () -> window.requireVisible());
    }

    @Test
    @GUITest
    public void shouldTriggerLoginOnEnterKeyPressed() {
        User mockUser = new User(null, "user", "pass", "");
        when(userService.authenticate("user", "pass")).thenReturn(true);
        when(userService.getUser("user")).thenReturn(mockUser);

        var usernameField = window.textBox("usernameField");
        var passwordField = window.textBox("passwordField");

        usernameField.enterText("user");
        passwordField.enterText("pass");
        passwordField.pressAndReleaseKeys(KeyEvent.VK_ENTER);

        // Após VK_ENTER, deve simular clique no botão de login
        verify(userService).authenticate("user", "pass");
    }

//    @Test
//    @GUITest
//    public void shouldCallHandleNewUserOnLabelClick() {
//        LoginController controllerSpy = spy(new LoginController(userService, 800, 600));
//        LoginFrame frame = GuiActionRunner.execute(() -> new LoginFrame(800, 600, controllerSpy));
//        window = new FrameFixture(robot, frame);
//        window.robot().waitForIdle();
//
//        window.label("loginLabel").click();
//
//        verify(controllerSpy, times(1)).handleNewUser();
//    }

//    @Test
//    @GUITest
//    public void shouldChangeCursorOnLabelHover() {
//        var label = window.label("loginLabel");
//        label.target().dispatchEvent(new MouseEvent(
//                label.target(), MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 10, 10, 1, false
//        ));
//
//        assertThat(label.target().getCursor().getType()).isEqualTo(Cursor.HAND_CURSOR);
//    }
}