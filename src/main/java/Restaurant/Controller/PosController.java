package Restaurant.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.restaurant.DatabaseConnection; 

public class PosController {

   
    public List<Object[]> layDanhSachMonAn() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT ID_MonAn, TenMon, DonGia FROM MonAn WHERE TrangThai = 'Dang kinh doanh'";
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            if (con == null) return list; // Tránh lỗi NullPointerException
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("ID_MonAn"), rs.getString("TenMon"), rs.getDouble("DonGia")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> layDanhSachBanTrong() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT ID_Ban, TenBan FROM Ban WHERE Trangthai = 'Con trong'";
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            if (con == null) return list;
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("ID_Ban"), rs.getString("TenBan")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean luuHoaDon(int idBan, DefaultTableModel billModel) {
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            if (con == null) return false;
            con.setAutoCommit(false); 

            int newHoaDonID = 1;
            String sqlGetID = "SELECT ISNULL(MAX(ID_HoaDon), 0) + 1 AS NewID FROM HoaDon";
            PreparedStatement psID = con.prepareStatement(sqlGetID);
            ResultSet rsID = psID.executeQuery();
            if (rsID.next()) { newHoaDonID = rsID.getInt("NewID"); }

            String sqlHD = "INSERT INTO HoaDon (ID_HoaDon, ID_Ban, NgayHD, Trangthai) VALUES (?, ?, CURRENT_TIMESTAMP, 'Da thanh toan')";
            PreparedStatement psHD = con.prepareStatement(sqlHD);
            psHD.setInt(1, newHoaDonID);
            psHD.setInt(2, idBan);
            psHD.executeUpdate();

            String sqlCTHD = "INSERT INTO CTHD (ID_HoaDon, ID_MonAn, SoLuong) VALUES (?, ?, ?)";
            PreparedStatement psCTHD = con.prepareStatement(sqlCTHD);

            for (int i = 0; i < billModel.getRowCount(); i++) {
                int idMonAn = (int) billModel.getValueAt(i, 0); 
                int soLuong = (int) billModel.getValueAt(i, 2); 
                
                psCTHD.setInt(1, newHoaDonID);
                psCTHD.setInt(2, idMonAn);
                psCTHD.setInt(3, soLuong);
                psCTHD.addBatch();
            }
            psCTHD.executeBatch();
            con.commit(); 
            con.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    // PHẦN 2: CODE DÀNH RIÊNG CHO LUỒNG THU NGÂN (Lấy theo Bàn/Tầng)
    

    // Hàm lấy danh sách các Hóa đơn chưa thanh toán 
    public List<Object[]> layDanhSachHoaDonChuaThanhToan() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT h.ID_HoaDon, b.Vitri, b.TenBan, h.Tongtien " +
                     "FROM HoaDon h JOIN Ban b ON h.ID_Ban = b.ID_Ban " +
                     "WHERE h.Trangthai = N'Chua thanh toan'";
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            if (con == null) return list;
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("ID_HoaDon"), 
                    rs.getString("Vitri"), 
                    rs.getString("TenBan"), 
                    rs.getDouble("Tongtien")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Hàm lấy chi tiết các món ăn của 1 Hóa đơn cụ thể (Đổ vào Bảng bên phải)
    public List<Object[]> layChiTietHoaDon(int idHoaDon) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT m.TenMon, c.SoLuong, m.DonGia, c.Thanhtien " +
                     "FROM CTHD c JOIN MonAn m ON c.ID_MonAn = m.ID_MonAn " +
                     "WHERE c.ID_HoaDon = ?";
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            if (con == null) return list;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idHoaDon); 
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("TenMon"), 
                    rs.getInt("SoLuong"), 
                    rs.getDouble("DonGia"), 
                    rs.getDouble("Thanhtien")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    // Hàm cập nhật trạng thái hóa đơn thành Đã thanh toán
    public boolean thanhToanHoaDon(int idHoaDon) {
        String sql = "UPDATE HoaDon SET Trangthai = N'Da thanh toan' WHERE ID_HoaDon = ?";
        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            if (con == null) return false;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idHoaDon);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}