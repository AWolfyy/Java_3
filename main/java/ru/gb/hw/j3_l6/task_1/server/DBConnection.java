package j3_l6.task_1.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:sqlite:C:\\Users\\Brux\\YandexDisk\\Общая папка\\SQLite\\gb.db"
            );
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных.", e);
        }
    }
}
