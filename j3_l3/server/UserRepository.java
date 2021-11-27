package j3_l3.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {

    public Optional<User> findByLoginAndPassword(String login, String password) {
            try (Connection connection = DBConnection.getConnection()) {
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

        return Optional.empty();
    }
}
