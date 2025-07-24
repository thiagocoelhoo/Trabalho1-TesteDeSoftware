package org.example.page;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JTextComponentFixture;

public class NewUserPage {
    private final FrameFixture window;

    public NewUserPage(FrameFixture window) {
        this.window = window;
    }

    public void createUser(String username, String password, String avatar) {
        JTextComponentFixture nameField = window.textBox("nameField");
        JTextComponentFixture passwordField = window.textBox("passwordField");
        JComboBoxFixture avatarComboBox = window.comboBox("avatarComboBox");
        JButtonFixture createButton = window.button("createButton");

        nameField.enterText(username);
        passwordField.enterText(password);
        avatarComboBox.selectItem(avatar);
        createButton.click();
    }
}