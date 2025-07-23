package org.example.app.models;

//TODO testes para os getters e setters

public class User {
    private int id;
    private String username;
    private String password;
    private String avatar;
    private int simulationCount;
    private int succesfulSimulations;

    public User(int id, String username, String password, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.simulationCount = 0;
        this.succesfulSimulations = 0;
    }

    public User(int id, String username, String password, String av, int qtSim, int sucedidas) {
        this.username = username;
        this.password = password;
        this.avatar = av;
        this.simulationCount = qtSim;
        this.succesfulSimulations = sucedidas;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSimulationCount() {
        return simulationCount;
    }

    public void setSimulationCount(int simulationCount) {
        this.simulationCount = simulationCount;
    }

    public int getSuccesfulSimulations() {
        return succesfulSimulations;
    }
    public void setSuccesfulSimulations(int succesfulSimulations) {
        this.succesfulSimulations = succesfulSimulations;
    }

    public String toString() {
        return "Usuario{" + "id=" + id + ", login='" + username + '\'' + ", pontuacao=" + succesfulSimulations +
                ", simulacoesExecutadas=" + simulationCount + '}';
    }
}
