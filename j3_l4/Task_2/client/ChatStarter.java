package j3_l4.Task_2.client;

public class ChatStarter {
    public static void run(String host, int port) {
        new Chat(host, port);
    }

    public static void run() {
        run("localhost", 8080);
    }
}
