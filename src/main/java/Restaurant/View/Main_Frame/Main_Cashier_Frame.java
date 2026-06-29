package Restaurant.View.Main_Frame;

import javax.swing.JFrame;

import Restaurant.Model.ModelNguoiDung;
import Restaurant.View.Form.CashierPosPanel;

public class Main_Cashier_Frame extends JFrame {

    private ModelNguoiDung user;

    public Main_Cashier_Frame(ModelNguoiDung user) {
        this.user = user;
        
        // Cài đặt thông số cơ bản cho cửa sổ
        setTitle("Royal TheDreamers — Hệ Thống Thu Ngân (POS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null); // Hiển thị ra giữa màn hình
        
        
        add(new CashierPosPanel());
    }

    // Hàm main để gọi cửa sổ này lên từ file Login
    public static void main(ModelNguoiDung user) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main_Cashier_Frame(user).setVisible(true);
        });
    }
}