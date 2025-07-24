package org.example;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.*;

import javax.swing.*;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {

    private Robot robot;

    @BeforeEach
    void setUp() {
        robot = BasicRobot.robotWithNewAwtHierarchy();
    }

    @AfterEach
    void tearDown() {
        robot.cleanUp();
    }

    @Test
    void shouldStartLoginFrame() {
        // Executa o main em outra thread
        SwingUtilities.invokeLater(() -> Main.main(new String[]{}));

        // Espera a janela abrir
        FrameFixture window = new FrameFixture(robot, findLoginFrame());
        window.requireVisible();
        window.requireSize(new java.awt.Dimension(600, 400));
        window.cleanUp();
    }

    private JFrame findLoginFrame() {
        for (Frame f : Frame.getFrames()) {
            if (f instanceof JFrame frame && frame.isVisible() && frame.getTitle().isEmpty()) {
                return frame;
            }
        }
        throw new AssertionError("LoginFrame não foi exibido.");
    }
}
