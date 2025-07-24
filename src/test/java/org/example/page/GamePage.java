package org.example.page;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.timing.Pause;
import org.assertj.swing.timing.Timeout;

import java.time.Duration;

public class GamePage {
    private final FrameFixture gameWindow;

    public GamePage(FrameFixture window) {
        this.gameWindow = window;
        this.gameWindow.requireVisible();
    }

    public boolean isSimulationRunning() {
        return gameWindow.target().isVisible();
    }

    public void close() {
        if (gameWindow.target().isVisible()) {
            gameWindow.close();
        }
    }
}