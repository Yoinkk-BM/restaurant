package Restaurant.Controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test cho PosController (UC_08 Process Payment, UC_09 View Bill, UC_18 Print Receipt)
 */
@DisplayName("PosController - Unit Test")
class PosControllerTest extends BaseServiceTest {

    private PosController controller;

    @org.junit.jupiter.api.BeforeEach
    void initController() {
        controller = new PosController();
    }

    // ============================================================
    //  Test: layDanhSachMonAn()
    // ============================================================
    @Test
    @DisplayName("TC_POS_01: layDanhSachMonAn() - trả về danh sách món đang KD")
    void testLayDanhSachMonAn_HasFoods_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("ID_MonAn")).thenReturn(1, 2);
        when(mockRs.getString("TenMon")).thenReturn("Phở", "Bún");
        when(mockRs.getDouble("Dongia")).thenReturn(50000.0, 40000.0);

        List<Object[]> result = controller.layDanhSachMonAn();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ============================================================
    //  Test: layDanhSachBanTrong()
    // ============================================================
    @Test
    @DisplayName("TC_POS_02: layDanhSachBanTrong() - trả về bàn có trạng thái Con trong")
    void testLayDanhSachBanTrong_ReturnsEmptyTables() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, true, false);
        when(mockRs.getInt("ID_Ban")).thenReturn(101, 102, 103);
        when(mockRs.getString("TenBan")).thenReturn("Ban T1.1", "Ban T1.2", "Ban T1.3");

        List<Object[]> result = controller.layDanhSachBanTrong();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    // ============================================================
    //  Test: layDanhSachHoaDonChuaThanhToan() - UC_09
    // ============================================================
    @Test
    @DisplayName("TC_POS_03: layDanhSachHoaDonChuaThanhToan() - trả HD chưa thanh toán")
    void testLayDanhSachHoaDonChuaThanhToan_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("ID_HoaDon")).thenReturn(120, 121);
        when(mockRs.getString("TenBan")).thenReturn("Ban T1.1", "Ban T1.2");
        when(mockRs.getDouble("Tongtien")).thenReturn(450000.0, 320000.0);

        List<Object[]> result = controller.layDanhSachHoaDonChuaThanhToan();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ============================================================
    //  Test: layChiTietHoaDon() - UC_09
    // ============================================================
    @Test
    @DisplayName("TC_POS_04: layChiTietHoaDon() - trả về chi tiết hóa đơn")
    void testLayChiTietHoaDon_HasItems_ReturnsDetail() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getString("TenMon")).thenReturn("Phở", "Bún");
        when(mockRs.getInt("SoLuong")).thenReturn(2, 1);
        when(mockRs.getDouble("Dongia")).thenReturn(50000.0, 40000.0);
        when(mockRs.getDouble("Thanhtien")).thenReturn(100000.0, 40000.0);

        List<Object[]> result = controller.layChiTietHoaDon(120);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ============================================================
    //  Test: thanhToanHoaDon() - UC_08 Process Payment
    // ============================================================
    @Test
    @DisplayName("TC_POS_05: thanhToanHoaDon() - cập nhật trạng thái Da thanh toan")
    void testThanhToanHoaDon_UpdatesStatus_ReturnsTrue() throws SQLException {
        when(mockPstmt.executeUpdate()).thenReturn(1);

        boolean result = controller.thanhToanHoaDon(120);

        assertTrue(result);
        verify(mockPstmt).setInt(1, 120);
    }

    @Test
    @DisplayName("TC_POS_06: thanhToanHoaDon() - HD không tồn tại -> false")
    void testThanhToanHoaDon_NotExists_ReturnsFalse() throws SQLException {
        when(mockPstmt.executeUpdate()).thenReturn(0);

        boolean result = controller.thanhToanHoaDon(99999);

        assertFalse(result);
    }
}
