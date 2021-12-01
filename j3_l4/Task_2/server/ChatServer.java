package j3_l4.Task_2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private final Set<ClientHandler> loggedClients;
    private final AuthService authService;

    public ChatServer() {
        authService = new AuthService(new UserRepository());
        loggedClients = new HashSet<>();
        try (ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("Сервер запущен.");
            while (true) {
                System.out.println("Ждем подключения...");
                Socket socket = serverSocket.accept();
                System.out.println("Успешное подключение.");

                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            throw new ChatServerException("Ошибка сервера.", e);
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void sendPm(String message, String nick) {
        for (ClientHandler clientHandler :
                loggedClients) {
            if (clientHandler.getNick().equals(nick)) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public void broadcast(String message) {
        for (ClientHandler clientHandler :
                loggedClients) {
            clientHandler.sendMessage(message);
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        loggedClients.add(clientHandler);
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        loggedClients.remove(clientHandler);
    }

    public boolean isLoggedIn(String nick) {
        return loggedClients.stream().filter(client -> client.getNick().equals(nick)).findFirst().isPresent();
    }
}
