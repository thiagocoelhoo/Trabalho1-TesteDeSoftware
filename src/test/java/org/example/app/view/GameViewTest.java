package org.example.app.view;

import org.example.app.controllers.GameController;
import org.example.app.models.Jumper;
import org.example.app.models.JumperType;
import org.example.app.models.User;
import org.example.app.services.UserService;
import org.example.utils.CircularLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameViewTest {

    private GameView gameView;
    private GameController gameController;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        gameController = mock(GameController.class);
        userService = mock(UserService.class);
        user = new User(1, "test", "pass", "guardian");
        doNothing().when(userService).updateUser(any());
        gameView = new GameView(gameController, user, userService);
    }

    @Test
    void shouldInitializeCorrectly() {
        assertThat(gameView).isNotNull();
        assertThat(gameView.getPreferredSize()).isEqualTo(new Dimension(800, 450));
        assertThat(gameView.getBackground()).isEqualTo(Color.BLACK);
    }

    @Test
    void shouldCallUpdateOnActionPerformed() {
        when(gameController.isSimulationFinished()).thenReturn(false);

        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "update");
        gameView.actionPerformed(event);

        verify(gameController, atLeastOnce()).update(anyDouble());
    }

    @Test
    void shouldSetInvisibleWhenSimulationFinishes() {
        when(gameController.isSimulationFinished()).thenReturn(true);

        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "update");
        gameView.setVisible(true);
        gameView.actionPerformed(event);

        assertThat(gameView.isVisible()).isFalse();
    }

    @Test
    void checkWinShouldReturnFalseIfMoreThanTwoJumpers() {
        List<Jumper> jumpers = List.of(mock(Jumper.class), mock(Jumper.class), mock(Jumper.class));
        CircularLinkedList<Jumper> mockedList = mock(CircularLinkedList.class);
        when(mockedList.toList()).thenReturn(List.of(
                new Jumper(0),
                new Jumper(1),
                new Jumper(2)

        ));
        when(gameController.getJumpers()).thenReturn(mockedList);

        assertThat(gameView.checkWin()).isFalse();
    }

    @Test
    void checkWinShouldReturnFalseIfAnyIsCluster() {
        Jumper j1 = mock(Jumper.class);
        j1.type = JumperType.CRIATURA;

        Jumper j2 = mock(Jumper.class);
        j2.type = JumperType.CLUSTER;

        CircularLinkedList<Jumper> mockedList = mock(CircularLinkedList.class);
        when(mockedList.toList()).thenReturn(List.of(j1, j2));
        when(gameController.getJumpers()).thenReturn(mockedList);

        assertThat(gameView.checkWin()).isFalse();
    }

    @Test
    void checkWinShouldReturnTrueIfExactlyTwoAndNoCluster() {
        Jumper j1 = mock(Jumper.class);
        when(j1.type).thenReturn(JumperType.CRIATURA);
        Jumper j2 = mock(Jumper.class);
        when(j2.type).thenReturn(JumperType.GUARDIAO);
        List<Jumper> jumpers = List.of(j1, j2);

        CircularLinkedList<Jumper> mockedList = mock(CircularLinkedList.class);
        when(mockedList.toList()).thenReturn(List.of(
                new Jumper(0),
                new Jumper(0)
        ));
        when(gameController.getJumpers()).thenReturn(mockedList);

        assertThat(gameView.checkWin()).isTrue();
    }
    @Test
    void shouldDrawJumperForCriaturaRedAndGuardian() {
        Graphics g = mock(Graphics.class);

        Jumper j1 = mock(Jumper.class); // tipo CRIATURA < 100 moedas
        when(j1.getX()).thenReturn(100.0);
        when(j1.getY()).thenReturn(200.0);
        when(j1.getCoins()).thenReturn(50);
        j1.type = JumperType.CRIATURA;

        Jumper j2 = mock(Jumper.class); // tipo GUARDIAO
        when(j2.getX()).thenReturn(150.0);
        when(j2.getY()).thenReturn(300.0);
        when(j2.getCoins()).thenReturn(200);
        j2.type = JumperType.GUARDIAO;

        gameView.setSize(800, 450);
        gameView.drawJumper(g, j1);
        gameView.drawJumper(g, j2);
    }

    @Test
    void shouldDrawScenario() {
        Graphics g = mock(Graphics.class);
        gameView.setSize(800, 450);
        gameView.drawScenario(g);
    }

    @Test
    void shouldPaintComponentCompletely() {
        // mock do graphics
        Graphics g = mock(Graphics.class);

        // mock do jumper
        Jumper jumper = mock(Jumper.class);
        when(jumper.getX()).thenReturn(150.0);
        when(jumper.getY()).thenReturn(300.0);
        when(jumper.getCoins()).thenReturn(10);
        jumper.type = JumperType.CRIATURA;

        // mock do controller com jumper
        CircularLinkedList<Jumper> jumperList = mock(CircularLinkedList.class);
        when(jumperList.toList()).thenReturn(List.of(jumper));
        when(gameController.getJumpers()).thenReturn(jumperList);

        // mock de getPreferredSize (caso necessário)
        gameView.setSize(800, 450);

        // invoca método real
        gameView.paintComponent(g);

        // verificação explícita (opcional, só para garantir chamadas feitas)
        verify(gameController).getJumpers();
        verify(g, atLeastOnce()).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    void shouldStartGameViewTimer() {
        gameView.start();
    }
}
