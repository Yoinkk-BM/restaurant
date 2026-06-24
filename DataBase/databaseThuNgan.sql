-- ==========================================
-- 1. KHỞI TẠO DATABASE
-- ==========================================
CREATE DATABASE QuanLyNhaHang;
GO
USE QuanLyNhaHang;
GO

-- ==========================================
-- 2. NHÓM BẢNG HỆ THỐNG (NGƯỜI DÙNG, NHÂN VIÊN, KHÁCH HÀNG)
-- ==========================================
CREATE TABLE NguoiDung(
    ID_ND INT PRIMARY KEY,
    Email NVARCHAR(50),
    Matkhau NVARCHAR(20),
    VerifyCode NVARCHAR(10) DEFAULT NULL,
    Trangthai NVARCHAR(10) DEFAULT '',
    Vaitro NVARCHAR(20)
);

CREATE TABLE NhanVien(
    ID_NV INT PRIMARY KEY,
    TenNV NVARCHAR(50),
    NgayVL DATE,
    SDT NVARCHAR(50),
    Chucvu NVARCHAR(50),
    ID_ND INT NULL,
    ID_NQL INT NULL,
    Tinhtrang NVARCHAR(20),
    FOREIGN KEY (ID_ND) REFERENCES NguoiDung(ID_ND),
    FOREIGN KEY (ID_NQL) REFERENCES NhanVien(ID_NV)
);

CREATE TABLE KhachHang(
    ID_KH INT PRIMARY KEY,
    TenKH NVARCHAR(50), 
    Ngaythamgia DATE, 
    Doanhso FLOAT DEFAULT 0, 
    Diemtichluy INT DEFAULT 0,
    ID_ND INT NULL,
    FOREIGN KEY (ID_ND) REFERENCES NguoiDung(ID_ND)
);

-- ==========================================
-- 3. NHÓM BẢNG NGHIỆP VỤ NHÀ HÀNG (MÓN ĂN, BÀN, VOUCHER)
-- ==========================================
CREATE TABLE MonAn(
    ID_MonAn INT PRIMARY KEY,
    TenMon NVARCHAR(50), 
    DonGia FLOAT,
    Loai NVARCHAR(50),
    TrangThai NVARCHAR(30)
);

CREATE TABLE Ban(
    ID_Ban INT PRIMARY KEY,
    TenBan NVARCHAR(50), 
    Vitri NVARCHAR(50), 
    Trangthai NVARCHAR(50)
);

CREATE TABLE Voucher(
    Code_Voucher NVARCHAR(10) PRIMARY KEY,
    Mota NVARCHAR(50),
    Phantram INT,
    LoaiMA NVARCHAR(50),
    SoLuong INT,
    Diem INT
);

-- ==========================================
-- 4. NHÓM BẢNG QUẢN LÝ ĐƠN HÀNG (HÓA ĐƠN)
-- ==========================================
CREATE TABLE HoaDon(
    ID_HoaDon INT PRIMARY KEY,
    ID_KH INT NULL,
    ID_Ban INT NULL,
    NgayHD DATETIME,
    TienMonAn FLOAT DEFAULT 0,
    Code_Voucher NVARCHAR(10) NULL,
    TienGiam FLOAT DEFAULT 0,
    Tongtien FLOAT DEFAULT 0,
    Trangthai NVARCHAR(50),
    FOREIGN KEY (ID_KH) REFERENCES KhachHang(ID_KH),
    FOREIGN KEY (ID_Ban) REFERENCES Ban(ID_Ban),
    FOREIGN KEY (Code_Voucher) REFERENCES Voucher(Code_Voucher)
);

CREATE TABLE CTHD(
    ID_HoaDon INT,
    ID_MonAn INT,
    SoLuong INT,
    Thanhtien FLOAT DEFAULT 0,
    PRIMARY KEY (ID_HoaDon, ID_MonAn),
    FOREIGN KEY (ID_HoaDon) REFERENCES HoaDon(ID_HoaDon),
    FOREIGN KEY (ID_MonAn) REFERENCES MonAn(ID_MonAn)
);

-- ==========================================
-- 5. NHÓM BẢNG QUẢN LÝ KHO (TẠO CHO ĐỦ ĐỂ APP KHÔNG BÁO LỖI)
-- ==========================================
CREATE TABLE NguyenLieu(
    ID_NL INT PRIMARY KEY,
    TenNL NVARCHAR(50), 
    Dongia FLOAT, 
    Donvitinh NVARCHAR(50)
);

