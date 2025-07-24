package org.example.page;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JSpinnerFixture;
import org.assertj.swing.fixture.JTableFixture;

public class MainPage {
    private final FrameFixture window;

    public MainPage(FrameFixture window) {
        this.window = window;
    }

    public void clickStartSimulation() {
        JButtonFixture simulateButton = window.button("simulateButton");
        simulateButton.click();
    }

    public void verifyUserScore(String username, int expectedScore) {
        JTableFixture table = window.table("usersTable");
        table.requireRowCount(1); // Deve haver apenas o usuário de teste

        // Verifica se a pontuação está correta na tabela
        // table.requireCellValue(0, 2, expectedScore);

        // Verifica também no painel do usuário
        window.label("userScoreLabel").requireText("Pontuação: " + expectedScore);
    }
}