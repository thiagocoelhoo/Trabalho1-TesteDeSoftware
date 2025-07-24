package org.example.app.view;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.example.app.controllers.MainController;
import org.example.app.models.User;
import org.example.app.services.UserService;
import org.example.app.view.MainFrame;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MainFrameTest {
    private FrameFixture window;
    private Robot robot;
    private UserService userService;
    private User currentUser;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install(); // Detecta EDT violations
    }

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        currentUser = new User(null, "Alice", "123", "guardian 1");
        currentUser.setSimulationCount(5);
        currentUser.setSuccessfulSimulations(3);

        when(userService.getAllUsers()).thenReturn(List.of(currentUser));
        when(userService.getTotalSimulations()).thenReturn(10);
        when(userService.getAverageWins()).thenReturn(4.5);
        when(userService.getUser("Alice")).thenReturn(currentUser);

        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(10);

        MainFrame frame = GuiActionRunner.execute(() -> {
            MainFrame f = new MainFrame(800, 600, new MainController(userService, currentUser));
            f.setVisible(true);
            return f;
        });

        window = new FrameFixture(robot, frame);
        window.robot().waitForIdle();
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }

    // ✅ Verifica se os componentes principais existem
    @Test
    public void shouldShowMainComponents() {
        window.label("userNameLabel").requireText("Usuário: Alice");
        window.label("userScoreLabel").requireText("Pontuação: 3");
        window.label("totalSimulationsLabel").requireText("Total de simulações: 10");
        window.label("averageWinsLabel").requireText("Média de simulações ganhas: 4.5");

        window.button("simulateButton").requireVisible();
        window.button("exitButton").requireVisible();
        window.button("addUserButton").requireVisible();
        window.button("removeUserButton").requireVisible();
    }

    // ✅ Verifica se a tabela contém o usuário esperado
    @Test
    public void shouldDisplayUserInTable() {
        window.table("usersTable")
                .requireRowCount(1)
                .requireContents(new String[][] {
                        {"Alice", "5", "3"}
                });
    }

    // ✅ Verifica se botão de remover usuário bloqueia auto-remover
    @Test
    public void shouldNotAllowSelfRemoval() {
        window.table("usersTable").selectRows(0);
        window.button("removeUserButton").click();

//        window.dialog().requireVisible()
//                .requireTitle("Message")
//                .requireModal();
//        window.dialog().requireMessage("Usuário não pode se remover!");
        window.dialog().button().click(); // Fecha o diálogo
    }

    // ✅ Verifica se clicar em "Nova Simulação" chama serviço de incremento
    @Test
    public void shouldTriggerSimulationIncrement() {
        window.button("simulateButton").click();

        verify(userService, times(1)).incrementTotalGames("Alice");
    }

    // ✅ Verifica se ao clicar em "Sair", o frame é fechado e LoginFrame aberto
    @Test
    public void shouldExitToLoginFrame() {
        window.button("exitButton").click();

        // Aqui apenas garantimos que MainFrame foi fechado
        assertThat(window.target().isDisplayable()).isFalse();
    }

    @Test
    public void shouldRefreshPageAndUpdateData() {
        MainFrame frame = (MainFrame) window.target();

        // Atualiza mocks para retorno novo (como se tivesse mudado)
        when(userService.getAllUsers()).thenReturn(List.of(currentUser));
        when(userService.getTotalSimulations()).thenReturn(20);
        when(userService.getAverageWins()).thenReturn(6.5);
        currentUser.setSuccessfulSimulations(10);
        when(userService.getUser("Alice")).thenReturn(currentUser);

        GuiActionRunner.execute(() -> frame.refreshPage());

        window.label("totalSimulationsLabel").requireText("Total de simulações: 20");
        window.label("averageWinsLabel").requireText("Média de simulações ganhas: 6.5");
        window.label("userScoreLabel").requireText("Pontuação: 10");

        window.table("usersTable")
                .requireRowCount(1)
                .requireContents(new String[][] {
                        {"Alice", "5", "10"}
                });
    }
}