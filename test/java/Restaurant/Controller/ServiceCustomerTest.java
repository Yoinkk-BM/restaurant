package Restaurant.Controller;

import Restaurant.Model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test cho ServiceCustomer (UC_02 View Menu, UC_03 Place Order,
 * UC_04 View Current Bill, UC_11 Add Item, UC_01 Membership)
 */
@DisplayName("ServiceCustomer - Unit Test")
class ServiceCustomerTest extends BaseServiceTest {

    private ServiceCustomer service;

    @org.junit.jupiter.api.BeforeEach
    void initService() {
        service = new ServiceCustomer();
    }

    // ============================================================
    //  Test: MenuFood() - UC_02
    // ============================================================
    @Test
    @DisplayName("TC_CUS_01: MenuFood() - lấy menu theo loại Aries -> có data")
    void testMenuFood_TypeAries_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("ID_MonAn")).thenReturn(1, 2);
        when(mockRs.getString("TenMon")).thenReturn("DUI CUU NUONG", "BAP CUU NUONG");
        when(mockRs.getDouble("Dongia")).thenReturn(250000.0, 250000.0);
        when(mockRs.getString("Loai")).thenReturn("Aries", "Aries");
        when(mockRs.getString("TrangThai")).thenReturn("Dang kinh doanh", "Dang kinh doanh");

        ArrayList<ModelMonAn> result = service.MenuFood("Aries");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("TC_CUS_02: MenuFood() - loại không tồn tại -> list rỗng")
    void testMenuFood_InvalidType_ReturnsEmpty() throws SQLException {
        when(mockRs.next()).thenReturn(false);

        ArrayList<ModelMonAn> result = service.MenuFood("Invalid");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ============================================================
    //  Test: MenuTable() - UC_04 (xem bàn)
    // ============================================================
    @Test
    @DisplayName("TC_CUS_03: MenuTable() - lấy danh sách bàn tầng 1")
    void testMenuTable_Floor1_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, true, false);
        when(mockRs.getInt("ID_Ban")).thenReturn(101, 102, 103);
        when(mockRs.getString("TenBan")).thenReturn("Ban T1.1", "Ban T1.2", "Ban T1.3");
        when(mockRs.getString("Vitri")).thenReturn("Tang 1", "Tang 1", "Tang 1");
        when(mockRs.getString("Trangthai")).thenReturn("Con trong", "Dang dung bua", "Con trong");

        ArrayList<ModelBan> result = service.MenuTable("Tang 1");

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    // ============================================================
    //  Test: getCustomer() - UC_04 (xem hồ sơ khách)
    // ============================================================
    @Test
    @DisplayName("TC_CUS_04: getCustomer() - khách hàng tồn tại -> trả về ModelKhachHang")
    void testGetCustomer_Exists_ReturnsCustomer() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("ID_KH")).thenReturn(100);
        when(mockRs.getString("TenKH")).thenReturn("Nguyen Van A");
        when(mockRs.getDouble("Doanhso")).thenReturn(1500000.0);
        when(mockRs.getInt("Diemtichluy")).thenReturn(75);

        ModelKhachHang result = service.getCustomer(104);

        assertNotNull(result);
        assertEquals(100, result.getID_KH());
        assertEquals("Nguyen Van A", result.getTenKH());
        assertEquals(75, result.getDiemtichluy());
    }

    @Test
    @DisplayName("TC_CUS_05: getCustomer() - userID không tồn tại -> trả null")
    void testGetCustomer_NotExists_ReturnsNull() throws SQLException {
        when(mockRs.next()).thenReturn(false);

        ModelKhachHang result = service.getCustomer(99999);

        assertNull(result);
    }

    // ============================================================
    //  Test: MenuVoucher() - xem danh sách voucher
    // ============================================================
    @Test
    @DisplayName("TC_CUS_06: MenuVoucher() - có voucher -> trả danh sách")
    void testMenuVoucher_HasVouchers_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getString("Code_Voucher")).thenReturn("VC001", "VC002");
        when(mockRs.getString("Mota")).thenReturn("Giảm 10%", "Giảm 20%");
        when(mockRs.getInt("Phantram")).thenReturn(10, 20);
        when(mockRs.getString("LoaiMA")).thenReturn("All", "Aries");
        when(mockRs.getInt("SoLuong")).thenReturn(100, 50);
        when(mockRs.getInt("Diem")).thenReturn(100, 200);

        ArrayList<ModelVoucher> result = service.MenuVoucher();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ============================================================
    //  Test: getCTHD() - xem chi tiết hóa đơn (UC_04)
    // ============================================================
    @Test
    @DisplayName("TC_CUS_07: getCTHD() - hóa đơn có 3 món -> trả 3 dòng CTHD")
    void testGetCTHD_HasItems_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, true, false);
        when(mockRs.getInt("ID_HoaDon")).thenReturn(100, 100, 100);
        when(mockRs.getInt("ID_MonAn")).thenReturn(1, 2, 3);
        when(mockRs.getInt("SoLuong")).thenReturn(2, 1, 3);
        when(mockRs.getDouble("Thanhtien")).thenReturn(500000.0, 250000.0, 150000.0);

        ArrayList<ModelCTHD> result = service.getCTHD(100);

        assertNotNull(result);
        assertEquals(3, result.size());
    }
}
