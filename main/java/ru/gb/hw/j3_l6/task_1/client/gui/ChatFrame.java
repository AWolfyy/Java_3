package j3_l6.task_1.client.gui;

import j3_l6.task_1.client.gui.api.Receiver;
import j3_l6.task_1.client.gui.api.Sender;

import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {
    private final JTextArea chatArea;

    public ChatFrame(Sender sender) {
        setTitle("Chat v0.1");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(600, 300, 500, 500);

        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        top.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(top, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());

        JTextField inputField = new JTextField();
        SubBtnListener subBtnListener = new SubBtnListener(inputField, sender);
        inputField.addActionListener(subBtnListener);
        JButton subBtn = new JButton("Отправить");
        subBtn.addActionListener(subBtnListener);

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(subBtn, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    public Receiver getReceiver() {
        return data -> {
            if (!data.isEmpty()) {
                chatArea.append(data);
                chatArea.append("\n");
            }
        };
    }
}
