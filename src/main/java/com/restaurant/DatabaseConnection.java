package com.restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // ĐƯA LOGIC KẾT NỐI VÀO HÀM KHỞI TẠO NÀY
    private DatabaseConnection() {
        try {
final String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhaHang;encrypt=false;trustServerCertificate=true";
            final String username = "sa";
            final String password = "202306";
            
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối Database trong Singleton:");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}