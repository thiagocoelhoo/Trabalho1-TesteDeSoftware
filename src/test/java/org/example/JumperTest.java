package org.example;

import org.example.Creatures.Jumper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class JumperTest {

    private final int INITIAL_COIN_AMOUNT = 1_000_000;
    private Jumper jumper;

    @BeforeEach
    void setUp() {
        jumper = new Jumper(0.0);
    }

    // ===============================================================
    //               TESTES DE DOMÍNIO
    // ===============================================================

    // Verifica a inicialização correta do Jumper
    @Test
    void shouldInitializeWithCorrectValues() {
        assertThat(jumper.getX()).isZero();
        assertThat(jumper.getY()).isZero();
        assertThat(jumper.getCoins()).isEqualTo(Jumper.INITIAL_COIN_AMOUNT);
        assertThat(jumper.isMoving()).isFalse();
    }

    // Verifica o valor retornado pela fórmula base + acceleration * time
    @Test
    void calcJumpShouldReturnCorrectValue() {
        double result = jumper.calcJump(10.0, 0.5, 100);
        assertThat(result).isEqualTo(10.0 + 0.5 * 100);
    }

    // Verifica se o Jumper se move corretamente para a direita após jumpTo
    @Test
    void updateShouldMoveJumperCorrectlyToRight() {
        double initialX = jumper.getX();
        double targetX = initialX + 500;

        jumper.jumpTo(targetX);

        // Simula vários updates até o Jumper cair no chão
        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        // Verifica a posição final e se voltou ao solo
        assertThat(jumper.getX()).isCloseTo(targetX, within(0.1));
        assertThat(jumper.getY()).isCloseTo(0, within(0.1));
    }

    // Verifica se o Jumper se move corretamente para a esquerda após jumpTo
    @Test
    void updateShouldMoveJumperCorrectlyToLeft() {
        double initialX = jumper.getX();
        double targetX = initialX - 500;

        jumper.jumpTo(targetX);

        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        assertThat(jumper.getX()).isCloseTo(targetX, within(0.1));
        assertThat(jumper.getY()).isCloseTo(0, within(0.1));
    }

    // Verifica se o Jumper retorna ao solo após o salto
    @Test
    void jumperShouldLandOnGround() {
        jumper.jump();

        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        assertThat(jumper.getY()).isEqualTo(0);
        assertThat(jumper.isMoving()).isFalse();
    }

    // Verifica a quantidade inicial de moedas
    @Test
    public void testCoinsInitialAmount() {
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }

    // Verifica se o movimento horizontal é cancelado após stopJumping
    @Test
    public void stopJumpingShouldStopHorizontalMovement() {
        double initialX = jumper.getX();
        double targetX = initialX + 500;
        jumper.jumpTo(targetX);
        jumper.stopJumping();

        for (int i = 0; i < 100; i++) {
            jumper.update(0.1);
        }

        assertThat(jumper.getX()).isCloseTo(initialX, within(0.1));
        assertThat(jumper.getY()).isZero();
    }

    // Verifica o roubo de moedas de outro Jumper
    @Test
    public void testSteal() {
        Jumper other = new Jumper(0);
        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT + INITIAL_COIN_AMOUNT / 2);
        assertThat(other.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT / 2);
    }

    // Roubo de um Jumper com número par de moedas
    @Test
    public void testStealEvenCoins() {
        Jumper other = new Jumper(0.0);
        jumper.setCoins(5);
        other.setCoins(6);

        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(8);
        assertThat(other.getCoins()).isEqualTo(3);
    }

    // Roubo de um Jumper com número ímpar de moedas
    @Test
    public void testStealOddCoins() {
        Jumper other = new Jumper(0.0);
        jumper.setCoins(5);
        other.setCoins(3);

        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(7);
        assertThat(other.getCoins()).isEqualTo(1);
    }

    // ===============================================================
    //               TESTES DE FRONTEIRA
    // ===============================================================

    // Tentativa de roubo de um Jumper com 0 moedas
    @Test
    public void testStealPoorJumper() {
        Jumper other = new Jumper(0.0);
        other.setCoins(0);
        jumper.steal(other);

        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
        assertThat(other.getCoins()).isEqualTo(0);
    }

    // Tentativa de roubar a si mesmo
    @Test
    public void testSelfSteal() {
        jumper.steal(jumper);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }

    // Tentativa de roubar um Jumper nulo
    @Test
    public void testStealNull() {
        jumper.steal(null);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }
}
