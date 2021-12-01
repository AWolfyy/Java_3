package j3_l4.Task_2.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatCommunication {
    private final DataInputStream in;
    private final DataOutputStream out;

    public ChatCommunication(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ClientException("Ошибка соединения клиента", e);
        }
    }

    public void transmit(String data) {
        try {
            out.writeUTF(data);
        } catch (IOException e) {
            throw new ClientException("Ошибка отправки", e);
        }
    }

    public String receive() throws IOException {
        return in.readUTF();
    }
}