CREATE TABLE Kho(
    ID_NL INT PRIMARY KEY,
    SLTon INT DEFAULT 0,
    FOREIGN KEY (ID_NL) REFERENCES NguyenLieu(ID_NL)
);

CREATE TABLE PhieuNK(
    ID_NK INT PRIMARY KEY,
    ID_NV INT,
    NgayNK DATE,
    Tongtien FLOAT DEFAULT 0,
    FOREIGN KEY (ID_NV) REFERENCES NhanVien(ID_NV)
);

CREATE TABLE CTNK(
    ID_NK INT,
    ID_NL INT,
    SoLuong INT,
    Thanhtien FLOAT DEFAULT 0,
    PRIMARY KEY (ID_NK, ID_NL),
    FOREIGN KEY (ID_NK) REFERENCES PhieuNK(ID_NK),
    FOREIGN KEY (ID_NL) REFERENCES NguyenLieu(ID_NL)
);

CREATE TABLE PhieuXK(
    ID_XK INT PRIMARY KEY,
    ID_NV INT,
    NgayXK DATE,
    FOREIGN KEY (ID_NV) REFERENCES NhanVien(ID_NV)
);

CREATE TABLE CTXK(
    ID_XK INT,
    ID_NL INT,
    SoLuong INT,
    PRIMARY KEY (ID_XK, ID_NL),
    FOREIGN KEY (ID_XK) REFERENCES PhieuXK(ID_XK),
    FOREIGN KEY (ID_NL) REFERENCES NguyenLieu(ID_NL)
);
GO

-- ==========================================
-- BƠM DỮ LIỆU ĐỂ ÔNG ĐĂNG NHẬP VÀ TEST GIAO DIỆN
-- ==========================================

-- Bơm tài khoản để lát chạy file Main_LoginAndRegister
INSERT INTO NguoiDung(ID_ND, Email, MatKhau, Trangthai, Vaitro) VALUES 
(100, 'NVHoangViet@gmail.com', '123', 'Verified', 'Quan Ly'),
(101, 'NVHoangPhuc@gmail.com', '123', 'Verified', 'Nhan Vien');

INSERT INTO NhanVien(ID_NV, TenNV, NgayVL, SDT, Chucvu, ID_ND, ID_NQL, Tinhtrang) VALUES 
(100, N'Nguyen Hoang Viet', '2023-05-10', '0848044725', 'Quan ly', 100, 100, 'Dang lam viec'),
(101, N'Nguyen Hoang Phuc', '2023-05-20', '0838033334', 'Tiep tan', 101, 100, 'Dang lam viec');

-- Bơm Món ăn & Bàn
INSERT INTO MonAn(ID_MonAn, TenMon, Dongia, Loai, TrangThai) VALUES
(1, N'DUI CUU NUONG XE NHO', 250000, 'Aries', N'Dang kinh doanh'),
(2, N'BE SUON CUU NUONG GIAY BAC', 230000, 'Aries', N'Dang kinh doanh'),
(13, N'Bit tet bo My khoai tay', 179000, 'Taurus', N'Dang kinh doanh'),
(14, N'Bo bit tet Uc', 169000, 'Taurus', N'Dang kinh doanh'),
(29, N'Cua KingCrab Duc sot', 3650000, 'Cancer', N'Dang kinh doanh'),
(75, N'SIGNATURE WINE', 3290000, 'Aquarius', N'Dang kinh doanh'),
(84, N'Ca Hoi Ngam Tuong', 289000, 'Pisces', N'Dang kinh doanh');

INSERT INTO Ban(ID_Ban, TenBan, Vitri, Trangthai) VALUES
(100, N'Ban T1.1', N'Tang 1', N'Con trong'),
(101, N'Ban T1.2', N'Tang 1', N'Con trong'),
(102, N'Ban T1.3', N'Tang 1', N'Con trong'),
(112, N'Ban T2.1', N'Tang 2', N'Con trong'),
(113, N'Ban T2.2', N'Tang 2', N'Con trong'),
(124, N'Ban T3.1', N'Tang 3', N'Con trong');
GO