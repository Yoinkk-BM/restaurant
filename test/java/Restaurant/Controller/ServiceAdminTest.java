package Restaurant.Controller;

import Restaurant.Model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test cho ServiceAdmin
 * (Quản lý nhân viên, món ăn, doanh thu, báo cáo)
 */
@DisplayName("ServiceAdmin - Unit Test")
class ServiceAdminTest extends BaseServiceTest {

    private ServiceAdmin service;

    @org.junit.jupiter.api.BeforeEach
    void initService() {
        service = new ServiceAdmin();
    }

    // ============================================================
    //  Test: getListNV() - lấy danh sách nhân viên
    // ============================================================
    @Test
    @DisplayName("TC_ADMIN_01: getListNV() - có nhân viên -> trả danh sách")
    void testGetListNV_HasStaff_ReturnsList() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("ID_NV")).thenReturn(100, 101);
        when(mockRs.getString("TenNV")).thenReturn("Nguyen A", "Tran B");
        when(mockRs.getString("Chucvu")).thenReturn("Quan ly", "Tiep tan");
        when(mockRs.getString("SDT")).thenReturn("0901234567", "0901234568");
        when(mockRs.getString("Tinhtrang")).thenReturn("Dang lam viec", "Dang lam viec");

        ArrayList<ModelNhanVien> result = service.getListNV();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ============================================================
    //  Test: getNextID_NV() - lấy ID nhân viên tiếp theo
    // ============================================================
    @Test
    @DisplayName("TC_ADMIN_02: getNextID_NV() - DB có MAX(ID)=109 -> trả về 110")
    void testGetNextIDNV_ReturnsMaxPlusOne() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("ID")).thenReturn(109);

        int result = service.getNextID_NV();

        assertEquals(110, result);
    }

    // ============================================================
    //  Test: FireStaff() - cho nhân viên nghỉ việc
    // ============================================================
    @Test
    @DisplayName("TC_ADMIN_03: FireStaff() - cập nhật trạng thái thành Da nghi viec")
    void testFireStaff_UpdatesTinhTrang() throws SQLException {
        service.FireStaff(105);

        verify(mockPstmt).setInt(1, 105);
        verify(mockPstmt).execute();
    }

    // ============================================================
    //  Test: getRevenueHD() - tính doanh thu
    // ============================================================
    @Test
    @DisplayName("TC_ADMIN_04: getRevenueHD() - filter tháng này -> trả tổng doanh thu")
    void testGetRevenueHD_ThisMonth_ReturnsSum() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("Tongtien")).thenReturn(12500000);

        int result = service.getRevenueHD("month");

        assertEquals(12500000, result);
    }

    @Test
    @DisplayName("TC_ADMIN_05: getRevenueHD() - không có HD -> trả 0")
    void testGetRevenueHD_NoData_ReturnsZero() throws SQLException {
        when(mockRs.next()).thenReturn(false);

        int result = service.getRevenueHD("day");

        assertEquals(0, result);
    }

    // ============================================================
    //  Test: getMenuFood() - lấy menu
    // ============================================================
    @Test
    @DisplayName("TC_ADMIN_06: getMenuFood() - trả về toàn bộ menu")
    void testGetMenuFood_ReturnsAllDishes() throws SQLException {
        when(mockRs.next()).thenReturn(true, true, true, false);
        when(mockRs.getInt("ID_MonAn")).thenReturn(1, 2, 3);
        when(mockRs.getString("TenMon")).thenReturn("Phở", "Bún", "Cơm");
        when(mockRs.getDouble("Dongia")).thenReturn(50000.0, 40000.0, 35000.0);
        when(mockRs.getString("Loai")).thenReturn("Aries", "Taurus", "Gemini");
        when(mockRs.getString("TrangThai")).thenReturn("Dang kinh doanh", "Dang kinh doanh", "Ngung kinh doanh");

        ArrayList<ModelMonAn> result = service.getMenuFood();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    // ============================================================
    //  Test: StopSell() / BackSell() - dừng/khôi phục bán
    // ============================================================
    @Test
    @DisplayName("TC_ADMIN_07: StopSell() - dừng bán món với ID đúng")
    void testStopSell_CallsUpdateWithCorrectID() throws SQLException {
        service.StopSell(15);

        verify(mockPstmt).setInt(1, 15);
        verify(mockPstmt).execute();
    }

    @Test
    @DisplayName("TC_ADMIN_08: BackSell() - khôi phục bán món")
    void testBackSell_CallsUpdateWithCorrectID() throws SQLException {
        service.BackSell(15);

        verify(mockPstmt).setInt(1, 15);
        verify(mockPstmt).execute();
    }

    // ============================================================
    //  Test: insertNV() - thêm nhân viên mới
    // ============================================================
    @Test
    @DisplayName("TC_ADMIN_09: insertNV() - thêm nhân viên với đầy đủ thông tin")
    void testInsertNV_ValidData_ExecutesInsert() throws SQLException {
        ModelNhanVien nv = new ModelNhanVien();
        nv.setID_NV(120);
        nv.setTenNV("Nguyen Test");
        nv.setSDT("0901111111");
        nv.setChucvu("Phuc vu");
        nv.setTinhtrang("Dang lam viec");

        service.insertNV(nv);

        verify(mockPstmt).execute();
    }
}
