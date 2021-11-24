package j3_l2.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DBConnection {
    public static Optional<Connection> getConnection() {
        try {
            return Optional.of(
                    DriverManager.getConnection(
                            "jdbc:sqlite:C:\\Users\\Brux\\YandexDisk\\Общая папка\\SQLite\\gb.db"
                    )
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
