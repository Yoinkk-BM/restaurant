package Restaurant.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.restaurant.DatabaseConnection;

import Restaurant.Model.ModelKitchenOrder;

/**
 * Service xử lý toàn bộ logic nghiệp vụ cho màn hình bếp.
 * Kết nối DB qua Singleton DatabaseConnection để dùng chung với các Service khác.
 */
public class ServiceKitchen {

    private final Connection con = DatabaseConnection.getInstance().getConnection();

    // =========================================================================
    // LẤY DANH SÁCH MÓN ĐANG CHỜ VÀ ĐANG LÀM (hiển thị lên bảng bếp)
    // =========================================================================
    /**
     * Lấy tất cả đơn có trạng thái 'Dang cho' hoặc 'Dang lam', sắp xếp theo
     * thời gian đặt tăng dần (món cũ nhất lên đầu).
     */
    public List<ModelKitchenOrder> getPendingOrders() throws SQLException {
        List<ModelKitchenOrder> list = new ArrayList<>();

        String sql = "SELECT ID_KO, ID_HoaDon, ID_MonAn, TenMon, SoLuong, " +
                     "       TrangThai, ThoiGianDat, TenBan " +
                     "FROM KitchenOrders " +
                     "WHERE TrangThai IN (N'Dang cho', N'Dang lam') " +
                     "ORDER BY ThoiGianDat ASC";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ModelKitchenOrder order = new ModelKitchenOrder();
                order.setId(rs.getInt("ID_KO"));
                order.setIdHoaDon(rs.getInt("ID_HoaDon"));
                order.setIdMonAn(rs.getInt("ID_MonAn"));
                order.setTenMon(rs.getString("TenMon"));
                order.setSoLuong(rs.getInt("SoLuong"));
                order.setTrangThai(rs.getString("TrangThai"));
                // Chuyển Timestamp sang LocalDateTime
                if (rs.getTimestamp("ThoiGianDat") != null) {
                    order.setThoiGianDat(rs.getTimestamp("ThoiGianDat").toLocalDateTime());
                }
                order.setTenBan(rs.getString("TenBan"));
                list.add(order);
            }
        }
        return list;
    }

    // =========================================================================
    // CẬP NHẬT TRẠNG THÁI MÓN (đầu bếp bấm nút)
    // =========================================================================
    /**
     * Chuyển trạng thái của một món theo ID_KO.
     * Trạng thái hợp lệ: 'Dang cho' → 'Dang lam' → 'Hoan thanh'
     *
     * @param idKO     ID của dòng trong bảng KitchenOrders
     * @param trangThai trạng thái mới ('Dang lam' hoặc 'Hoan thanh')
     */
    public boolean updateStatus(int idKO, String trangThai) throws SQLException {
        String sql = "UPDATE KitchenOrders SET TrangThai = ? WHERE ID_KO = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, idKO);
            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================================
    // INSERT THỦ CÔNG (gọi từ code nếu không dùng Trigger)
    // Trigger Tg_CTHD_SyncKitchen đã tự động làm việc này khi insert CTHD,
    // nhưng để an toàn bạn có thể gọi hàm này từ ServiceOrder nếu cần.
    // =========================================================================
    /**
     * Thêm một món vào hàng đợi bếp.
     * Thường được gọi tự động bởi Trigger; chỉ dùng nếu không có Trigger.
     */
    public void insertKitchenOrder(int idHoaDon, int idMonAn,
                                   String tenMon, int soLuong,
                                   String tenBan) throws SQLException {
        // Kiểm tra tránh duplicate
        String checkSql = "SELECT COUNT(*) FROM KitchenOrders " +
                          "WHERE ID_HoaDon = ? AND ID_MonAn = ?";
        try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
            checkPs.setInt(1, idHoaDon);
            checkPs.setInt(2, idMonAn);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return; // Đã tồn tại, không insert lại
            }
        }

        String sql = "INSERT INTO KitchenOrders " +
                     "(ID_HoaDon, ID_MonAn, TenMon, SoLuong, TrangThai, ThoiGianDat, TenBan) " +
                     "VALUES (?, ?, ?, ?, N'Dang cho', GETDATE(), ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHoaDon);
            ps.setInt(2, idMonAn);
            ps.setString(3, tenMon);
            ps.setInt(4, soLuong);
            ps.setString(5, tenBan);
            ps.executeUpdate();
        }
    }

    // =========================================================================
    // ĐẾM SỐ MÓN ĐANG CHỜ (dùng để hiển thị badge thông báo nếu cần)
    // =========================================================================
    public int countPendingOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM KitchenOrders WHERE TrangThai = N'Dang cho'";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}