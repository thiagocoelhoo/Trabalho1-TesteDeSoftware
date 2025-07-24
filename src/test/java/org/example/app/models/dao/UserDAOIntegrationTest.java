package org.example.app.models.dao;

import org.example.app.models.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDAOIntegrationTest extends DatabaseIntegrationTestBase{
    // insert
    @Test
    void insert(){
        User user1 = new User(null,"usuario1", "senha1", "guardian 1");
        User user2 = new User(null, "usuario2", "senha2", "guardian 2");
        dao.insert(user1);

        List<User> usersAfterInsert = dao.findAll();
        assertThat(usersAfterInsert)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(user1);

        dao.insert(user2);
        List<User> usersAfterInsertAgain = dao.findAll();
        assertThat(usersAfterInsertAgain).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(user1, user2);
    }

    // find by username
    @Test
    void findOne() throws SQLException {
        User user1 = new User(null, "usuario1", "senha1", "guardian 1");
        dao.insert(user1);

        assertThat(dao.findByUsername(user1.getUsername())).usingRecursiveComparison().isEqualTo(user1);
    }

    // find all
    @Test
    void findAll() throws SQLException {
        User user1 = new User(null, "usuario1", "senha1", "guardian 1");
        User user2 = new User(null, "usuario2", "senha2", "guardian 2");
        dao.insert(user1);
        dao.insert(user2);
        assertThat(dao.findAll()).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(user1, user2);
    }

    // update
    @Test
    void update() throws SQLException {
        User user1 = new User(null,"usuario1", "senha1", "guardian 1");
        dao.insert(user1);

        // id hard coded para fins de validação de teste
        User newUser1 = new User(7,"usuario1", "novasenha1", "guardian 2");
        dao.update(newUser1);
        assertThat(dao.findByUsername(newUser1.getUsername())).usingRecursiveComparison().isEqualTo(newUser1);
    }

    // delete
    @Test
    void delete() throws SQLException {
        User user1 = new User(null, "usuario1", "senha1", "guardian 1");
        dao.insert(user1);

        dao.delete("usuario1");
        List<User> usersAfterDelete = dao.findAll();
        assertThat(usersAfterDelete).isEmpty();
    }

    // get total simulations
    @Test
    void getTotalSim() throws SQLException {
        User user1 = new User(null, "usuario1", "senha1", "guardian 1");
        user1.setSimulationCount(10);
        User user2 = new User(null, "usuario2", "senha2", "guardian 2");
        user2.setSimulationCount(20);

        dao.insert(user1);
        dao.insert(user2);

        int totalSim = dao.getTotalSimulations();
        assertThat(totalSim).isEqualTo(user1.getSimulationCount() + user2.getSimulationCount());
    }

    // get average wins
    @Test
    void getAverageWins() throws SQLException {
        User user1 = new User(null, "usuario1", "senha1", "guardian 1");
        user1.setSuccessfulSimulations(5);
        User user2 = new User(null, "usuario2", "senha2", "guardian 2");
        user2.setSuccessfulSimulations(15);

        dao.insert(user1);
        dao.insert(user2);

        double averageWins = dao.getAverageWins();
        assertThat(averageWins).isEqualTo(10);
    }
}
