package org.example.app.services;

import org.example.app.models.User;
import org.example.app.models.dao.UserDAO;

import java.sql.SQLException;
import java.util.List;

// classe intermediária de controle de processos do usuário na base de dados
// compartilhada regras de negócio entre telas
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // adicionar usuário
    public boolean createUser(String username, String password, String avatar) {
        try {
            // se usuário já estiver cadastrado, não deve prosseguir
            if (userDAO.findByUsername(username) != null) {
                System.err.println("Erro: O nome de usuário já existe.");
                return false;
            }

            User user = new User(null, username, password, avatar);
            userDAO.insert(user);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    // verifica se a senha digitada equivale à senha cadastrada para tal usuário
    public boolean authenticate(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);
            return user != null && user.getPassword().equals(password);
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar: " + e.getMessage());
            return false;
        }
    }

    public User getUser(String username) {
        try {
            return userDAO.findByUsername(username);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getRanking() {
        return userDAO.findAll();
    }

    // aumentar total de simulações rodadas pelo usuário ativo
    public boolean incrementTotalGames(String username) {
        try {
            User user = userDAO.findByUsername(username);
            if (user == null) return false;
            user.setSimulationCount(user.getSimulationCount() + 1);
            userDAO.update(user);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao incrementar partidas: " + e.getMessage());
            return false;
        }
    }

    // aumentar total de simulações bem sucedidas do usuário ativo
    public boolean incrementWins(String username) {
        try {
            User user = userDAO.findByUsername(username);
            if (user == null) return false;
            user.setSuccessfulSimulations(user.getSuccesfulSimulations() + 1);
            userDAO.update(user);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao incrementar vitórias: " + e.getMessage());
            return false;
        }
    }

    public boolean clearAllUsers() {
        try {
            List<User> allUsers = userDAO.findAll();
            for (User user : allUsers) {
                userDAO.delete(user.getUsername());
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao limpar usuários: " + e.getMessage());
            return false;
        }
    }

    public boolean removeUser(String username) {
        try {
            userDAO.delete(username);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao remover usuário: " + e.getMessage());
            return false;
        }
    }

    public int getTotalSimulations() {
        try {
            return userDAO.getTotalSimulations();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar total de partidas: " + e.getMessage());
            return 0;
        }
    }

    public double getAverageWins() {
        try {
            return userDAO.getAverageWins();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar média de vitórias: " + e.getMessage());
            return 0.0;
        }
    }
}
