package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest {

    // Teste de requisito: geração correta de criaturas
    @Test
    public void testQuantidadeCriaturas() {
        Game game = new Game();
        game.createJumpers(1000);
        ArrayList<Jumper> jumpers = game.getJumpers();
        assertThat(jumpers.size()).isEqualTo(1000);
    }

    // Teste de requisito: gold inicial == um milhão para as n criaturas
    @Test
    public void testMoedasInicial() {
        final int INITIAL_COINS = 1_000_000;

        Game game = new Game();
        ArrayList<Jumper> jumpers = game.getJumpers();

        for (Jumper jumper : jumpers) {
            assertThat(jumper.getCoins()).isEqualTo(INITIAL_COINS);
        }
    }

    //TODO verificar validade dos testes de quantidade (dupla inicialização de criaturas)

    // Teste de fronteira: quantidade_criaturas < 0
    // deve retornar exceção
    @Test
    public void testQuantidadeCriaturasNegativo() {
        Game game = new Game();
        assertThrows(
                IllegalArgumentException.class,
                () -> game.createJumpers(-1)
        );
    }

    // Teste de fronteira: quantidade_criaturas == 0
    // deve retornar exceção
    @Test
    public void testQuantidadeCriaturaZero() {
        Game game = new Game();
        assertThrows(
                IllegalArgumentException.class,
                () -> game.createJumpers(0)
        );
    }

    //TODO Teste de posição inicial de cada criatura
}
