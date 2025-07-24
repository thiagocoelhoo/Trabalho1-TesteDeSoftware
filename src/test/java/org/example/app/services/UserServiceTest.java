package org.example.app.services;

import org.example.app.models.User;
import org.example.app.models.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new UserService(userDAO);
    }

    @Test
    void shouldCreateUserSuccessfully() throws SQLException {
        when(userDAO.findByUsername("new_user")).thenReturn(null);

        boolean result = userService.createUser("new_user", "pass123", "avatar");

        assertThat(result).isTrue();
        verify(userDAO).insert(any(User.class));
    }

    @Test
    void shouldNotCreateUserIfUsernameExists() throws SQLException {
        when(userDAO.findByUsername("existing_user")).thenReturn(new User(1, "existing_user", "pass", "av"));

        boolean result = userService.createUser("existing_user", "pass", "av");

        assertThat(result).isFalse();
        verify(userDAO, never()).insert(any(User.class));
    }

    @Test
    void shouldHandleSQLExceptionWhenCreatingUser() throws SQLException {
        when(userDAO.findByUsername("user")).thenThrow(new SQLException("DB error"));

        boolean result = userService.createUser("user", "pass", "av");

        assertThat(result).isFalse();
    }

    @Test
    void shouldAuthenticateSuccessfully() throws SQLException {
        User user = new User(1, "user", "pass", "av");
        when(userDAO.findByUsername("user")).thenReturn(user);

        boolean authenticated = userService.authenticate("user", "pass");

        assertThat(authenticated).isTrue();
    }

    @Test
    void shouldFailAuthenticationIfUserNotFound() throws SQLException {
        when(userDAO.findByUsername("user")).thenReturn(null);

        boolean authenticated = userService.authenticate("user", "pass");

        assertThat(authenticated).isFalse();
    }

    @Test
    void shouldFailAuthenticationIfPasswordIncorrect() throws SQLException {
        User user = new User(1, "user", "pass", "av");
        when(userDAO.findByUsername("user")).thenReturn(user);

        boolean authenticated = userService.authenticate("user", "wrongpass");

        assertThat(authenticated).isFalse();
    }

    @Test
    void shouldReturnUserIfFound() throws SQLException {
        User user = new User(1, "user", "pass", "av");
        when(userDAO.findByUsername("user")).thenReturn(user);

        User result = userService.getUser("user");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldReturnNullIfSQLExceptionInGetUser() throws SQLException {
        when(userDAO.findByUsername("user")).thenThrow(new SQLException());

        User result = userService.getUser("user");

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = Arrays.asList(
                new User(1, "user1", "pass1", "a"),
                new User(2, "user2", "pass2", "b")
        );
        when(userDAO.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnRanking() {
        List<User> ranking = Arrays.asList(
                new User(1, "u1", "p1", "a", 5, 3)
        );
        when(userDAO.findAll()).thenReturn(ranking);

        List<User> result = userService.getRanking();

        assertThat(result).isEqualTo(ranking);
    }

    @Test
    void shouldIncrementTotalGames() throws SQLException {
        User user = new User(1, "u", "p", "a", 3, 1);
        when(userDAO.findByUsername("u")).thenReturn(user);

        boolean result = userService.incrementTotalGames("u");

        assertThat(result).isTrue();
        assertThat(user.getSimulationCount()).isEqualTo(4);
        verify(userDAO).update(user);
    }

    @Test
    void shouldFailToIncrementTotalGamesIfUserNotFound() throws SQLException {
        when(userDAO.findByUsername("u")).thenReturn(null);

        boolean result = userService.incrementTotalGames("u");

        assertThat(result).isFalse();
        verify(userDAO, never()).update(any());
    }

    @Test
    void shouldIncrementWins() throws SQLException {
        User user = new User(1, "u", "p", "a", 3, 2);
        when(userDAO.findByUsername("u")).thenReturn(user);

        boolean result = userService.incrementWins("u");

        assertThat(result).isTrue();
        assertThat(user.getSuccesfulSimulations()).isEqualTo(3);
        verify(userDAO).update(user);
    }

    @Test
    void shouldFailToIncrementWinsIfUserNotFound() throws SQLException {
        when(userDAO.findByUsername("u")).thenReturn(null);

        boolean result = userService.incrementWins("u");

        assertThat(result).isFalse();
        verify(userDAO, never()).update(any());
    }

    @Test
    void shouldRemoveUserSuccessfully() throws SQLException {
        boolean result = userService.removeUser("user");
        assertThat(result).isTrue();
        verify(userDAO).delete("user");
    }

    @Test
    void shouldHandleSQLExceptionOnRemoveUser() throws SQLException {
        doThrow(new SQLException()).when(userDAO).delete("user");

        boolean result = userService.removeUser("user");

        assertThat(result).isFalse();
    }

    @Test
    void shouldGetTotalSimulations() throws SQLException {
        when(userDAO.getTotalSimulations()).thenReturn(42);
        int total = userService.getTotalSimulations();
        assertThat(total).isEqualTo(42);
    }

    @Test
    void shouldReturnZeroIfSQLExceptionInTotalSimulations() throws SQLException {
        when(userDAO.getTotalSimulations()).thenThrow(new SQLException());
        assertThat(userService.getTotalSimulations()).isZero();
    }

    @Test
    void shouldGetAverageWins() throws SQLException {
        when(userDAO.getAverageWins()).thenReturn(3.5);
        double avg = userService.getAverageWins();
        assertThat(avg).isEqualTo(3.5);
    }

    @Test
    void shouldReturnZeroIfSQLExceptionInAverageWins() throws SQLException {
        when(userDAO.getAverageWins()).thenThrow(new SQLException());
        assertThat(userService.getAverageWins()).isZero();
    }

    @Test
    void shouldUpdateUser() {
        User user = new User(1, "test", "123", "av");

        userService.updateUser(user);

        verify(userDAO).update(user);
    }

    @Test
    void shouldHandleSQLExceptionInAuthenticate() throws SQLException {
        when(userDAO.findByUsername("user")).thenThrow(new SQLException("Erro simulado"));

        boolean result = userService.authenticate("user", "pass");

        assertThat(result).isFalse();
    }

    @Test
    void shouldHandleSQLExceptionWhenIncrementingTotalGames() throws SQLException {
        // Cria um usuário simulado
        User user = new User(1, "user", "pass", "avatar");

        // findByUsername funciona normalmente
        when(userDAO.findByUsername("user")).thenReturn(user);

        // update lança SQLException
        doThrow(new SQLException("Erro simulado")).when(userDAO).update(user);

        // Executa o método
        boolean result = userService.incrementTotalGames("user");

        // Valida que o resultado foi falso devido ao erro
        assertThat(result).isFalse();

        // Verifica que ambos os métodos foram chamados
        verify(userDAO).findByUsername("user");
        verify(userDAO).update(user);
    }
}

