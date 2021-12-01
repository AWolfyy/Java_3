package j3_l4.Task_2.server;

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
