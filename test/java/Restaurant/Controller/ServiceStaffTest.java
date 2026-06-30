package Restaurant.Controller;

import Restaurant.Model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test cho ServiceStaff (nhân viên phục vụ + kho)
 */
@DisplayName("ServiceStaff - Unit Test")
class ServiceStaffTest extends BaseServiceTest {

    private ServiceStaff service;

    @org.junit.jupiter.api.BeforeEach
    void initService() {
        service = new ServiceStaff();
    }

    // ============================================================
    //  Test: getStaff() - lấy thông tin nhân viên theo userID
    // ============================================================
    @Test
    @DisplayName("TC_STAFF_01: getStaff() - userID hợp lệ -> trả về ModelNhanVien")
    void testGetStaff_ValidUserID_ReturnsStaff() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("ID_NV")).thenReturn(100);
        when(mockRs.getString("TenNV")).thenReturn("Nguyen Hoang Viet");
        when(mockRs.getString("Chucvu")).thenReturn("Quan ly");

        ModelNhanVien result = service.getStaff(100);

        assertNotNull(result);
        assertEquals(100, result.getID_NV());
        assertEquals("Quan ly", result.getChucvu());
    }

    @Test
    @DisplayName("TC_STAFF_02: getStaff() - userID không tồn tại -> null")
    void testGetStaff_InvalidUserID_ReturnsNull() throws SQLException {
        when(mockRs.next()).thenReturn(false);

        ModelNhanVien result = service.getStaff(99999);

        assertNull(result);
    }

    // ============================================================
    //  Test: MenuNL() - lấy danh sách nguyên liệu
    // ============================================================
    @Test
    @DisplayName("TC_STAFF_03: MenuNL() - có nguyên liệu -> trả danh sách")
    void testMenuNL_HasIngredients_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("ID_NL")).thenReturn(1, 2);
        when(mockRs.getString("TenNL")).thenReturn("Thit cuu", "Khoai tay");
        when(mockRs.getDouble("Dongia")).thenReturn(450000.0, 25000.0);
        when(mockRs.getString("Donvitinh")).thenReturn("kg", "kg");

        ArrayList<ModelNguyenLieu> result = service.MenuNL();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ============================================================
    //  Test: getNextID_NL() - lấy ID nguyên liệu tiếp theo
    // ============================================================
    @Test
    @DisplayName("TC_STAFF_04: getNextID_NL() - MAX(ID)=50 -> trả 51")
    void testGetNextID_NL_ReturnsMaxPlusOne() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("ID")).thenReturn(50);

        int result = service.getNextID_NL();

        assertEquals(51, result);
    }

    // ============================================================
    //  Test: setTableReserve() - đặt bàn
    // ============================================================
    @Test
    @DisplayName("TC_STAFF_05: setTableReserve() - đổi trạng thái bàn thành Da dat truoc")
    void testSetTableReserve_UpdatesTableStatus() throws SQLException {
        service.setTableReserve(101);

        verify(mockPstmt).setInt(1, 101);
        verify(mockPstmt).execute();
    }

    @Test
    @DisplayName("TC_STAFF_06: CancelTableReserve() - hủy đặt bàn")
    void testCancelTableReserve_RestoresTableStatus() throws SQLException {
        service.CancelTableReserve(101);

        verify(mockPstmt).setInt(1, 101);
        verify(mockPstmt).execute();
    }

    // ============================================================
    //  Test: InsertNL() - thêm nguyên liệu
    // ============================================================
    @Test
    @DisplayName("TC_STAFF_07: InsertNL() - thêm nguyên liệu hợp lệ")
    void testInsertNL_ValidData_ExecutesInsert() throws SQLException {
        ModelNguyenLieu nl = new ModelNguyenLieu();
        nl.setID_NL(120);
        nl.setTenNL("Cà chua");
        nl.setDongia(15000.0);
        nl.setDonvitinh("kg");

        service.InsertNL(nl);

        verify(mockPstmt).execute();
    }

    // ============================================================
    //  Test: getSLNL_TonKho() - đếm số loại nguyên liệu trong kho
    // ============================================================
    @Test
    @DisplayName("TC_STAFF_08: getSLNL_TonKho() - trả về số lượng loại NL trong kho")
    void testGetSLNL_TonKho_ReturnsCount() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(45);

        int result = service.getSLNL_TonKho();

        assertEquals(45, result);
    }
}
