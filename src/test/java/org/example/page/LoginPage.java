package org.example.page;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;

public class LoginPage {
    private final FrameFixture window;

    public LoginPage(FrameFixture window) {
        this.window = window;
    }

    public void clickNewUser() {
        window.label("newUserLabel").click();
        // Não tentar localizar a nova janela aqui - deixar para o teste principal
    }

    public void login(String username, String password) {
        JTextComponentFixture usernameField = window.textBox("usernameField");
        JTextComponentFixture passwordField = window.textBox("passwordField");
        JButtonFixture loginButton = window.button("loginButton");

        usernameField.enterText(username);
        passwordField.enterText(password);
        loginButton.click();
    }

//    public void clickNewUser() {
//        window.label("newUserLabel").click();
//    }
}