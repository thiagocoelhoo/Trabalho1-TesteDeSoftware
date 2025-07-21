package org.example.app.models;

public class User {
    private String username;
    private String password;
    private int partidas_totais;
    private int partidas_ganhas;
    private String avatar;

    public User(String username, String password, String a) {
        this.username = username;
        this.password = password;
        this.partidas_ganhas = 0;
        this.partidas_totais = 0;
        this.avatar = a;
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

    public int getPartidas_totais() {
        return partidas_totais;
    }

    public void setPartidas_totais(int partidas_totais) {
        this.partidas_totais = partidas_totais;
    }

    public int getPartidas_ganhas() {
        return partidas_ganhas;
    }

    public void setPartidas_ganhas(int partidas_ganhas) {
        this.partidas_ganhas = partidas_ganhas;
    }
}
