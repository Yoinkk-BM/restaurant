package com.restaurant;

import javax.swing.SwingUtilities;
// TODO: Import class giao diện chính của bạn vào đây
// Ví dụ: import com.restaurant.View.Form.LoginForm; 

public class Main {
    public static void main(String[] args) {
        
        // 1. KIỂM TRA KẾT NỐI DATABASE TRƯỚC KHI CHẠY APP
        System.out.println("Đang thiết lập kết nối đến cơ sở dữ liệu...");
        try {
            // Gọi Singleton DatabaseConnection
            DatabaseConnection db = DatabaseConnection.getInstance();
            
            // Giả sử bạn có viết hàm getConnection() trong class DatabaseConnection
            if (db.getConnection() != null) {
                System.out.println(" Kết nối Database thành công!");
            } else {
                System.out.println(" Kết nối Database thất bại (Connection is null).");
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối Database:");
            e.printStackTrace();
            // Nếu bắt buộc phải có DB mới chạy được app, bạn có thể thêm 'return;' ở đây để dừng chương trình.
        }

        // 2. CHẠY GIAO DIỆN NGƯỜI DÙNG (GUI)
        // Sử dụng SwingUtilities.invokeLater để đảm bảo Thread Safety cho Java Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: Khởi tạo và hiển thị Giao diện đầu tiên của bạn ở đây (thường là form Đăng nhập)
                    
                    /* MẪU VÍ DỤ:
                    LoginForm loginForm = new LoginForm();
                    loginForm.setVisible(true);
                    loginForm.setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
                    */
                    
                    System.out.println(" khởi chạy giao diện hệ thống!");
                    
                } catch (Exception e) {
                    System.err.println(" Lỗi khi khởi chạy giao diện:");
                    e.printStackTrace();
                }
            }
        });
    }
}