package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest {
    /*
    1) N criaturas (testar input para criaturas de 1 a n)
    2) Dinheiro = um milhão (testar qt por bixo)
    3) testar posição Xi de cada criatura
    4) verificar ordem de processamento de criaturas (garantir que
    são processadas em ordem especialmente em deleções)
    5) Muita coisa:
        5.1)  nova posição xI
        5.2)  valor r (random)
        5.3)  equação completa
        5.4)  roubo de moedas e adição à quantidade do atual
     */

    @Test
    public void testQuantidadeCriaturas() {
        Game game = new Game();
        game.createJumpers(1000);
        ArrayList<Jumper> jumpers = game.getJumpers();
        assertThat(jumpers.size()).isEqualTo(1000);
    }

    @Test
    public void testQuantidadeCriaturasNegativo() {
        Game game = new Game();
        assertThrows(
                IllegalArgumentException.class,
                () -> game.createJumpers(-1)
        );
    }

    @Test
    public void testQuantidadeCriaturaZero() {
        Game game = new Game();
        game.createJumpers(0);
        ArrayList<Jumper> jumpers = game.getJumpers();
        assertThat(jumpers.size()).isEqualTo(0);
    }

    @Test
    public void testMoedasInicial() {
        final int INITIAL_COINS = 1_000_000;

        Game game = new Game();
        ArrayList<Jumper> jumpers = game.getJumpers();

        for (Jumper jumper : jumpers) {
            assertThat(jumper.getCoins()).isEqualTo(INITIAL_COINS);
        }
    }
}
