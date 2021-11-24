package j3_l2.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//1. Добавить в сетевой чат авторизацию через базу данных SQLite.
public class UserRepository {

    public Optional<User> findByLoginAndPassword(String login, String password) {
        if (DBConnection.getConnection().isPresent()) {
            try (Connection connection = DBConnection.getConnection().get()) {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM users WHERE login = ? AND password = ?"
                );
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(new User(
                            resultSet.getString("login"),
                            resultSet.getString("password"),
                            resultSet.getString("nick")
                    ));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }
}
