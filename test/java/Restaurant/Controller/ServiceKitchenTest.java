package Restaurant.Controller;

import Restaurant.Model.ModelKitchenOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test cho ServiceKitchen (UC_07 View Pending, UC_05 Update Status)
 *  - getPendingOrders()
 *  - updateStatus(int, String)
 *  - countPendingOrders()
 */
@DisplayName("ServiceKitchen - Unit Test")
class ServiceKitchenTest extends BaseServiceTest {

    private ServiceKitchen service;

    @org.junit.jupiter.api.BeforeEach
    void initService() {
        service = new ServiceKitchen();
    }

    // ============================================================
    //  Test: getPendingOrders()
    // ============================================================
    @Test
    @DisplayName("TC_KITCHEN_01: getPendingOrders() - có order đang chờ -> trả danh sách")
    void testGetPendingOrders_HasOrders_ReturnsList() throws SQLException {
        // Arrange: giả lập 2 dòng kết quả
        when(mockRs.next()).thenReturn(true, true, false);
        when(mockRs.getInt("ID_KO")).thenReturn(1, 2);
        when(mockRs.getInt("ID_HoaDon")).thenReturn(100, 101);
        when(mockRs.getInt("ID_MonAn")).thenReturn(5, 6);
        when(mockRs.getString("TenMon")).thenReturn("Phở bò", "Bún chả");
        when(mockRs.getInt("SoLuong")).thenReturn(2, 1);
        when(mockRs.getString("TrangThai")).thenReturn("Dang cho", "Dang cho");
        when(mockRs.getString("TenBan")).thenReturn("Ban 1", "Ban 2");

        // Act
        List<ModelKitchenOrder> result = service.getPendingOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "Phải có 2 order");
    }

    @Test
    @DisplayName("TC_KITCHEN_02: getPendingOrders() - không có order -> trả list rỗng")
    void testGetPendingOrders_NoOrders_ReturnsEmptyList() throws SQLException {
        when(mockRs.next()).thenReturn(false);

        List<ModelKitchenOrder> result = service.getPendingOrders();

        assertNotNull(result);
        assertEquals(0, result.size(), "Phải trả về list rỗng khi không có order");
    }

    // ============================================================
    //  Test: updateStatus()
    // ============================================================
    @Test
    @DisplayName("TC_KITCHEN_03: updateStatus() - cập nhật trạng thái thành công")
    void testUpdateStatus_Valid_ReturnsTrue() throws SQLException {
        when(mockPstmt.executeUpdate()).thenReturn(1);  // 1 row affected

        boolean result = service.updateStatus(10, "Dang lam");

        assertTrue(result, "Phải trả về true khi update 1 row thành công");
        verify(mockPstmt).setString(eq(1), eq("Dang lam"));
        verify(mockPstmt).setInt(eq(2), eq(10));
    }

    @Test
    @DisplayName("TC_KITCHEN_04: updateStatus() - trạng thái không hợp lệ -> false")
    void testUpdateStatus_NoRowAffected_ReturnsFalse() throws SQLException {
        when(mockPstmt.executeUpdate()).thenReturn(0); // 0 rows affected

        boolean result = service.updateStatus(999, "Hoan thanh");

        assertFalse(result, "Phải trả về false khi không có row nào được update");
    }

    // ============================================================
    //  Test: countPendingOrders()
    // ============================================================
    @Test
    @DisplayName("TC_KITCHEN_05: countPendingOrders() - trả về số order đang chờ")
    void testCountPendingOrders_ReturnsCount() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(5);

        int result = service.countPendingOrders();

        assertEquals(5, result);
    }

    @Test
    @DisplayName("TC_KITCHEN_06: countPendingOrders() - không có order -> trả 0")
    void testCountPendingOrders_NoOrders_ReturnsZero() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt(1)).thenReturn(0);

        int result = service.countPendingOrders();

        assertEquals(0, result);
    }
}
