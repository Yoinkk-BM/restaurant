package Restaurant.View.Main_Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.restaurant.DatabaseConnection;

import Restaurant.Model.ModelNguoiDung;
import Restaurant.View.Form.Staff_Form.Kitchen.Kitchen_Form;

/**
 * Cửa sổ chính dành riêng cho Nhân Viên Bếp.
 * Chỉ hiển thị Kitchen_Form, không có menu điều hướng phức tạp.
 * Lý do: đầu bếp chỉ cần một màn hình duy nhất để xem và xử lý đơn.
 */
public class Main_Kitchen_Frame extends JFrame {

    private Kitchen_Form kitchenForm;
    private ModelNguoiDung user;

    // ─── Constructor không tham số (dùng để test) ────────────────────────────
    public Main_Kitchen_Frame() {
        initComponents();
        setTitle("Royal TheDreamers — Màn Hình Bếp");
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/Icons/restaurant (1).png")));
    }

    // ─── Constructor có user (dùng sau khi đăng nhập) ───────────────────────
    public Main_Kitchen_Frame(ModelNguoiDung user) {
        this.user = user;
        initComponents();
        setTitle("Royal TheDreamers — Màn Hình Bếp");
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/Icons/restaurant (1).png")));
    }

    // =========================================================================
    // KHỞI TẠO GIAO DIỆN
    // =========================================================================
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Thanh trên cùng hiển thị tên nhân viên ───────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        topBar.setBackground(new Color(30, 30, 30));

        String tenNV = (user != null) ? user.getEmail() : "Nhân Viên Bếp";
        JLabel lblUser = new JLabel("👤  " + tenNV + "   ");
        lblUser.setForeground(Color.LIGHT_GRAY);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Nút đăng xuất
        javax.swing.JButton btnLogout = new javax.swing.JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            if (kitchenForm != null) kitchenForm.stopRefresh(); // Dừng Timer
            dispose();
            Main_LoginAndRegister.main(); // Quay về màn hình login
        });

        topBar.add(lblUser);
        topBar.add(btnLogout);
        add(topBar, BorderLayout.NORTH);

        // ── Kitchen Form (màn hình chính) ─────────────────────────────────────
        kitchenForm = new Kitchen_Form();
        add(kitchenForm, BorderLayout.CENTER);

        // Dừng Timer khi đóng cửa sổ
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (kitchenForm != null) kitchenForm.stopRefresh();
            }
        });
    }

    // =========================================================================
    // ENTRY POINT — Gọi từ Main_LoginAndRegister
    // =========================================================================
    public static void main(ModelNguoiDung user) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Main_Kitchen_Frame.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        DatabaseConnection.getInstance();
        java.awt.EventQueue.invokeLater(() -> new Main_Kitchen_Frame(user).setVisible(true));
    }

    public static void main(String[] args) {
        DatabaseConnection.getInstance();
        java.awt.EventQueue.invokeLater(() -> new Main_Kitchen_Frame().setVisible(true));
    }
}