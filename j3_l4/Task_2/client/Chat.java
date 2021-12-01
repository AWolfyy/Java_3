package j3_l4.Task_2.client;

import j3_l4.Task_2.client.gui.ChatFrame;
import j3_l4.Task_2.client.gui.api.Receiver;

import java.io.IOException;

public class Chat {
    private final ChatFrame chatFrame;
    private final ChatCommunication chatCommunication;

    public Chat(String host, int port) {
        chatCommunication = new ChatCommunication(host, port);
        chatFrame = new ChatFrame(data -> chatCommunication.transmit(data));

        new Thread(() -> {
            Receiver receiver = chatFrame.getReceiver();
            while (true) {
                try {
                    receiver.receive(chatCommunication.receive());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })
                .start();
    }
}
