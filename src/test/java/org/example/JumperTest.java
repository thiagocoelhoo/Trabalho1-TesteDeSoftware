package org.example;

import org.example.app.models.Jumper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class JumperTest {
    /*
    2) Dinheiro = um milhão (testar qt por bixo)
    3) testar posição Xi de cada criatura
    5) Muita coisa:
        5.1)  nova posição xI
        5.2)  valor r (random)
        5.3)  equação completa
        5.4)  roubo de moedas e adição à quantidade do atual
     */

    private final int INITIAL_COIN_AMOUNT = 1_000_000;
    private Jumper jumper;

    @BeforeEach
    void setUp() {
        jumper = new Jumper(0.0);
    }

    @Test
    void shouldInitializeWithCorrectValues() {
        assertThat(jumper.getX()).isZero();
        assertThat(jumper.getY()).isZero();
        assertThat(jumper.getCoins()).isEqualTo(Jumper.INITIAL_COIN_AMOUNT);
        assertThat(jumper.isMoving()).isFalse();
    }

    @Test
    void calcJumpShouldReturnCorrectValue() {
        double result = jumper.calcJump(10.0, 0.5, 100);
        assertThat(result).isEqualTo(10.0 + 0.5 * 100);
    }

    @Test
    void updateShouldMoveJumperCorrectlyToRight() {
        double initialX = jumper.getX();
        double targetX = initialX + 500;

        jumper.jumpTo(targetX);

        // Simula vários updates até cair
        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        // Verifica se a posição final está próxima do target definido (com uma distância aceitável de 0.1)
        assertThat(jumper.getX()).isCloseTo(targetX, within(0.1));

        // Verifica se o jumper está no chão ou muito próximo a ele
        assertThat(jumper.getY()).isCloseTo(0, within(0.1));
    }

    @Test
    void updateShouldMoveJumperCorrectlyToLeft() {
        double initialX = jumper.getX();
        double targetX = initialX - 500;

        jumper.jumpTo(targetX);

        // Simula vários updates até cair
        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        // Verifica se a posição final está próxima do target definido (com uma distância aceitável de 0.1)
        assertThat(jumper.getX()).isCloseTo(targetX, within(0.1));

        // Verifica se o jumper está no chão ou muito próximo a ele
        assertThat(jumper.getY()).isCloseTo(0, within(0.1));
    }

    @Test
    void jumperShouldLandOnGround() {
        jumper.jump();

        // Simula vários updates até cair
        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        assertThat(jumper.getY()).isEqualTo(0);
        assertThat(jumper.isMoving()).isFalse(); // Deve ter parado de subir/descender no eixo Y
    }

    @Test
    public void testCoinsInitialAmount() {
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }

    @Test
    public void stopJumpingShouldStopHorizontalMovement() {
        double initialX = jumper.getX();
        double targetX = initialX + 500;
        jumper.jumpTo(targetX);
        jumper.stopJumping();

        // Simula vários updates até cair
        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        // Verifica se a posição final está igual a posição inicial
        assertThat(jumper.getX()).isCloseTo(initialX, within(0.1));
        assertThat(jumper.getY()).isZero();
    }

    @Test
    public void testSteal() {
        Jumper other = new Jumper(0);

        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT + INITIAL_COIN_AMOUNT / 2);
        assertThat(other.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT / 2);
    }

    @Test
    public void testStealPoorJumper() {
        Jumper other = new Jumper(0.0);

        other.setCoins(0);
        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
        assertThat(other.getCoins()).isEqualTo(0);
    }

    @Test
    public void testStealEvenCoins() {
        Jumper other = new Jumper(0.0);

        jumper.setCoins(5);
        other.setCoins(6);

        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(8);
        assertThat(other.getCoins()).isEqualTo(3);
    }

    @Test
    public void testStealOddCoins() {
        Jumper other = new Jumper(0.0);

        jumper.setCoins(5);
        other.setCoins(3);

        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(7);
        assertThat(other.getCoins()).isEqualTo(1);
    }

    @Test
    public void testSelfSteal() {
        jumper.steal(jumper);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }

    @Test
    public void testStealNull() {
        jumper.steal(null);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }
}
