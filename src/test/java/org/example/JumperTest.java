package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JumperTest {
    private final int INITIAL_COIN_AMOUNT = 1_000_000;

    // Teste de requisito: gold inicial == um milhão
    @Test
    public void testCoinsInitialAmount() {
        Jumper jumper = new Jumper(0, 0);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }

    // Teste de Requisito: nova posição continua na tela
    @Test
    public void testNovaPosX(){ //TODO des-quebrar isso aqui
        Jumper jumper = new Jumper(0, 0);
        jumper.jump();
        // int renderedX = ((jumper.getX() - minX) / (maxX - minX) * 550 + 25; ???
        // (x - minX) / (maxX - minX) * 550 + 25

        // assertThat(jumper.getX()).isBetween(0.0, 600.0);
    }

    // Teste de Requisito: valor random entre -1 e 1
    @Test
    public void testIntervaloRandom(){
        Jumper jumper = new Jumper(0, 0);
        jumper.jump();
        double getRandom = jumper.getX()/jumper.getCoins();
        assertThat(getRandom).isBetween(-1.0, 1.0);
    }

    /* Teste de Requisito: roubo
        - gold do ladrão atualizado
        - gold do roubado atualizado
    */
    @Test
    public void testSteal() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper1.steal(jumper2);

        assertThat(jumper1.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT + INITIAL_COIN_AMOUNT / 2);
        assertThat(jumper2.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT / 2);
    }

    // Teste de Fronteira: roubo de criatura sem gold
    @Test
    public void testStealPoorJumper() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper2.setCoins(0);
        jumper1.steal(jumper2);

        assertThat(jumper1.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
        assertThat(jumper2.getCoins()).isEqualTo(0);
    }

    // Teste de Fronteira: Roubo de criatura com dinheiro máximo
    @Test
    public void testStealRichJumper() {
        Jumper jumper1 = new Jumper(0, 0);
        Jumper jumper2 = new Jumper(0, 0);

        jumper2.setCoins(Integer.MAX_VALUE);
        jumper1.steal(jumper2);

        assertThat(jumper2.getCoins()).isEqualTo(Integer.MAX_VALUE / 2);
        assertThat(jumper1.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT +
                                        (int)Math.ceil(Integer.MAX_VALUE / 2.0));
    }

    // Teste de Fronteira: roubo de quantidades pares
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

    // Teste de Fronteira: roubo de quantidades ímpares
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

    // ???
    @Test
    public void testSelfSteal() {
        Jumper jumper = new Jumper(0, 0);
        jumper.steal(jumper);
        assertThat(jumper.getCoins()).isEqualTo(INITIAL_COIN_AMOUNT);
    }
}
