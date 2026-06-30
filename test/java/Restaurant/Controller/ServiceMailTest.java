package Restaurant.Controller;

import Restaurant.Model.ModelMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test cho ServiceMail.
 *
 * LƯU Ý: ServiceMail.sendMain() gọi thẳng đến server SMTP của Gmail nên không
 * thể mock với JDBC. Test này được đánh dấu @Disabled mặc định để tránh fail
 * khi chạy trên CI/CD. Bỏ @Disabled khi cần test thực tế gửi mail.
 */
@DisplayName("ServiceMail - Unit Test")
class ServiceMailTest {

    @Test
    @Disabled("Test gửi mail thật - cần network + SMTP, chạy thủ công khi cần")
    @DisplayName("TC_MAIL_01: sendMain() - gửi email với verify code")
    void testSendMain_WithValidEmail_ReturnsSuccessMessage() {
        ServiceMail service = new ServiceMail();
        ModelMessage result = service.sendMain("test@gmail.com", "123456");

        assertNotNull(result, "Phải trả về ModelMessage");
        assertTrue(result.isSuccess(), "Phải success khi gửi mail thành công");
    }

    @Test
    @Disabled("Test gửi mail với email sai format - chạy thủ công")
    @DisplayName("TC_MAIL_02: sendMain() - email không hợp lệ -> message thất bại")
    void testSendMain_InvalidEmail_ReturnsFailureMessage() {
        ServiceMail service = new ServiceMail();
        ModelMessage result = service.sendMain("invalid_email", "123456");

        assertNotNull(result);
        assertFalse(result.isSuccess());
    }
}
