package com.restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Lớp quản lý kết nối cơ sở dữ liệu theo mẫu Singleton.
// Ý tưởng của mẫu này là chỉ tạo một kết nối duy nhất và chia sẻ cho toàn bộ chương trình,
// giúp tránh việc mở quá nhiều kết nối không cần thiết và giảm rủi ro lỗi liên quan đến tài nguyên.
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    // Trả về đối tượng kết nối duy nhất của lớp.
    // Nếu instance chưa tồn tại thì tạo mới, nếu đã có thì dùng lại instance cũ.
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Constructor được đặt là private để ngăn việc tạo đối tượng mới từ bên ngoài.
    // Tất cả logic kết nối SQL Server sẽ được thực hiện ngay tại đây khi instance đầu tiên được khởi tạo.
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

    // Trả về đối tượng Connection hiện tại để các controller/service có thể thực thi câu lệnh SQL.
    public Connection getConnection() {
        return connection;
    }

    // Cho phép thay thế connection hiện tại nếu cần dùng một kết nối khác trong quá trình chạy.
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}