package j3_l2.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nick;
    private final ChatServer chatServer;
    private final Socket socket;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ChatServerException("Ошибка подключения.", e);
        }

        doAuth();

        new Thread(() -> listen())
                .start();
    }

    public String getNick() {
        return nick;
    }

    public void listen() {
        receiveMessage();
    }

    private void doAuth() {
        sendMessage("Добро пожаловать в чат");
        sendMessage("У вас 2 минуты на авторизацию.");

        TimerTask timerTask = new MyTimerTask(this);
        Timer timer = new Timer();
        timer.schedule(timerTask, 120000);

        while (true) {
            sendMessage("Пожалуйста, введите логин и пароль по схеме:");
            sendMessage("/auth your_login your_password");

            try {
                String massage = in.readUTF();
                if (massage.startsWith("/auth")) {
                    String[] credentialsStruct = massage.split("\\s");
                    String login = credentialsStruct[1];
                    String password = credentialsStruct[2];

                    Optional<User> mayBeCredentials = chatServer.getAuthService()
                            .findUser(login, password);

                    if (mayBeCredentials.isPresent()) {
                        User credentials = mayBeCredentials.get();
                        if (!chatServer.isLoggedIn(credentials.getNick())) {
                            nick = credentials.getNick();
                            chatServer.broadcast(String.format("Пользователь с именем %s вошел в чат.", nick));
                            chatServer.subscribe(this);
                            sendMessage("Успешная аутентификация - общайтесь!");
                            sendMessage("Для отправки личного сообщения напишите сообщение вида: ");
                            sendMessage("/pm nick \"message\"");
                            sendMessage("Для смены ника напишите сообщение вида: ");
                            sendMessage("/changeNick \"newNick\"");

                            timerTask.cancel();

                            return;
                        } else {
                            sendMessage("Пользователь c таким именем уже залогинен.");
                        }
                    } else {
                        sendMessage("Неправильный логин или пароль.");
                    }
                } else {
                    sendMessage("Неправильное аутентификационное сообщение...");
                }
            } catch (IOException e) {
                throw new ChatServerException("Ошибка аутентификации.", e);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new ChatServerException("Ошибка отправки сообщения", e);
        }
    }

    public void closeConnection() {
        chatServer.unsubscribe(this);
        chatServer.broadcast(nick + " вышел из чата.");

        try {
            in.close();
        } catch (IOException e) {
            throw new ChatServerException("Ошибка закрытия in", e);
        }

        try {
            out.close();
        } catch (IOException e) {
            throw new ChatServerException("Ошибка закрытия out", e);
        }

        try {
            socket.close();
        } catch (IOException e) {
            throw new ChatServerException("Ошибка закрытия socket", e);
        }
    }

    public void receiveMessage() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("/pm")) {
                    String[] split = message.split("\\s");
                    String nick = split[1];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 2; i < split.length; i++) {
                        if (i == split.length - 1) {
                            sb.append(split[i]);
                        } else {
                            sb.append(split[i]);
                            sb.append(" ");
                        }
                    }
                    if (chatServer.isLoggedIn(nick)) {
                        chatServer.sendPm(String.format("От %s: %s", this.nick, sb), nick);
                    } else {
                        sendMessage(String.format("Пользователя с ником %s нет в чате!", nick));
                    }
                //2.*Добавить в сетевой чат возможность смены ника.
                } else if (message.startsWith("/changeNick")) {
                    String[] split = message.split("\\s");
                    String newNick = split[1];

                    if (DBConnection.getConnection().isPresent()) {
                        try (Connection connection = DBConnection.getConnection().get()) {
                            PreparedStatement preparedStatement = connection.prepareStatement(
                                    "UPDATE users SET nick = ? WHERE nick = ?"
                            );
                            preparedStatement.setString(1, newNick);
                            preparedStatement.setString(2, nick);
                            preparedStatement.executeUpdate();
                            nick = newNick;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (message.equals("/quit")) {
                    closeConnection();
                    break;
                } else {
                    chatServer.broadcast(String.format("От %s: %s", nick, message));
                }
            } catch (IOException e) {
                closeConnection();
                throw new ChatServerException("Ошибка получения сообщения", e);
            }
        }
    }
}
