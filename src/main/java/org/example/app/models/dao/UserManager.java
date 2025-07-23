package org.example.app.models.dao;

import org.example.app.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserManager {

    public int createUser(String username, String password, String avatar){
        String sql = "INSERT INTO users (username, password, avatar) values (?,?,?)";
        int result = 0;
        try (Connection conn = DbManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, avatar);
            statement.executeUpdate();
            result = 1;

        } catch (SQLException e){
            if (e.getErrorCode() == 23505) {
                System.err.println("Erro de inclusão: O nome de usuário '" + username + "' já existe.");
            } else {
                System.err.println("Erro ao adicionar usuário: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean checkPassword(String username, String password){
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DbManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                String databasePass = rs.getString("password");
                return password.equals(databasePass);
            }

        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String username, String password){
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        User user = null;

        try (Connection conn = DbManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                int currentId = rs.getInt("id");
                String currentUser = rs.getString("username");
                String currentPassword = rs.getString("password");
                String currentAvatar = rs.getString("avatar");
                int currentSimCount = rs.getInt("simulationCount");
                int successCount = rs.getInt("succesfulSimulations");

                user = new User(currentId, currentUser, currentPassword, currentAvatar, currentSimCount, successCount );
            }
        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return user;
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY succesfulSimulations DESC";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()){
            while(rs.next()){
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("avatar"),
                        rs.getInt("simulationCount"),
                        rs.getInt("succesfulSimulations")
                ));
            }

        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    public int getTotalSimulations(){
        String sql = "SELECT SUM(simulationCount) AS totalSims FROM users";
        int total = 0;

        try (Connection conn = DbManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                total = rs.getInt("totalSims");
            }

        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }

    public double getAverageSuccessfulSimulations(){
        String sql = "SELECT AVG(succesfulSimulations) AS avgSims FROM users";
        double average = 0;

        try (Connection conn = DbManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)){
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                average = rs.getDouble("avgSims");
            }

        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return average;
    }

    public void removeUser(String username){
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DbManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, username);
            statement.executeUpdate();
            System.out.println("Usuário " + username + " removido com sucesso.");

        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getUserSuccessfulSimulations(String username){
        String sql = "SELECT succesfulSimulations FROM users WHERE username = ?";
        int successful = 0;

        try (Connection conn = DbManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                successful = rs.getInt("succesfulSimulations");
            }
        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }

        return successful;
    }

    public void incrementSimCount(String username){
        String sql = "UPDATE users SET simulationCount = simulationCount + 1 WHERE username = ?";
        try (Connection conn = DbManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, username);
            statement.executeUpdate();

        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void incrementSuccessfulSimulations(String username){
        String sql = "UPDATE users SET succesfulSimulations = succesfulSimulations + 1 WHERE username = ?";
        try (Connection conn = DbManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1,username);
            statement.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
