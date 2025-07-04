package org.example.app.models;

import java.awt.image.BufferedImage;

public class User {
    private String username;
    private char[] password;
    private int score;
    private BufferedImage avatar;

    public User(String username, char[] password, BufferedImage a) {
        this.username = username;
        this.password = password;
        this.score = 0;
        avatar = a;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }
}
