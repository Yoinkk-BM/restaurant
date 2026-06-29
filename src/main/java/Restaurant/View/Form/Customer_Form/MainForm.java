package Restaurant.View.Form.Customer_Form;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.border.EmptyBorder;

// Form chính ở giữa màn hình
public class MainForm extends javax.swing.JPanel {
    
    // --- Các biến quản lý thông báo và phiên làm việc ---
    private javax.swing.Timer notificationTimer;
    private int currentHoaDonID = -1; 
    private int currentKhachHangID = 100; // Giả sử khách ID 100 (Hà Thảo Dương) đang đăng nhập
    
    public MainForm() {
        initComponents();
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Kiểm tra xem khách này có hóa đơn nào đang chưa thanh toán trong DB không
        checkActiveSession();

        // 2. Chỉ chạy Timer thông báo Bếp NẾU khách này thực sự đang có Hóa đơn dở dang
        if (currentHoaDonID != -1) {
            startOrderTracking();
        }
    }

    // Thay đổi giữa các form subMenu theo Menu được chọn
    public void showForm(Component form){
        removeAll();
        add(form);
        repaint();
        revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


    // =========================================================================
    // XỬ LÝ DATABASE: KIỂM TRA PHIÊN & THÔNG BÁO TỪ BẾP
    // =========================================================================

    /**
     * Khởi tạo và chạy Timer ngầm để theo dõi tiến độ món ăn.
     * Cứ mỗi 5 giây, Timer sẽ gọi hàm checkForCompletedDishes() để kiểm tra xem
     * có món nào được bếp nấu xong chưa.
     */
    private void startOrderTracking() {
        notificationTimer = new javax.swing.Timer(5000, e -> checkForCompletedDishes());
        notificationTimer.setRepeats(true);
        notificationTimer.start();
    }

    /**
     * Kiểm tra trạng thái phiên làm việc hiện tại của khách hàng.
     * Truy vấn CSDL xem khách hàng này có hóa đơn nào đang "Chưa thanh toán" hay không.
     * Nếu có, lưu lại ID Hóa Đơn và ID Bàn để xử lý các nghiệp vụ gọi món tiếp theo.
     */
    private void checkActiveSession() {
        String sql = "SELECT ID_HoaDon, ID_Ban FROM HoaDon WHERE ID_KH = ? AND Trangthai = 'Chua thanh toan'";
        
        try {
            java.sql.Connection conn = com.restaurant.DatabaseConnection.getInstance().getConnection();
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setInt(1, currentKhachHangID);
            java.sql.ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                currentHoaDonID = rs.getInt("ID_HoaDon");
                int idBan = rs.getInt("ID_Ban");
                System.out.println("Đã tìm thấy hóa đơn chưa thanh toán: " + currentHoaDonID + " tại bàn " + idBan);
            } else {
                currentHoaDonID = -1;
                System.out.println("Khách chưa đặt bàn/chưa có hóa đơn.");
            }
            
            // Chỉ đóng ResultSet và PreparedStatement, KHÔNG đóng conn
            rs.close();
            pst.close();
            
        } catch (java.sql.SQLException ex) {
            System.out.println("Lỗi kiểm tra hóa đơn: " + ex.getMessage());
        }
    }

    /**
     * Lắng nghe và thông báo món ăn hoàn thành từ Bếp.
     * 1. Tìm các món thuộc Hóa đơn hiện tại có trạng thái "Hoan thanh" (do bếp bấm).
     * 2. Nếu có, lập tức UPDATE CSDL chuyển trạng thái thành "Da giao" để khóa dòng này lại (tránh thông báo lặp).
     * 3. Bật hộp thoại (JOptionPane) báo cho khách hàng biết món đã nấu xong.
     */
    private void checkForCompletedDishes() {
        String sqlCheck = "SELECT ID_MonAn, TenMon FROM KitchenOrders WHERE ID_HoaDon = ? AND TrangThai = 'Hoan thanh'";
        String sqlUpdate = "UPDATE KitchenOrders SET TrangThai = 'Da giao' WHERE ID_HoaDon = ? AND ID_MonAn = ?";

        try {
            java.sql.Connection conn = com.restaurant.DatabaseConnection.getInstance().getConnection();
            java.sql.PreparedStatement pstCheck = conn.prepareStatement(sqlCheck);
            
            pstCheck.setInt(1, currentHoaDonID);
            java.sql.ResultSet rs = pstCheck.executeQuery();

            while (rs.next()) {
                int idMon = rs.getInt("ID_MonAn");
                String tenMon = rs.getString("TenMon");

                // 1. CẬP NHẬT DATABASE TRƯỚC 
                // Khóa trạng thái món lại ngay lập tức để Timer lần sau không quét trúng nữa
                java.sql.PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate);
                pstUpdate.setInt(1, currentHoaDonID);
                pstUpdate.setInt(2, idMon);
                pstUpdate.executeUpdate();
                pstUpdate.close();

                // 2. BẬT THÔNG BÁO SAU
                javax.swing.JOptionPane.showMessageDialog(this,
                    "🎉 Món '" + tenMon + "' của bạn đã nấu xong và đang được mang ra!",
                    "Thông báo món ăn",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
            
            rs.close();
            pstCheck.close();
            
        } catch (java.sql.SQLException ex) {
            System.out.println("Lỗi kiểm tra thông báo món ăn: " + ex.getMessage());
        }
    }
}