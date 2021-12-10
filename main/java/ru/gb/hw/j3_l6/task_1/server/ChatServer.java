package j3_l6.task_1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private final static Logger logger = LogManager.getLogger(ChatServer.class);

    private final Set<ClientHandler> loggedClients;
    private final AuthService authService;
    private final ExecutorService executorService;

    public ChatServer() {
        authService = new AuthService(new UserRepository());
        loggedClients = new HashSet<>();
        executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(8080)){
            logger.info("Сервер запущен.");
            while (true) {
                logger.info("Ждем подключения...");
                Socket socket = serverSocket.accept();
                logger.info("Успешное подключение клиента.");

                new ClientHandler(socket, this, logger);
            }
        } catch (IOException e) {
            logger.error("Ошибка сервера", e);
        } finally {
            executorService.shutdown();
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void sendPm(String message, String nick) {

        for (ClientHandler clientHandler :
                loggedClients) {
            if (clientHandler.getNick().equals(nick)) {
                logger.info("Клиент отправил приватное сообщение");
                clientHandler.sendMessage(message);
            }
        }
    }

    public void broadcast(String message) {
        for (ClientHandler clientHandler :
                loggedClients) {
            logger.info("Клиент отправил сообщение");
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
