package Restaurant.Model;

import java.time.LocalDateTime;

/**
 * Model ánh xạ với bảng KitchenOrders trong DB.
 * Lưu thông tin một món cần bếp chế biến.
 */
public class ModelKitchenOrder {

    private int id;           // ID_KO
    private int idHoaDon;     // ID_HoaDon
    private int idMonAn;      // ID_MonAn
    private String tenMon;    // TenMon
    private int soLuong;      // SoLuong
    private String trangThai; // 'Dang cho' | 'Dang lam' | 'Hoan thanh'
    private LocalDateTime thoiGianDat; // ThoiGianDat
    private String tenBan;    // TenBan (để bếp biết phục vụ bàn nào)

    // ─── Constructor mặc định ───────────────────────────────────────────────
    public ModelKitchenOrder() {}

    // ─── Constructor đầy đủ ─────────────────────────────────────────────────
    public ModelKitchenOrder(int id, int idHoaDon, int idMonAn,
                             String tenMon, int soLuong,
                             String trangThai, LocalDateTime thoiGianDat,
                             String tenBan) {
        this.id = id;
        this.idHoaDon = idHoaDon;
        this.idMonAn = idMonAn;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
        this.thoiGianDat = thoiGianDat;
        this.tenBan = tenBan;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(int idHoaDon) { this.idHoaDon = idHoaDon; }

    public int getIdMonAn() { return idMonAn; }
    public void setIdMonAn(int idMonAn) { this.idMonAn = idMonAn; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LocalDateTime getThoiGianDat() { return thoiGianDat; }
    public void setThoiGianDat(LocalDateTime thoiGianDat) { this.thoiGianDat = thoiGianDat; }

    public String getTenBan() { return tenBan; }
    public void setTenBan(String tenBan) { this.tenBan = tenBan; }
}