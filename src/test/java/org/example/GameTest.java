package org.example;

import org.example.app.controllers.GameController;
import org.example.app.models.Jumper;
import org.example.app.models.JumperType;
import org.example.utils.CircularLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GameTest {
    private GameController game;

    @BeforeEach
    public void setUp() {
        game = new GameController();
    }

    // ==============================================================
    //     Criação de criaturas - Testes de domínio
    // ==============================================================

    // Teste de domínio: criar 1000 criaturas com sucesso
    @Test
    public void testJumperAmountIsCorrect() {
        GameController game = new GameController();
        game.createJumpers(1000);
        CircularLinkedList<Jumper> jumpers = game.getJumpers();
        assertThat(jumpers.size()).isEqualTo(1001); // Considerando o guardião como uma outra criatura
    }

    // ==============================================================
    //     Quantidade de moedas - Testes de domínio
    // ==============================================================

    // Teste de domínio: cada Jumper deve iniciar com a quantidade correta de moedas
    @Test
    public void testInitialCoinAmount() {
        final int INITIAL_COINS = 1_000_000;

        GameController game = new GameController();
        game.createJumpers(100);
        CircularLinkedList<Jumper> jumpers = game.getJumpers();

        for (Jumper jumper : jumpers.toList()) {
            if (jumper.type == JumperType.GUARDIAO) continue; // Ignorar o guardião pois ele começa com 0 moedas
            assertThat(jumper.getCoins()).isEqualTo(INITIAL_COINS);
        }
    }

    // ==============================================================
    //     Encontrar criatura mais próxima - Testes de domínio
    // ==============================================================

    // Teste de domínio: garantir que a criatura mais próxima está à esquerda
    @Test
    public void testFindNearestJumperAtLeft() {
        GameController game = new GameController();
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

    // Teste de domínio: garantir que a criatura mais próxima está à direita
    @Test
    public void testFindNearestJumperAtRight() {
        GameController game = new GameController();
        game.createJumpers(3);

        List<Jumper> jumpers = game.getJumpers().toList();
        Jumper j1 = jumpers.get(0);
        Jumper j2 = jumpers.get(1);
        Jumper j3 = jumpers.get(2);

        j1.setPosition(-1000.0);
        j2.setPosition(9500.0);
        j3.setPosition(10000.0);

        assertThat(game.findNearestJumper(j2)).isEqualTo(j3);
    }

    // ==============================================================
    //     Física e atualização - Testes de domínio
    // ==============================================================

    // Teste de domínio: roubo de moedas entre criaturas
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

    // Teste de domínio: remoção de criatura quando suas moedas chegam a zero
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

    // ==============================================================
    //     Seleção de criaturas - Testes de domínio
    // ==============================================================

    // Teste de domínio: a seleção de criaturas deve seguir a ordem da lista
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

    // ==============================================================
    //     Atualização - Testes de fronteira
    // ==============================================================

    // Teste de fronteira: deltaTime = 0 não deve alterar a posição das criaturas
    @Test
    public void testUpdateDeltaTimeZeroShouldDoNothing() {
        // Se o jogo for atualizado com um deltaTime = 0.
        // Nenhuma criatura deve ser movida.

        GameController game = new GameController();
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

        // Verificar se todas as criaturas permaneceram no mesmo lugar após os vários updates com deltaTime = 0
        for (int i = 0; i < jumpers.size(); i++) {
            Jumper jumper = jumpers.get(i);
            Double initialPosition = initialPositions.get(i);
            assertThat(jumper.getX()).isEqualTo(initialPosition);
        }
    }

    // Teste de fronteira: deltaTime negativo deve lançar exceção
    @Test
    public void testUpdateDeltaTimeNegativeShouldThrowException() {
        // Um intervalo de tempo negativo não faz sentido,
        // então uma exceção deve ser lançada.

        GameController game = new GameController();
        game.createJumpers(100);

        assertThatThrownBy(() -> game.update(-0.1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ==============================================================
    //     Criação de criaturas - Testes de fronteira
    // ==============================================================

    // Teste de fronteira: criação de 0 criaturas deve lançar exceção
    @Test
    public void testZeroJumperAmountShouldThrowException() {
        GameController game = new GameController();
        assertThatThrownBy(() -> game.createJumpers(0)).
                isInstanceOf(IllegalArgumentException.class);
    }

    // Teste de fronteira: criação de quantidade negativa de criaturas deve lançar exceção
    @Test
    public void testNegativeJumperAmountShouldThrowException() {
        GameController game = new GameController();
        assertThatThrownBy(() -> game.createJumpers(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ==============================================================
    //     Física - Testes de fronteira (borda da tela)
    // ==============================================================

    // Teste de fronteira: criatura deve parar se ultrapassar borda esquerda
    @Test
    void updateJumpersPhysics_ShouldStopJumperIfOutOfLeftBorder() {
        game.createJumpers(1);
        Jumper jumper = game.getJumpers().toList().get(0);

        // Saltar para além da borda esqueda da tela
        jumper.jumpTo(GameController.BORDER_LEFT - 100);

        // Simula vários updates
        for (int i = 0; i < 100; i++) {
            game.updateJumpersPhysics(0.1);
        }

        // Verificar se a criatura está exatamente na borda esquerda da tela
        assertThat(jumper.getX()).isEqualTo(GameController.BORDER_LEFT);
        assertThat(jumper.isMoving()).isFalse();
    }

    // Teste de fronteira: criatura deve parar se ultrapassar borda direita
    @Test
    void updateJumpersPhysics_ShouldStopJumperIfOutOfRightBorder() {
        game.createJumpers(1);
        Jumper jumper = game.getJumpers().toList().get(0);

        // Saltar para além da borda direita da tela
        jumper.jumpTo(GameController.BORDER_RIGHT + 100);

        // Simula vários updates
        for (int i = 0; i < 100; i++) {
            game.updateJumpersPhysics(0.1);
        }

        // Verificar se a criatura está exatamente na borda direita da tela
        assertThat(jumper.getX()).isEqualTo(GameController.BORDER_RIGHT);
        assertThat(jumper.isMoving()).isFalse();
    }

}
