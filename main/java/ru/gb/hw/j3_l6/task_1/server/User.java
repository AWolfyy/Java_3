package j3_l6.task_1.server;

public class User {
    private final String login;
    private final String password;
    private final String nick;

    public User(String login, String password, String nick) {
        this.login = login;
        this.password = password;
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
}
