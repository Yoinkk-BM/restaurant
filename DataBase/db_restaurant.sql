USE [master]
GO
/****** Object:  Database [QuanLyNhaHang]    Script Date: 6/26/2026 8:39:21 PM ******/
CREATE DATABASE [QuanLyNhaHang]
 LOG ON 
( NAME = N'QuanLyNhaHang_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL17.SQLEXPRESS\MSSQL\DATA\QuanLyNhaHang_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [QuanLyNhaHang] SET COMPATIBILITY_LEVEL = 170
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [QuanLyNhaHang].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [QuanLyNhaHang] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET ARITHABORT OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [QuanLyNhaHang] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [QuanLyNhaHang] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET  DISABLE_BROKER 
GO
ALTER DATABASE [QuanLyNhaHang] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [QuanLyNhaHang] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [QuanLyNhaHang] SET  MULTI_USER 
GO
ALTER DATABASE [QuanLyNhaHang] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [QuanLyNhaHang] SET DB_CHAINING OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [QuanLyNhaHang] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [QuanLyNhaHang] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [QuanLyNhaHang] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [QuanLyNhaHang] SET OPTIMIZED_LOCKING = OFF 
GO
ALTER DATABASE [QuanLyNhaHang] SET QUERY_STORE = ON
GO
ALTER DATABASE [QuanLyNhaHang] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [QuanLyNhaHang]
GO
/****** Object:  UserDefinedFunction [dbo].[CTHD_Tinhtiengiam]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

----------------------------------------------------
-- PHẦN 3: HÀM (FUNCTIONS)
----------------------------------------------------

-- Hàm tính tiền giảm theo voucher
CREATE   FUNCTION [dbo].[CTHD_Tinhtiengiam](
    @Tongtien DECIMAL(18,0),
    @Code     NVARCHAR(10)
)
RETURNS DECIMAL(18,0)
AS
BEGIN
    DECLARE @Tiengiam  DECIMAL(18,0) = 0;
    DECLARE @v_phantram INT;
    SELECT @v_phantram = Phantram FROM Voucher WHERE Code_Voucher = @Code;
    SET @Tiengiam = ROUND(@Tongtien * @v_phantram / 100.0, 0);
    RETURN ISNULL(@Tiengiam, 0);
END;

GO
/****** Object:  Table [dbo].[Ban]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Ban](
	[ID_Ban] [int] NOT NULL,
	[TenBan] [nvarchar](50) NOT NULL,
	[Vitri] [nvarchar](50) NOT NULL,
	[Trangthai] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_Ban] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CTHD]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CTHD](
	[ID_HoaDon] [int] NOT NULL,
	[ID_MonAn] [int] NOT NULL,
	[SoLuong] [int] NOT NULL,
	[Thanhtien] [decimal](18, 0) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_HoaDon] ASC,
	[ID_MonAn] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CTNK]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CTNK](
	[ID_NK] [int] NOT NULL,
	[ID_NL] [int] NOT NULL,
	[SoLuong] [int] NOT NULL,
	[Thanhtien] [decimal](18, 0) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_NK] ASC,
	[ID_NL] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CTXK]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CTXK](
	[ID_XK] [int] NOT NULL,
	[ID_NL] [int] NOT NULL,
	[SoLuong] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_XK] ASC,
	[ID_NL] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HoaDon]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HoaDon](
	[ID_HoaDon] [int] NOT NULL,
	[ID_KH] [int] NULL,
	[ID_Ban] [int] NULL,
	[NgayHD] [date] NOT NULL,
	[TienMonAn] [decimal](18, 0) NULL,
	[Code_Voucher] [nvarchar](10) NULL,
	[TienGiam] [decimal](18, 0) NULL,
	[Tongtien] [decimal](18, 0) NULL,
	[Trangthai] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_HoaDon] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KhachHang]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KhachHang](
	[ID_KH] [int] NOT NULL,
	[TenKH] [nvarchar](50) NOT NULL,
	[Ngaythamgia] [date] NOT NULL,
	[Doanhso] [decimal](18, 0) NOT NULL,
	[Diemtichluy] [int] NOT NULL,
	[ID_ND] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_KH] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Kho]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Kho](
	[ID_NL] [int] NOT NULL,
	[SLTon] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_NL] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[KitchenOrders]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[KitchenOrders](
	[ID_KO] [int] IDENTITY(1,1) NOT NULL,
	[ID_HoaDon] [int] NOT NULL,
	[ID_MonAn] [int] NOT NULL,
	[TenMon] [nvarchar](100) NOT NULL,
	[SoLuong] [int] NOT NULL,
	[TrangThai] [nvarchar](30) NOT NULL,
	[ThoiGianDat] [datetime] NOT NULL,
	[TenBan] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_KO] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MonAn]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MonAn](
	[ID_MonAn] [int] NOT NULL,
	[TenMon] [nvarchar](100) NOT NULL,
	[DonGia] [decimal](18, 0) NOT NULL,
	[Loai] [nvarchar](50) NULL,
	[TrangThai] [nvarchar](30) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_MonAn] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NguoiDung]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NguoiDung](
	[ID_ND] [int] NOT NULL,
	[Email] [nvarchar](50) NOT NULL,
	[Matkhau] [nvarchar](20) NOT NULL,
	[VerifyCode] [nvarchar](10) NULL,
	[Trangthai] [nvarchar](10) NULL,
	[Vaitro] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_ND] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NguyenLieu]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NguyenLieu](
	[ID_NL] [int] NOT NULL,
	[TenNL] [nvarchar](50) NOT NULL,
	[Dongia] [decimal](18, 0) NOT NULL,
	[Donvitinh] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_NL] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NhanVien]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NhanVien](
	[ID_NV] [int] NOT NULL,
	[TenNV] [nvarchar](50) NOT NULL,
	[NgayVL] [date] NOT NULL,
	[SDT] [nvarchar](50) NOT NULL,
	[Chucvu] [nvarchar](50) NULL,
	[ID_ND] [int] NULL,
	[ID_NQL] [int] NULL,
	[Tinhtrang] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_NV] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PhieuNK]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PhieuNK](
	[ID_NK] [int] NOT NULL,
	[ID_NV] [int] NULL,
	[NgayNK] [date] NOT NULL,
	[Tongtien] [decimal](18, 0) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_NK] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PhieuXK]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PhieuXK](
	[ID_XK] [int] NOT NULL,
	[ID_NV] [int] NULL,
	[NgayXK] [date] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_XK] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Voucher]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Voucher](
	[Code_Voucher] [nvarchar](10) NOT NULL,
	[Mota] [nvarchar](100) NOT NULL,
	[Phantram] [int] NULL,
	[LoaiMA] [nvarchar](50) NULL,
	[SoLuong] [int] NULL,
	[Diem] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[Code_Voucher] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[CTHD] ADD  DEFAULT ((0)) FOR [Thanhtien]
GO
ALTER TABLE [dbo].[CTNK] ADD  DEFAULT ((0)) FOR [Thanhtien]
GO
ALTER TABLE [dbo].[HoaDon] ADD  DEFAULT ((0)) FOR [TienMonAn]
GO
ALTER TABLE [dbo].[HoaDon] ADD  DEFAULT ((0)) FOR [TienGiam]
GO
ALTER TABLE [dbo].[HoaDon] ADD  DEFAULT ((0)) FOR [Tongtien]
GO
ALTER TABLE [dbo].[KhachHang] ADD  DEFAULT ((0)) FOR [Doanhso]
GO
ALTER TABLE [dbo].[KhachHang] ADD  DEFAULT ((0)) FOR [Diemtichluy]
GO
ALTER TABLE [dbo].[Kho] ADD  DEFAULT ((0)) FOR [SLTon]
GO
ALTER TABLE [dbo].[KitchenOrders] ADD  DEFAULT ('Dang cho') FOR [TrangThai]
GO
ALTER TABLE [dbo].[KitchenOrders] ADD  DEFAULT (getdate()) FOR [ThoiGianDat]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  DEFAULT (NULL) FOR [VerifyCode]
GO
ALTER TABLE [dbo].[NguoiDung] ADD  DEFAULT ('') FOR [Trangthai]
GO
ALTER TABLE [dbo].[NhanVien] ADD  DEFAULT (NULL) FOR [ID_ND]
GO
ALTER TABLE [dbo].[PhieuNK] ADD  DEFAULT ((0)) FOR [Tongtien]
GO
ALTER TABLE [dbo].[CTHD]  WITH CHECK ADD FOREIGN KEY([ID_HoaDon])
REFERENCES [dbo].[HoaDon] ([ID_HoaDon])
GO
ALTER TABLE [dbo].[CTHD]  WITH CHECK ADD FOREIGN KEY([ID_HoaDon])
REFERENCES [dbo].[HoaDon] ([ID_HoaDon])
GO
ALTER TABLE [dbo].[CTHD]  WITH CHECK ADD FOREIGN KEY([ID_MonAn])
REFERENCES [dbo].[MonAn] ([ID_MonAn])
GO
ALTER TABLE [dbo].[CTHD]  WITH CHECK ADD FOREIGN KEY([ID_MonAn])
REFERENCES [dbo].[MonAn] ([ID_MonAn])
GO
ALTER TABLE [dbo].[CTNK]  WITH CHECK ADD FOREIGN KEY([ID_NK])
REFERENCES [dbo].[PhieuNK] ([ID_NK])
GO
ALTER TABLE [dbo].[CTNK]  WITH CHECK ADD FOREIGN KEY([ID_NK])
REFERENCES [dbo].[PhieuNK] ([ID_NK])
GO
ALTER TABLE [dbo].[CTNK]  WITH CHECK ADD FOREIGN KEY([ID_NL])
REFERENCES [dbo].[NguyenLieu] ([ID_NL])
GO
ALTER TABLE [dbo].[CTNK]  WITH CHECK ADD FOREIGN KEY([ID_NL])
REFERENCES [dbo].[NguyenLieu] ([ID_NL])
GO
ALTER TABLE [dbo].[CTXK]  WITH CHECK ADD FOREIGN KEY([ID_NL])
REFERENCES [dbo].[NguyenLieu] ([ID_NL])
GO
ALTER TABLE [dbo].[CTXK]  WITH CHECK ADD FOREIGN KEY([ID_NL])
REFERENCES [dbo].[NguyenLieu] ([ID_NL])
GO
ALTER TABLE [dbo].[CTXK]  WITH CHECK ADD FOREIGN KEY([ID_XK])
REFERENCES [dbo].[PhieuXK] ([ID_XK])
GO
ALTER TABLE [dbo].[CTXK]  WITH CHECK ADD FOREIGN KEY([ID_XK])
REFERENCES [dbo].[PhieuXK] ([ID_XK])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([Code_Voucher])
REFERENCES [dbo].[Voucher] ([Code_Voucher])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([Code_Voucher])
REFERENCES [dbo].[Voucher] ([Code_Voucher])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([ID_Ban])
REFERENCES [dbo].[Ban] ([ID_Ban])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([ID_Ban])
REFERENCES [dbo].[Ban] ([ID_Ban])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([ID_KH])
REFERENCES [dbo].[KhachHang] ([ID_KH])
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD FOREIGN KEY([ID_KH])
REFERENCES [dbo].[KhachHang] ([ID_KH])
GO
ALTER TABLE [dbo].[KhachHang]  WITH CHECK ADD FOREIGN KEY([ID_ND])
REFERENCES [dbo].[NguoiDung] ([ID_ND])
GO
ALTER TABLE [dbo].[KhachHang]  WITH CHECK ADD FOREIGN KEY([ID_ND])
REFERENCES [dbo].[NguoiDung] ([ID_ND])
GO
ALTER TABLE [dbo].[Kho]  WITH CHECK ADD FOREIGN KEY([ID_NL])
REFERENCES [dbo].[NguyenLieu] ([ID_NL])
GO
ALTER TABLE [dbo].[Kho]  WITH CHECK ADD FOREIGN KEY([ID_NL])
REFERENCES [dbo].[NguyenLieu] ([ID_NL])
GO
ALTER TABLE [dbo].[KitchenOrders]  WITH CHECK ADD FOREIGN KEY([ID_HoaDon])
REFERENCES [dbo].[HoaDon] ([ID_HoaDon])
GO
ALTER TABLE [dbo].[KitchenOrders]  WITH CHECK ADD FOREIGN KEY([ID_HoaDon])
REFERENCES [dbo].[HoaDon] ([ID_HoaDon])
GO
ALTER TABLE [dbo].[KitchenOrders]  WITH CHECK ADD FOREIGN KEY([ID_MonAn])
REFERENCES [dbo].[MonAn] ([ID_MonAn])
GO
ALTER TABLE [dbo].[KitchenOrders]  WITH CHECK ADD FOREIGN KEY([ID_MonAn])
REFERENCES [dbo].[MonAn] ([ID_MonAn])
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD FOREIGN KEY([ID_ND])
REFERENCES [dbo].[NguoiDung] ([ID_ND])
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD FOREIGN KEY([ID_ND])
REFERENCES [dbo].[NguoiDung] ([ID_ND])
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD FOREIGN KEY([ID_NQL])
REFERENCES [dbo].[NhanVien] ([ID_NV])
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD FOREIGN KEY([ID_NQL])
REFERENCES [dbo].[NhanVien] ([ID_NV])
GO
ALTER TABLE [dbo].[PhieuNK]  WITH CHECK ADD FOREIGN KEY([ID_NV])
REFERENCES [dbo].[NhanVien] ([ID_NV])
GO
ALTER TABLE [dbo].[PhieuNK]  WITH CHECK ADD FOREIGN KEY([ID_NV])
REFERENCES [dbo].[NhanVien] ([ID_NV])
GO
ALTER TABLE [dbo].[PhieuXK]  WITH CHECK ADD FOREIGN KEY([ID_NV])
REFERENCES [dbo].[NhanVien] ([ID_NV])
GO
ALTER TABLE [dbo].[PhieuXK]  WITH CHECK ADD FOREIGN KEY([ID_NV])
REFERENCES [dbo].[NhanVien] ([ID_NV])
GO
ALTER TABLE [dbo].[Ban]  WITH CHECK ADD CHECK  (([Trangthai]='Da dat truoc' OR [Trangthai]='Dang dung bua' OR [Trangthai]='Con trong'))
GO
ALTER TABLE [dbo].[Ban]  WITH CHECK ADD CHECK  (([Trangthai]='Da dat truoc' OR [Trangthai]='Dang dung bua' OR [Trangthai]='Con trong'))
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD CHECK  (([Trangthai]='Da thanh toan' OR [Trangthai]='Chua thanh toan'))
GO
ALTER TABLE [dbo].[HoaDon]  WITH CHECK ADD CHECK  (([Trangthai]='Da thanh toan' OR [Trangthai]='Chua thanh toan'))
GO
ALTER TABLE [dbo].[KitchenOrders]  WITH CHECK ADD CHECK  (([TrangThai]='Hoan thanh' OR [TrangThai]='Dang lam' OR [TrangThai]='Dang cho'))
GO
ALTER TABLE [dbo].[KitchenOrders]  WITH CHECK ADD CHECK  (([TrangThai]='Hoan thanh' OR [TrangThai]='Dang lam' OR [TrangThai]='Dang cho'))
GO
ALTER TABLE [dbo].[MonAn]  WITH CHECK ADD CHECK  (([Loai]='Pisces' OR [Loai]='Aquarius' OR [Loai]='Capricorn' OR [Loai]='Sagittarius' OR [Loai]='Scorpio' OR [Loai]='Libra' OR [Loai]='Virgo' OR [Loai]='Leo' OR [Loai]='Cancer' OR [Loai]='Gemini' OR [Loai]='Taurus' OR [Loai]='Aries'))
GO
ALTER TABLE [dbo].[MonAn]  WITH CHECK ADD CHECK  (([Loai]='Pisces' OR [Loai]='Aquarius' OR [Loai]='Capricorn' OR [Loai]='Sagittarius' OR [Loai]='Scorpio' OR [Loai]='Libra' OR [Loai]='Virgo' OR [Loai]='Leo' OR [Loai]='Cancer' OR [Loai]='Gemini' OR [Loai]='Taurus' OR [Loai]='Aries'))
GO
ALTER TABLE [dbo].[MonAn]  WITH CHECK ADD CHECK  (([TrangThai]='Ngung kinh doanh' OR [TrangThai]='Dang kinh doanh'))
GO
ALTER TABLE [dbo].[MonAn]  WITH CHECK ADD CHECK  (([TrangThai]='Ngung kinh doanh' OR [TrangThai]='Dang kinh doanh'))
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD CHECK  (([Vaitro]='Nhan Vien Bep' OR [Vaitro]='Quan Ly' OR [Vaitro]='Nhan Vien Kho' OR [Vaitro]='Nhan Vien' OR [Vaitro]='Thu Ngan' OR [Vaitro]='Khach Hang'))
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD CHECK  (([Vaitro]='Nhan Vien Bep' OR [Vaitro]='Quan Ly' OR [Vaitro]='Nhan Vien Kho' OR [Vaitro]='Nhan Vien' OR [Vaitro]='Thu Ngan' OR [Vaitro]='Khach Hang'))
GO
ALTER TABLE [dbo].[NguoiDung]  WITH CHECK ADD CHECK  (([Vaitro]='Nhan Vien Bep' OR [Vaitro]='Quan Ly' OR [Vaitro]='Nhan Vien Kho' OR [Vaitro]='Nhan Vien' OR [Vaitro]='Thu Ngan' OR [Vaitro]='Khach Hang'))
GO
ALTER TABLE [dbo].[NguyenLieu]  WITH CHECK ADD CHECK  (([Donvitinh]='l' OR [Donvitinh]='ml' OR [Donvitinh]='kg' OR [Donvitinh]='g'))
GO
ALTER TABLE [dbo].[NguyenLieu]  WITH CHECK ADD CHECK  (([Donvitinh]='l' OR [Donvitinh]='ml' OR [Donvitinh]='kg' OR [Donvitinh]='g'))
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD CHECK  (([Chucvu]='Quan ly' OR [Chucvu]='Kho' OR [Chucvu]='Bep' OR [Chucvu]='Thu ngan' OR [Chucvu]='Tiep tan' OR [Chucvu]='Phuc vu'))
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD CHECK  (([Chucvu]='Quan ly' OR [Chucvu]='Kho' OR [Chucvu]='Bep' OR [Chucvu]='Thu ngan' OR [Chucvu]='Tiep tan' OR [Chucvu]='Phuc vu'))
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD CHECK  (([Tinhtrang]='Da nghi viec' OR [Tinhtrang]='Dang lam viec'))
GO
ALTER TABLE [dbo].[NhanVien]  WITH CHECK ADD CHECK  (([Tinhtrang]='Da nghi viec' OR [Tinhtrang]='Dang lam viec'))
GO
ALTER TABLE [dbo].[Voucher]  WITH CHECK ADD CHECK  (([LoaiMA]='Pisces' OR [LoaiMA]='Aquarius' OR [LoaiMA]='Capricorn' OR [LoaiMA]='Sagittarius' OR [LoaiMA]='Scorpio' OR [LoaiMA]='Libra' OR [LoaiMA]='Virgo' OR [LoaiMA]='Leo' OR [LoaiMA]='Cancer' OR [LoaiMA]='Gemini' OR [LoaiMA]='Taurus' OR [LoaiMA]='Aries' OR [LoaiMA]='All'))
GO
ALTER TABLE [dbo].[Voucher]  WITH CHECK ADD CHECK  (([LoaiMA]='Pisces' OR [LoaiMA]='Aquarius' OR [LoaiMA]='Capricorn' OR [LoaiMA]='Sagittarius' OR [LoaiMA]='Scorpio' OR [LoaiMA]='Libra' OR [LoaiMA]='Virgo' OR [LoaiMA]='Leo' OR [LoaiMA]='Cancer' OR [LoaiMA]='Gemini' OR [LoaiMA]='Taurus' OR [LoaiMA]='Aries' OR [LoaiMA]='All'))
GO
ALTER TABLE [dbo].[Voucher]  WITH CHECK ADD CHECK  (([Phantram]>(0) AND [Phantram]<=(100)))
GO
ALTER TABLE [dbo].[Voucher]  WITH CHECK ADD CHECK  (([Phantram]>(0) AND [Phantram]<=(100)))
GO
/****** Object:  StoredProcedure [dbo].[KH_TruDTL]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- Trừ điểm tích lũy khách hàng khi đổi voucher
CREATE   PROCEDURE [dbo].[KH_TruDTL](@ID INT, @diemdoi INT)
AS
BEGIN
    IF EXISTS (SELECT 1 FROM KhachHang WHERE ID_KH = @ID)
        UPDATE KhachHang SET Diemtichluy = Diemtichluy - @diemdoi WHERE ID_KH = @ID;
    ELSE
        RAISERROR('Khach hang khong ton tai', 16, 1);
END

GO
/****** Object:  StoredProcedure [dbo].[Voucher_GiamSL]    Script Date: 6/26/2026 8:39:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

----------------------------------------------------
-- PHẦN 4: THỦ TỤC (STORED PROCEDURES)
----------------------------------------------------

-- Giảm số lượng Voucher khi đổi
CREATE   PROCEDURE [dbo].[Voucher_GiamSL](@code NVARCHAR(10))
AS
BEGIN
    IF EXISTS (SELECT 1 FROM Voucher WHERE Code_Voucher = @code)
        UPDATE Voucher SET SoLuong = SoLuong - 1 WHERE Code_Voucher = @code;
    ELSE
        RAISERROR('Voucher khong ton tai', 16, 1);
END

GO
USE [master]
GO
ALTER DATABASE [QuanLyNhaHang] SET  READ_WRITE 
GO
