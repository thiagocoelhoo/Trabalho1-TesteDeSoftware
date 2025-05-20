package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void testCoinsInitialAmount() {
        Jumper jumper = new Jumper(0, 0);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }

    @Test
    public void testSteal() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper1.steal(jumper2);

        assertThat(jumper1.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT + INITIAL_COIN_AMOUNT / 2);
        assertThat(jumper2.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT / 2);
    }

    @Test
    public void testStealPoorJumper() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper2.setCoins(0);
        jumper1.steal(jumper2);

        assertThat(jumper1.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
        assertThat(jumper2.getCoins()).isEqualTo(0);
    }

    @Test
    public void testStealRichJumper() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper2.setCoins(Integer.MAX_VALUE);
        jumper1.steal(jumper2);

        assertThat(jumper2.getCoins()).isEqualTo(Integer.MAX_VALUE / 2);
        assertThat(jumper1.getCoins()).isEqualTo(Integer.MAX_VALUE + Integer.MAX_VALUE / 2);
    }

    @Test
    public void testStealEvenCoins() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper1.setCoins(5);
        jumper2.setCoins(6);

        jumper1.steal(jumper2);

        assertThat(jumper1.getCoins()).isEqualTo(8);
        assertThat(jumper2.getCoins()).isEqualTo(3);
    }

    @Test
    public void testStealOddCoins() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper1.setCoins(5);
        jumper2.setCoins(3);

        jumper1.steal(jumper2);

        assertThat(jumper1.getCoins()).isEqualTo(7);
        assertThat(jumper2.getCoins()).isEqualTo(1);
    }

    @Test
    public void testSelfSteal() {
        Jumper jumper = new Jumper(0, 0);
        jumper.steal(jumper);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }

    @Test
    public void testStealNull() {
        Jumper jumper = new Jumper(0, 0);
        jumper.steal(null);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }
}
