package j3_l4.Task_2.server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        //2. На серверной стороне сетевого чата реализовать управление потоками через ExecutorService.
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            doAuth();
            listen();
        });
        executorService.shutdown();
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
                            sendMessage("Для просмотра списка команд наберите: /help");

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
            saveHistory(message);
        } catch (IOException e) {
            throw new ChatServerException("Ошибка отправки сообщения", e);
        }
    }

    public void receiveMessage() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("/pm")) {
                    sendPmMessage(message);
                } else if (message.startsWith("/history")) {
                    loadHistory();
                } else if (message.startsWith("/help")) {
                    loadHelp();
                } else if (message.startsWith("/changeNick")) {
                    changeNick(message);
                } else if (message.equals("/quit")) {
                    closeConnection();
                    break;
                } else {
                    if (!message.isEmpty()) {
                        chatServer.broadcast(String.format("От %s: %s", nick, message));
                    } else {
                        sendMessage("Сообщение не может быть пустым.");
                    }
                }
            } catch (IOException e) {
                closeConnection();
                throw new ChatServerException("Ошибка получения сообщения", e);
            }
        }
    }

    private void changeNick(String message) {
        String[] split = message.split("\\s");
        String newNick = split[1];

        if (!(isRegistered(newNick))) {
            try (Connection connection = DBConnection.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE users SET nick = ? WHERE nick = ?"
                );
                preparedStatement.setString(1, newNick);
                preparedStatement.setString(2, nick);
                preparedStatement.executeUpdate();

                chatServer.broadcast(
                        String.format("Пользователь с именем %s изменил имя на %s.", nick, newNick)
                );

                File src = new File(String.format("history_%s.txt", nick));
                File dest = new File(String.format("history_%s.txt", newNick));
                src.renameTo(dest);

                nick = newNick;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            sendMessage(String.format("Пользователь с ником %s уже существует.", newNick));
        }
    }

    private void sendPmMessage(String message) {
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
            if (!(sb.isEmpty())) {
                chatServer.sendPm(String.format("От %s: %s", this.nick, sb), nick);
            } else {
                sendMessage("Сообщение не может быть пустым.");
            }
        } else {
            sendMessage(String.format("Пользователя с ником %s нет в чате!", nick));
        }
    }

    private void saveHistory(String message) {
        if (nick != null) {
            File file = new File(String.format("history_%s.txt", nick));
            try (
                    FileOutputStream os = new FileOutputStream(file, true);
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw)
            ) {
                bw.write(message);
                bw.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadHistory() {
        int pos = 100;
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        File load = new File(String.format("history_%s.txt", nick));
        try (FileInputStream fis = new FileInputStream(load);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            while (br.ready()) {
                String str = br.readLine();
                list.add(str);
            }

            if (list.size() > pos) {
                sb.append(">>Последние 100 строк истории чата<<");
                list.stream()
                        .skip(list.size() - pos)
                        .forEach(str -> sb.append(str).append("\n"));
            } else {
                sb.append(">>Последние ").append(list.size()).append(" строк истории чата<<").append("\n");
                list.forEach(str -> sb.append(str).append("\n"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage(sb.toString());
    }

    private void loadHelp() {
        File help = new File("help.txt");
        try (FileInputStream fis = new FileInputStream(help);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            while (br.ready()) {
                String s = br.readLine();
                sendMessage(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private boolean isRegistered(String newNick) {
        try (Connection connection = DBConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT nick FROM users WHERE nick = ?"
            );

            preparedStatement.setString(1, newNick);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nick = resultSet.getString("nick");
                if (!(nick.isEmpty())) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
