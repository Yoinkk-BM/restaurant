
package Restaurant.Controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.restaurant.DatabaseConnection;

import Restaurant.Model.ModelLogin;
import Restaurant.Model.ModelNguoiDung;

// Controller quản lý đăng ký, đăng nhập, xác minh tài khoản và đổi mật khẩu.
public class ServiceUser {
    
    private final Connection con;
    
    //Connect tới DataBase       
    public ServiceUser() {
        con=DatabaseConnection.getInstance().getConnection();
    }
    
    /*
        Xác thực thông tin đăng nhập của người dùng.
        Nếu email và mật khẩu khớp và tài khoản đã được xác minh, hàm sẽ trả về đối tượng người dùng.
        Nếu sai, hàm trả về null để giao diện biết cần hiển thị lỗi đăng nhập.
    */
    public ModelNguoiDung login(ModelLogin login) throws SQLException {
    ModelNguoiDung user = null;
    // Dùng TOP 1 thay vì FETCH FIRST
    String sql = "SELECT TOP 1 * FROM NguoiDung WHERE Email=? AND Matkhau=? AND Trangthai='Verified'";
    
    PreparedStatement p = con.prepareStatement(sql);
    p.setString(1, login.getEmail());
    p.setString(2, login.getPassword());
    
    ResultSet r = p.executeQuery();
    if (r.next()) {
        int UserID = r.getInt("ID_ND");
        String email = r.getString("Email");
        String password = r.getString("Matkhau");
        String role = r.getString("Vaitro");
        user = new ModelNguoiDung(UserID, email, password, role);
    }
    r.close();
    p.close();
    return user;
}
    /*
        Đăng ký tài khoản dành cho khách hàng.
        Sau khi đăng ký thành công, hệ thống sẽ tạo một bản ghi người dùng mới với role mặc định là khách hàng
        và sinh ra một mã xác minh để kích hoạt tài khoản.
    */
    public void insertUser(ModelNguoiDung user)throws SQLException{
        //Lấy ID của User tiếp theo 
        PreparedStatement p1=con.prepareStatement("SELECT MAX(ID_ND) as ID_ND FROM NguoiDung");
        ResultSet r= p1.executeQuery();
        r.next();
        int userID=r.getInt("ID_ND")+1;
        
        //Thêm Người Dùng
        String sql_ND = "INSERT INTO NguoiDung (ID_ND,Email, MatKhau, VerifyCode,Vaitro) VALUES (?,?, ?, ?,'Khach Hang')";
        PreparedStatement p=con.prepareStatement(sql_ND);
        String code=generateVerifiyCode();
        p.setInt(1, userID);
        p.setString(2, user.getEmail());
        p.setString(3, user.getPassword());
        p.setString(4, code);
        p.execute();
        
        r.close();
        p.close();
        p1.close();
        
        user.setUserID(userID);
        user.setVerifyCode(code);
        user.setRole("Khách Hàng");
    }
    
    // Tạo mã xác minh ngẫu nhiên để gửi cho người dùng xác thực tài khoản.
    // Mã này phải đảm bảo không bị trùng với các mã đã tồn tại trong hệ thống.
    public String generateVerifiyCode()throws SQLException{
        DecimalFormat df = new DecimalFormat("000000");
        Random ran = new Random();
        String code = df.format(ran.nextInt(1000000));  //  Random from 0 to 999999
        while (checkDuplicateCode(code)) {
            code = df.format(ran.nextInt(1000000));
        }
        return code;
    }
    
    // Kiểm tra xem mã xác minh vừa tạo đã tồn tại trong cơ sở dữ liệu hay chưa.
    // Nếu trùng thì tạo lại mã mới cho đến khi hợp lệ.
    private boolean checkDuplicateCode(String code) throws SQLException{
        boolean duplicate=false;
        String sql="SELECT * FROM NguoiDung WHERE VerifyCode=? FETCH FIRST 1 ROWS ONLY";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, code);
        ResultSet r=p.executeQuery();
        if(r.next()){
            duplicate=true;
        }
        r.close();
        p.close();
        return duplicate;
    }
    
    /*
        Kiểm tra email đã tồn tại trong hệ thống hay chưa.
        Nếu email đã được dùng bởi một tài khoản đã xác minh, hàm trả về true.
        Điều này giúp ngăn việc đăng ký trùng tài khoản.
    */
    public boolean checkDuplicateEmail(String email) throws SQLException{
        boolean duplicate=false;
        String sql="SELECT * FROM NguoiDung WHERE Email=? AND Trangthai='Verified' FETCH FIRST 1 ROWS ONLY";
        PreparedStatement p = con.prepareStatement(sql);
        p.setString(1, email);
        ResultSet r=p.executeQuery();
        if(r.next()){
            duplicate=true;
        }
        r.close();
        p.close();
        return duplicate;
    }
    /*
        Hoàn tất xác minh tài khoản sau khi người dùng nhập đúng mã xác minh.
        Quá trình này gồm hai bước:
        1. Cập nhật trạng thái tài khoản từ chưa xác minh sang đã xác minh.
        2. Tạo bản ghi khách hàng mới trong bảng KhachHang để liên kết với tài khoản.
    */
    public void doneVerify(int userID,String name) throws SQLException{
        //Cập nhật NguoiDung
        String sql_ND="UPDATE NguoiDung SET VerifyCode='', Trangthai='Verified' WHERE ID_ND=?";
        PreparedStatement p1 = con.prepareStatement(sql_ND);
        p1.setInt(1, userID);
        p1.execute();
        //Lấy id của Khách Hàng tiếp theo
        int id=0;
        String sql_ID="SELECT MAX(ID_KH) as ID FROM KhachHang";
        PreparedStatement p_id = con.prepareStatement(sql_ID);
        ResultSet r=p_id.executeQuery();
        if(r.next()){
            id=r.getInt("ID")+1;
        }
        
        //Thêm KH mới
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
        String sql_KH="INSERT INTO KhachHang (ID_KH,TenKH, Ngaythamgia,ID_ND) VALUES (?,?,to_date(?, 'dd-mm-yyyy'),?)";
        PreparedStatement p2=con.prepareStatement(sql_KH);
        p2.setInt(1, id);
        p2.setString(2, name);
        p2.setString(3, simpleDateFormat.format(new Date()));
        p2.setInt(4, userID);
        p2.execute();
        
        p1.close();
        p_id.close();
        p2.close();
    }
    
    /*
        So sánh mã xác minh người dùng nhập vào với mã đã lưu trong cơ sở dữ liệu.
        Nếu khớp, tài khoản được coi là đã xác minh thành công.
    */
    public boolean verifyCodeWithUser(int userID,String code) throws SQLException{
        boolean verify=false;
        String sql="SELECT COUNT(ID_ND) as CountID FROM NguoiDung WHERE ID_ND=? AND VerifyCode=?";
        PreparedStatement p = con.prepareStatement(sql);
        p.setInt(1, userID);
        p.setString(2,code);
        ResultSet r=p.executeQuery();
        if(r.next()){
            if(r.getInt("CountID")>0){
                verify=true;
            }
        }
        r.close();
        p.close();
        return verify;
    }
    // Thay đổi mật khẩu cho tài khoản người dùng.
    // Dùng khi người dùng thực hiện chức năng đổi mật khẩu từ giao diện.
    public void changePassword(int userID,String newPass) throws SQLException{
        String sql="UPDATE NguoiDung SET MatKhau = ? WHERE ID_ND = ?";
        PreparedStatement p=con.prepareStatement(sql);
        p.setString(1, newPass);
        p.setInt(2, userID);
        p.execute();
        p.close();
    }
}
