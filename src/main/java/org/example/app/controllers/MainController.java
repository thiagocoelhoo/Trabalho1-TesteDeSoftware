package org.example.app.controllers;

import org.example.app.models.User;
import org.example.app.services.UserService;

import java.util.List;

public class MainController {
    private final UserService userService;
    private User currentUser;

    public MainController(UserService userService, User currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
    }

    public boolean canRemoveUser(String username) {
        return !username.equals(currentUser.getUsername());
    }

    public void removeUser(String username) {
        userService.removeUser(username);
    }

    public void startSimulation() {
        userService.incrementTotalGames(currentUser.getUsername());
    }

    public Object[][] getUsersTableData() {
        List<User> users = userService.getAllUsers();
        Object[][] data = new Object[users.size()][3];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            data[i][0] = u.getUsername();
            data[i][1] = u.getSimulationCount();
            data[i][2] = u.getSuccesfulSimulations();
        }
        return data;
    }

    public int getTotalSimulations() {
        return userService.getTotalSimulations();
    }

    public double getAverageWins() {
        return userService.getAverageWins();
    }

    public User getUpdatedCurrentUser() {
        currentUser = userService.getUser(currentUser.getUsername());
        return currentUser;
    }

    public String getAvatarPath(String avatar) {
        return switch (avatar) {
            case "guardian 1" -> "src/main/java/org/example/app/resources/aegislash.png";
            case "guardian 2" -> "src/main/java/org/example/app/resources/regigigas.png";
            case "creature 1" -> "src/main/java/org/example/app/resources/spoink.png";
            case "creature 2" -> "src/main/java/org/example/app/resources/spoink_red.png";
            case "cluster" -> "src/main/java/org/example/app/resources/grumpig.png";
            default -> null;
        };
    }

    public UserService getUserService() {
        return userService;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
