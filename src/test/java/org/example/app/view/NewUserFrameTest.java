package org.example.app.view;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.example.app.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class NewUserFrameTest {
    private FrameFixture window;
    private Robot robot;
    private UserService userService;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install(); // EDT violation check
    }

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(10);

        NewUserFrame frame = GuiActionRunner.execute(() ->
                new NewUserFrame(800, 600, userService)
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
    public void testComponentsAreVisible() {
        window.textBox("nameField").requireVisible();
        window.textBox("passwordField").requireVisible();
        window.comboBox("avatarComboBox").requireVisible();
        window.button("createButton").requireVisible();
    }

    @Test
    public void testCreateUserSuccessfully() {
        when(userService.createUser("Alice", "password123", "guardian 1")).thenReturn(true);

        window.textBox("nameField").enterText("Alice");
        window.textBox("passwordField").enterText("password123");
        window.comboBox("avatarComboBox").selectItem("guardian 1");

        window.button("createButton").click();

        verify(userService).createUser("Alice", "password123", "guardian 1");
        // window.dialog().requireVisible().requireMessage("Usuário criado com sucesso!");
        window.dialog().label("OptionPane.label").requireText("Usuário criado com sucesso!");

        window.dialog().button(JButtonMatcher.withText("OK")).click(); // fecha o diálogo
    }

    @Test
    public void testCreateUserWithEmptyFields() {
        window.textBox("nameField").setText("");
        window.textBox("passwordField").setText("");

        window.button("createButton").click();

        // window.dialog().requireVisible().requireMessage("Preencha todos os campos e selecione uma imagem.");
        window.dialog().label("OptionPane.label").requireText("Preencha todos os campos e selecione uma imagem.");
        window.dialog().button(JButtonMatcher.withText("OK")).click();
        verify(userService, never()).createUser(anyString(), anyString(), anyString());
    }

    @Test
    public void testDuplicateUserError() {
        when(userService.createUser("Bob", "secret", "creature 2")).thenReturn(false);

        window.textBox("nameField").enterText("Bob");
        window.textBox("passwordField").enterText("secret");
        window.comboBox("avatarComboBox").selectItem("creature 2");
        window.button("createButton").click();

        window.dialog().label("OptionPane.label").requireText("Erro de inclusão: o nome de usuário 'Bob' já existe.");
        window.dialog().button(JButtonMatcher.withText("OK")).click();
    }

    @Test
    public void testCreateUserWithEnterKey() {
        when(userService.createUser("Charlie", "pw", "cluster")).thenReturn(true);

        window.textBox("nameField").enterText("Charlie");
        window.textBox("passwordField").enterText("pw");
        window.comboBox("avatarComboBox").selectItem("cluster");

        window.button("createButton").pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);

        verify(userService).createUser("Charlie", "pw", "cluster");
        // window.dialog().requireVisible().requireMessage("Usuário criado com sucesso!");
        window.dialog().label("OptionPane.label").requireText("Usuário criado com sucesso!");
        window.dialog().button(JButtonMatcher.withText("OK")).click();
    }
}
