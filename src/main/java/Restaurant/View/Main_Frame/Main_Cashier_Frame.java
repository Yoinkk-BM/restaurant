package Restaurant.View.Main_Frame;

import javax.swing.JFrame;

import Restaurant.Model.ModelNguoiDung;
import Restaurant.View.Form.CashierPosPanel;

public class Main_Cashier_Frame extends JFrame {

    private ModelNguoiDung user;


    /**
     * Khởi tạo cửa sổ chính cho phiên làm việc của Thu Ngân.
     * Cài đặt tiêu đề, kích thước cửa sổ (1280x800), đưa cửa sổ ra giữa màn hình.
     * Khởi tạo và nạp Panel chứa nghiệp vụ bán hàng (CashierPosPanel) vào Frame.
     * * @param user Thông tin tài khoản Thu ngân đang đăng nhập
     */
    public Main_Cashier_Frame(ModelNguoiDung user) {
        this.user = user;
        
        // Cài đặt thông số cơ bản cho cửa sổ
        setTitle("Royal TheDreamers — Hệ Thống Thu Ngân (POS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null); // Hiển thị ra giữa màn hình
        
        
        add(new CashierPosPanel());
    }

    
    /**
     * Hàm main hỗ trợ khởi chạy luồng giao diện độc lập cho Thu Ngân.
     * Được gọi từ chức năng Đăng nhập sau khi xác thực đúng vai trò.
     */
    public static void main(ModelNguoiDung user) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main_Cashier_Frame(user).setVisible(true);
        });
    }
}