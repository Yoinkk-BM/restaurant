package Restaurant.Controller;

import Restaurant.Model.ModelLogin;
import Restaurant.Model.ModelNguoiDung;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit test cho ServiceUser
 *  - login()
 *  - insertUser()
 *  - generateVerifiyCode()
 *  - checkDuplicateEmail()
 *  - doneVerify()
 *  - verifyCodeWithUser()
 *  - changePassword()
 */
@DisplayName("ServiceUser - Unit Test")
class ServiceUserTest extends BaseServiceTest {

    private ServiceUser service;

    @org.junit.jupiter.api.BeforeEach
    void initService() {
        service = new ServiceUser();
    }

    // ============================================================
    //  Test: login()
    // ============================================================
    @Test
    @DisplayName("TC_USER_01: login() - thông tin đúng -> trả về ModelNguoiDung")
    void testLogin_ValidCredentials_ReturnsUser() throws SQLException {
        // Arrange
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("ID_ND")).thenReturn(100);
        when(mockRs.getString("Email")).thenReturn("test@gmail.com");
        when(mockRs.getString("Matkhau")).thenReturn("123");
        when(mockRs.getString("Vaitro")).thenReturn("Quan Ly");

        ModelLogin login = new ModelLogin("test@gmail.com", "123");

        // Act
        ModelNguoiDung result = service.login(login);

        // Assert
        assertNotNull(result, "Phải trả về user khi đăng nhập đúng");
        assertEquals(100, result.getUserID());
        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("Quan Ly", result.getRole());
        verify(mockPstmt).setString(1, "test@gmail.com");
        verify(mockPstmt).setString(2, "123");
    }

    @Test
    @DisplayName("TC_USER_02: login() - thông tin sai -> trả về null")
    void testLogin_InvalidCredentials_ReturnsNull() throws SQLException {
        // Arrange: ResultSet không có dòng nào
        when(mockRs.next()).thenReturn(false);
        ModelLogin login = new ModelLogin("wrong@gmail.com", "wrong");

        // Act
        ModelNguoiDung result = service.login(login);

        // Assert
        assertNull(result, "Phải trả về null khi sai thông tin");
    }

    @Test
    @DisplayName("TC_USER_03: login() - email rỗng -> trả về null")
    void testLogin_EmptyEmail_ReturnsNull() throws SQLException {
        when(mockRs.next()).thenReturn(false);
        ModelLogin login = new ModelLogin("", "123");
        ModelNguoiDung result = service.login(login);
        assertNull(result);
    }

    // ============================================================
    //  Test: generateVerifiyCode()
    // ============================================================
    @Test
    @DisplayName("TC_USER_04: generateVerifiyCode() - sinh mã 6 ký tự số")
    void testGenerateVerifyCode_Returns6DigitCode() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        // Giả lập không có mã trùng (CountID = 0 -> ResultSet trống ngay từ đầu)
        // Vì checkDuplicateCode private, ta giả lập ResultSet không trả mã trùng
        when(mockRs.next()).thenReturn(false); // không trùng

        String code = service.generateVerifiyCode();

        assertNotNull(code);
        assertEquals(6, code.length(), "Mã verify phải có 6 ký tự");
        assertTrue(code.matches("\\d{6}"), "Mã verify phải là 6 chữ số");
    }

    // ============================================================
    //  Test: checkDuplicateEmail()
    // ============================================================
    @Test
    @DisplayName("TC_USER_05: checkDuplicateEmail() - email đã tồn tại -> true")
    void testCheckDuplicateEmail_Exists_ReturnsTrue() throws SQLException {
        when(mockRs.next()).thenReturn(true);

        boolean result = service.checkDuplicateEmail("existing@gmail.com");

        assertTrue(result, "Phải trả về true nếu email đã có trong DB");
        verify(mockPstmt).setString(1, "existing@gmail.com");
    }

    @Test
    @DisplayName("TC_USER_06: checkDuplicateEmail() - email chưa tồn tại -> false")
    void testCheckDuplicateEmail_NotExists_ReturnsFalse() throws SQLException {
        when(mockRs.next()).thenReturn(false);

        boolean result = service.checkDuplicateEmail("new@gmail.com");

        assertFalse(result, "Phải trả về false nếu email chưa có trong DB");
    }

    // ============================================================
    //  Test: verifyCodeWithUser()
    // ============================================================
    @Test
    @DisplayName("TC_USER_07: verifyCodeWithUser() - mã đúng -> true")
    void testVerifyCodeWithUser_Correct_ReturnsTrue() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("CountID")).thenReturn(1);

        boolean result = service.verifyCodeWithUser(100, "123456");

        assertTrue(result);
        verify(mockPstmt).setInt(1, 100);
        verify(mockPstmt).setString(2, "123456");
    }

    @Test
    @DisplayName("TC_USER_08: verifyCodeWithUser() - mã sai -> false")
    void testVerifyCodeWithUser_Wrong_ReturnsFalse() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("CountID")).thenReturn(0);

        boolean result = service.verifyCodeWithUser(100, "000000");

        assertFalse(result);
    }

    // ============================================================
    //  Test: changePassword()
    // ============================================================
    @Test
    @DisplayName("TC_USER_09: changePassword() - gọi UPDATE đúng tham số")
    void testChangePassword_CallsUpdateWithCorrectParams() throws SQLException {
        service.changePassword(100, "newPassword123");

        verify(mockPstmt).setString(1, "newPassword123");
        verify(mockPstmt).setInt(2, 100);
        verify(mockPstmt).execute();
    }

    // ============================================================
    //  Test: insertUser()
    // ============================================================
    @Test
    @DisplayName("TC_USER_10: insertUser() - thêm user mới với vai trò Khach Hang")
    void testInsertUser_NewUser_AssignsCustomerRole() throws SQLException {
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("ID_ND")).thenReturn(115);

        ModelNguoiDung newUser = new ModelNguoiDung();
        newUser.setEmail("new@gmail.com");
        newUser.setPassword("123");

        service.insertUser(newUser);

        // Kiểm tra ID được set bằng MAX + 1 = 116
        assertEquals(116, newUser.getUserID());
        // Mã verify được sinh
        assertNotNull(newUser.getVerifyCode());
        assertEquals(6, newUser.getVerifyCode().length());
        // Vai trò mặc định
        assertEquals("Khách Hàng", newUser.getRole());
    }
}
