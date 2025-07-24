package org.example.page;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JSpinnerFixture;

public class SimulationPage {
    private final FrameFixture window;

    public SimulationPage(FrameFixture window) {
        this.window = window;
    }

    public void setCreatureQuantity(int quantity) {
        JSpinnerFixture spinner = window.spinner("quantidadeSpinner");
        spinner.increment(quantity - 1); // Assume que o valor padrão é 1
        window.button("startSimButton").click();
    }

    public void clickStart() {
        window.button().click(); // único botão no frame
        window.robot().waitForIdle();
    }
}
