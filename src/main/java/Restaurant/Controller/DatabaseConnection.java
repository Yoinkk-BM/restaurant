package Restaurant.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Kết nối tới DataBase của hệ thống

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {

    }

    // Thực hiện kết nối tới Database SQL Server
    public void connectToDatabase() throws SQLException {
        final String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhaHang;encrypt=false;trustServerCertificate=true";
        final String username = "sa";
        final String password = "202306";
        connection = DriverManager.getConnection(url, username, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}