package j3_l2.server;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private final ClientHandler clientHandler;

    public MyTimerTask(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() {
        clientHandler.sendMessage("Вы отключены от сервера за 2ух минутное бездействие");
        clientHandler.closeConnection();
    }
}
