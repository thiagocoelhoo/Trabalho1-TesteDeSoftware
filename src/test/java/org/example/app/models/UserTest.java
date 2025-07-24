package org.example.app.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "usuario1", "password123", "guardian 1");
    }

    // ===============================================================
    //               TESTES DE DOMÍNIO
    // ===============================================================

    @Test
    void shouldInitializeWithCorrectValues() {
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("usuario1");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getAvatar()).isEqualTo("guardian 1");
        assertThat(user.getSimulationCount()).isZero();
        assertThat(user.getSuccesfulSimulations()).isZero();
    }

    @Test
    void shouldUpdateUsernameCorrectly() {
        user.setUsername("new_user");
        assertThat(user.getUsername()).isEqualTo("new_user");
    }

    @Test
    void shouldUpdatePasswordCorrectly() {
        user.setPassword("new_pass");
        assertThat(user.getPassword()).isEqualTo("new_pass");
    }

    @Test
    void shouldUpdateAvatarCorrectly() {
        user.setAvatar("creature 2");
        assertThat(user.getAvatar()).isEqualTo("creature 2");
    }

    @Test
    void shouldUpdateSimulationCountCorrectly() {
        user.setSimulationCount(42);
        assertThat(user.getSimulationCount()).isEqualTo(42);
    }

    @Test
    void shouldUpdateSuccesfulSimulationsCorrectly() {
        user.setSuccessfulSimulations(25);
        assertThat(user.getSuccesfulSimulations()).isEqualTo(25);
    }

    @Test
    void shouldReturnCorrectToStringFormat() {
        user.setSimulationCount(10);
        user.setSuccessfulSimulations(7);
        assertThat(user.toString()).isEqualTo("Usuario{id=1, login='usuario1', pontuacao=7, simulacoesExecutadas=10}");
    }

    // ===============================================================
    //               TESTES DE FRONTEIRA
    // ===============================================================

    @Test
    void shouldntAllowEmptyUsername() {
        String oldName = user.getUsername();
        user.setUsername("");
        assertThat(user.getUsername()).isEqualTo(oldName);
    }

    @Test
    void shouldntAllowEmptyPassword() {
        String oldPassword = user.getPassword();
        user.setPassword("");
        assertThat(user.getPassword()).isEqualTo(oldPassword);
    }

    @Test
    void shouldAllowZeroSimulations() {
        user.setSimulationCount(0);
        user.setSuccessfulSimulations(0);
        assertThat(user.getSimulationCount()).isZero();
        assertThat(user.getSuccesfulSimulations()).isZero();
    }

    @Test
    void shouldAllowMaxIntSimulations() {
        user.setSimulationCount(Integer.MAX_VALUE);
        user.setSuccessfulSimulations(Integer.MAX_VALUE);
        assertThat(user.getSimulationCount()).isEqualTo(Integer.MAX_VALUE);
        assertThat(user.getSuccesfulSimulations()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void shouldHandleNegativeValuesForSimulationCount() {
        int oldSimulationCount = user.getSimulationCount();
        user.setSimulationCount(-5);
        assertThat(user.getSimulationCount()).isEqualTo(oldSimulationCount);
    }

    @Test
    void shouldHandleNegativeValuesForSuccessfulSimulations() {
        int oldCount = user.getSimulationCount();
        user.setSuccessfulSimulations(-3);
        assertThat(user.getSuccesfulSimulations()).isEqualTo(oldCount);
    }
}
