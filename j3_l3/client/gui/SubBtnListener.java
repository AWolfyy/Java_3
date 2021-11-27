package j3_l3.client.gui;

import j3_l3.client.gui.api.Sender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubBtnListener implements ActionListener {
    private final JTextField inputField;
    private final Sender sender;

    public SubBtnListener(JTextField inputField, Sender sender) {
        this.inputField = inputField;
        this.sender = sender;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sender.send(inputField.getText());
        inputField.setText("");
    }
}
