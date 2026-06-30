package Restaurant.Controller;

import com.restaurant.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Base class cho tất cả các Service test.
 * Cung cấp các mock JDBC objects (Connection, PreparedStatement, ResultSet)
 * và tự động inject mock Connection vào DatabaseConnection singleton trước mỗi test.
 *
 * Cách dùng:
 *   class MyServiceTest extends BaseServiceTest {
 *       @Test
 *       void testSomething() throws SQLException {
 *           when(mockRs.next()).thenReturn(true);
 *           // ...
 *       }
 *   }
 */
public abstract class BaseServiceTest {

    protected Connection mockConn;
    protected PreparedStatement mockPstmt;
    protected ResultSet mockRs;
    protected Connection originalConnection;

    @BeforeEach
    void setUpMockDb() throws SQLException {
        // Tạo các mock JDBC objects
        mockConn = mock(Connection.class);
        mockPstmt = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        // Cấu hình mặc định: mọi prepareStatement đều trả về cùng PreparedStatement mock
        when(mockConn.prepareStatement(anyString())).thenReturn(mockPstmt);
        when(mockPstmt.executeQuery()).thenReturn(mockRs);

        // Lưu connection thật (nếu có) để khôi phục sau test
        originalConnection = DatabaseConnection.getInstance().getConnection();
        // Inject mock vào singleton
        DatabaseConnection.getInstance().setConnection(mockConn);
    }

    @AfterEach
    void tearDownMockDb() {
        // Khôi phục connection gốc (tránh ảnh hưởng các test khác)
        DatabaseConnection.getInstance().setConnection(originalConnection);
    }
}
