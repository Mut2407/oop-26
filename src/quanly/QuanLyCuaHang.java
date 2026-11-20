package quanly;

import models.*;
import danhsach.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class QuanLyCuaHang {
    // Thuộc tính static: DUY NHẤT một bộ dữ liệu cho toàn bộ hệ thống
    protected static DanhSachSanPham dsSanPham = new DanhSachSanPham();
    protected static DanhSachLoaiSP dsLoaiSP = new DanhSachLoaiSP();
    protected static DanhSachKhachHang dsKhachHang = new DanhSachKhachHang();
    protected static DanhSachNhanVien dsNhanVien = new DanhSachNhanVien();
    protected static DanhSachNhaCungCap dsNhaCungCap = new DanhSachNhaCungCap();
    protected static DanhSachHoaDon dsHoaDon = new DanhSachHoaDon();
    protected static DanhSachPhieuNhap dsPhieuNhap = new DanhSachPhieuNhap();
    protected static Kho kho = new Kho(dsSanPham);
    protected static LuuTruFileText luuTru = new LuuTruFileText("data");

    public QuanLyCuaHang() {
    }

    // ======= Nạp dữ liệu từ file =======
    public void docDuLieu() {
        try {
            luuTru.docDuLieu(this);
            System.out.println("Nap du lieu tu file thanh cong.");
        } catch (IOException e) {
            System.out.println("Loi khi nap du lieu: " + e.getMessage());
        }
    }

    // ======= Ghi tất cả dữ liệu ra file =======
    public void ghiDuLieu() {
        try {
            luuTru.ghiDuLieu(this);
            System.out.println("Da ghi toan bo du lieu ra file.");
        } catch (IOException e) {
            System.out.println("Loi khi ghi du lieu: " + e.getMessage());
        }
    }

    // Phương thức khung để các lớp con @Override (ĐA HÌNH - Polymorphism)
    public void menuChinh() {
    }

    // Getter cho lưu trữ
    public DanhSachSanPham getDanhSachSanPham() { return dsSanPham; }
    public DanhSachLoaiSP getDanhSachLoaiSP() { return dsLoaiSP; }
    public DanhSachKhachHang getDanhSachKhachHang() { return dsKhachHang; }
    public DanhSachNhanVien getDanhSachNhanVien() { return dsNhanVien; }
    public DanhSachNhaCungCap getDanhSachNhaCungCap() { return dsNhaCungCap; }
    public DanhSachHoaDon getDanhSachHoaDon() { return dsHoaDon; }
    public DanhSachPhieuNhap getDanhSachPhieuNhap() { return dsPhieuNhap; }
    public Kho getKho() { return kho; }

    // Thống kê dùng chung
    public static void thongKeTongTien() {
        BigDecimal thu = dsHoaDon.thongKeTongTien();
        BigDecimal chi = dsPhieuNhap.thongKeTongTien();
        BigDecimal loiNhuan = thu.subtract(chi);
        System.out.println("\n===== THONG KE DOANH THU TONG =====");
        System.out.println("Tong Thu (Hoa don):    " + thu.toPlainString());
        System.out.println("Tong Chi (Phieu nhap):  " + chi.toPlainString());
        System.out.println("Loi Nhuan:              " + loiNhuan.toPlainString());
    }

    public static void thongKeTheoKhoang(LocalDate tu, LocalDate den) {
        BigDecimal thu = dsHoaDon.thongKeTongTien(tu, den);
        BigDecimal chi = dsPhieuNhap.thongKeTongTien(tu, den);
        BigDecimal loiNhuan = thu.subtract(chi);
        System.out.println("\n===== THONG KE DOANH THU TU " + tu + " DEN " + den + " =====");
        System.out.println("Tong Thu (Hoa don):    " + thu.toPlainString());
        System.out.println("Tong Chi (Phieu nhap):  " + chi.toPlainString());
        System.out.println("Loi Nhuan:              " + loiNhuan.toPlainString());
    }

    public static void traCuuTonKho(String maSP) {
        int ton = kho.traCuuTon(maSP);
        if (ton < 0) {
            System.out.println("Khong tim thay san pham co ma: " + maSP);
        } else {
            System.out.println("So luong ton hien tai cua " + maSP + ": " + ton);
        }
    }
}
