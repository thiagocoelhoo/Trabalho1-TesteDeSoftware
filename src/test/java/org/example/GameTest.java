package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    // ==============================================================
    //     Criação de criaturas - Testes de domínio e fronteira
    // ==============================================================

    @Test
    public void testJumperAmountIsCorrect() {
        Game game = new Game();
        game.createJumpers(1000);
        CircularLinkedList<Jumper> jumpers = game.getJumpers();
        assertThat(jumpers.size()).isEqualTo(1000);
    }

    @Test
    public void testZeroJumperAmountShouldThrowException() {
        Game game = new Game();
        assertThatThrownBy(() -> game.createJumpers(0)).
                isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testNegativeJumperAmountShouldThrowException() {
        Game game = new Game();
        assertThatThrownBy(() -> game.createJumpers(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ==============================================================
    //     Quantidade de moedas - Testes de domínio
    // ==============================================================

    @Test
    public void testInitialCoinAmount() {
        final int INITIAL_COINS = 1_000_000;

        Game game = new Game();
        game.createJumpers(100);
        CircularLinkedList<Jumper> jumpers = game.getJumpers();

        for (Jumper jumper : jumpers.toList()) {
            assertThat(jumper.getCoins()).isEqualTo(INITIAL_COINS);
        }
    }

    // ==============================================================
    //     Encontrar criatura mais próxima
    // ==============================================================

    @Test
    public void testFindNearestJumperAtLeft() {
        Game game = new Game();
        game.createJumpers(3);

        List<Jumper> jumpers = game.getJumpers().toList();

        Jumper j1 = jumpers.get(0);
        Jumper j2 = jumpers.get(1);
        Jumper j3 = jumpers.get(2);

        j1.setPosition(-1000.0);
        j2.setPosition( -500.0);
        j3.setPosition(10000.0);

        assertThat(game.findNearestJumper(j2)).isEqualTo(j1);
    }

    @Test
    public void testFindNearestJumperAtRight() {
        Game game = new Game();
        game.createJumpers(3);

        List<Jumper> jumpers = game.getJumpers().toList();

        Jumper j1 = jumpers.get(0);
        Jumper j2 = jumpers.get(1);
        Jumper j3 = jumpers.get(2);

        j1.setPosition(-1000.0);
        j2.setPosition( 9500.0);
        j3.setPosition(10000.0);

        assertThat(game.findNearestJumper(j2)).isEqualTo(j3);
    }

    // ==============================================================
    //     Update - Testes de domínio e fronteira
    // ==============================================================

    @Test
    public void testUpdateDeltaTimeZeroShouldDoNothing() {
        // Se o jogo for atualizado com um deltaTime = 0.
        // Nenhuma criatura deve ser movida.

        Game game = new Game();
        game.createJumpers(100);

        List<Jumper> jumpers = game.getJumpers().toList();
        List<Double> initialPositions = new ArrayList<>();

        for (Jumper jumper : jumpers) {
            initialPositions.add(jumper.getX());
        }

        // Atualizar simulação várias vezes com deltaTime = 0
        for (int i = 0; i < 100; i++) {
            game.update(0.0);
        }

        // Verificar se todas as criaturas permaneceram no mesmo lugar após vários os vários updates com deltaTime = 0
        for (int i = 0; i < jumpers.size(); i++) {
            Jumper jumper = jumpers.get(i);
            Double initialPosition = initialPositions.get(i);
            assertThat(jumper.getX()).isEqualTo(initialPosition);
        }
    }

    @Test
    public void testUpdateDeltaTimeNegativeShouldThrowException() {
        // Um intervalo de tempo negativo não faz sentido,
        // então uma exceção deve ser lançada.

        Game game = new Game();
        game.createJumpers(100);

        assertThatThrownBy(() -> game.update(-0.1))
                .isInstanceOf(IllegalArgumentException.class);
    }


    // =================================

    @Test
    void handleStealAndRemoval_ShouldStealHalfCoinsFromNearest() {
        game.createJumpers(2);
        Jumper j1 = game.getJumpers().toList().get(0);
        Jumper j2 = game.getJumpers().toList().get(1);

        j2.setCoins(100);
        game.setCurrentJumper(j1);

        game.handleStealAndRemoval();

        assertThat(j1.getCoins()).isEqualTo(Jumper.INITIAL_COIN_AMOUNT + 50);
        assertThat(j2.getCoins()).isEqualTo(50);
    }

    @Test
    void handleStealAndRemoval_ShouldRemoveJumperWhenCoinsZero() {
        game.createJumpers(2);
        Jumper j1 = game.getJumpers().toList().get(0);
        Jumper j2 = game.getJumpers().toList().get(1);

        j2.setCoins(1); // após roubo, ficará com 0

        for (int i = 0; i < 1000; i++) {
            game.update(0.1);
        }

        assertThat(game.getJumpers().toList()).doesNotContain(j2);
    }

    // ==================================================

    @Test
    void updateJumpersPhysics_ShouldStopJumperIfOutOfLeftBorder() {
        game.createJumpers(1);
        Jumper jumper = game.getJumpers().toList().get(0);

        // Saltar para além da borda esqueda da tela
        jumper.jumpTo(Game.BORDER_LEFT - 100);

        // Simula vários updates
        for (int i = 0; i < 100; i++) {
            game.updateJumpersPhysics(0.1);
        }

        // Verificar se a criatura está exatamente na borda esquerda da tela
        assertThat(jumper.getX()).isEqualTo(Game.BORDER_LEFT);
        assertThat(jumper.isMoving()).isFalse();
    }

    @Test
    void updateJumpersPhysics_ShouldStopJumperIfOutOfRightBorder() {
        game.createJumpers(1);
        Jumper jumper = game.getJumpers().toList().get(0);

        // Saltar para além da borda direita da tela
        jumper.jumpTo(Game.BORDER_RIGHT + 100);

        // Simula vários updates
        for (int i = 0; i < 100; i++) {
            game.updateJumpersPhysics(0.1);
        }

        // Verificar se a criatura está exatamente na borda direita da tela
        assertThat(jumper.getX()).isEqualTo(Game.BORDER_RIGHT);
        assertThat(jumper.isMoving()).isFalse();
    }

    // ====================================================================
    //     Verificar se as criaturas são selecionadas na ordem correta
    // ====================================================================

    @Test
    void testJumperSequenceIsCorrect() {
        game.createJumpers(100);
        List<Jumper> jumpers = game.getJumpers().toList();
        Jumper currentJumper = null;
        int counter = 0;

        while (currentJumper != jumpers.get(jumpers.size() - 1)) {
            game.selectNextJumper();
            currentJumper = game.getCurrentJumper();

            if (currentJumper != null) {
                assertThat(currentJumper).isEqualTo(jumpers.get(counter));
                counter += 1;
            }
        }
    }

}
