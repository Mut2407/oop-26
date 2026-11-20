package services;

import models.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.IOException;

public class QuanLyCuaHang {
    private DanhSachSanPham danhSachSanPham = new DanhSachSanPham();
    private DanhSachLoaiSP danhSachLoaiSP = new DanhSachLoaiSP();
    private DanhSachKhachHang danhSachKhachHang = new DanhSachKhachHang();
    private DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien();
    private DanhSachNhaCungCap danhSachNhaCungCap = new DanhSachNhaCungCap();
    private DanhSachHoaDon danhSachHoaDon = new DanhSachHoaDon();
    private DanhSachPhieuNhap danhSachPhieuNhap = new DanhSachPhieuNhap();
    private Kho kho = new Kho(danhSachSanPham);
    private LuuTruFileText luuTru;
    private Scanner banPhim;

    // Các class quản lý menu cho từng danh sách
    private QuanLySanPham quanLySanPham;
    private QuanLyLoaiSP quanLyLoaiSP;
    private QuanLyKhachHang quanLyKhachHang;
    private QuanLyNhanVien quanLyNhanVien;
    private QuanLyNhaCungCap quanLyNhaCungCap;
    private QuanLyHoaDon quanLyHoaDon;
    private QuanLyPhieuNhap quanLyPhieuNhap;

    public QuanLyCuaHang() {
        this("data");
    }

    public QuanLyCuaHang(String thuMuc) {
        luuTru = new LuuTruFileText(thuMuc);
    }

    // --- Getter cho LuuTruFileText ---
    public DanhSachSanPham getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public DanhSachLoaiSP getDanhSachLoaiSP() {
        return danhSachLoaiSP;
    }

    public DanhSachKhachHang getDanhSachKhachHang() {
        return danhSachKhachHang;
    }

    public DanhSachNhanVien getDanhSachNhanVien() {
        return danhSachNhanVien;
    }

    public DanhSachNhaCungCap getDanhSachNhaCungCap() {
        return danhSachNhaCungCap;
    }

    public DanhSachHoaDon getDanhSachHoaDon() {
        return danhSachHoaDon;
    }

    public DanhSachPhieuNhap getDanhSachPhieuNhap() {
        return danhSachPhieuNhap;
    }

    public Kho getKho() {
        return kho;
    }

    public void thayDuLieu(QuanLyCuaHang tam) {
        danhSachSanPham = tam.danhSachSanPham;
        danhSachLoaiSP = tam.danhSachLoaiSP;
        danhSachKhachHang = tam.danhSachKhachHang;
        danhSachNhanVien = tam.danhSachNhanVien;
        danhSachNhaCungCap = tam.danhSachNhaCungCap;
        danhSachHoaDon = tam.danhSachHoaDon;
        danhSachPhieuNhap = tam.danhSachPhieuNhap;
        kho = new Kho(danhSachSanPham);
    }

    public void docDuLieu() throws IOException {
        luuTru.docDuLieu(this);
    }

    public void ghiDuLieu() throws IOException {
        luuTru.ghiDuLieu(this);
    }

    // --- Khởi tạo các QuanLy* sau khi có Scanner ---
    private void khoiTaoQuanLy() {
        quanLySanPham = new QuanLySanPham(danhSachSanPham, danhSachLoaiSP, banPhim);
        quanLyLoaiSP = new QuanLyLoaiSP(danhSachLoaiSP, banPhim);
        quanLyKhachHang = new QuanLyKhachHang(danhSachKhachHang, banPhim);
        quanLyNhanVien = new QuanLyNhanVien(danhSachNhanVien, banPhim);
        quanLyNhaCungCap = new QuanLyNhaCungCap(danhSachNhaCungCap, banPhim);
        quanLyHoaDon = new QuanLyHoaDon(danhSachHoaDon, danhSachSanPham,
                danhSachKhachHang, danhSachNhanVien, kho, banPhim);
        quanLyPhieuNhap = new QuanLyPhieuNhap(danhSachPhieuNhap, danhSachSanPham,
                danhSachNhaCungCap, danhSachNhanVien, kho, banPhim);
    }

    // --- Chạy chương trình ---
    public void chay() {
        banPhim = new Scanner(System.in, "UTF-8");
        if (!napLucBatDau())
            return;
        khoiTaoQuanLy();
        while (chayMotLuot()) {
            // Mỗi lần chỉ chọn 1
        }
        luuVaThongBao();
    }

    private boolean napLucBatDau() {
        try {
            docDuLieu();
            return true;
        } catch (IOException e) {
            System.out.println("Khong mo duoc du lieu: " + e.getMessage());
            return false;
        }
    }

    private boolean chayMotLuot() {
        try {
            hienThiMenu();
            return xuLyMenu(nhapSoNguyen("Chon: ", 0, 10));
        } catch (IllegalArgumentException | DateTimeException e) {
            System.out.println("Da dung thao tac: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    private void hienThiMenu() {
        System.out.println("\n===== QUAN LY CUA HANG DUNG CU THE THAO =====");
        System.out.println("1. Quan ly San pham");
        System.out.println("2. Quan ly Loai san pham");
        System.out.println("3. Quan ly Khach hang");
        System.out.println("4. Quan ly Nhan vien");
        System.out.println("5. Quan ly Nha cung cap");
        System.out.println("6. Quan ly Hoa don");
        System.out.println("7. Quan ly Phieu nhap");
        System.out.println("8. Tra cuu ton kho");
        System.out.println("9. Thong ke tong tien");
        System.out.println("10. Luu file");
        System.out.println("0. Luu va thoat");
    }

    // Ủy thác menu cho từng QuanLy* tương ứng
    private boolean xuLyMenu(int chon) {
        switch (chon) {
            case 1: quanLySanPham.hienThiMenu(); break;
            case 2: quanLyLoaiSP.hienThiMenu(); break;
            case 3: quanLyKhachHang.hienThiMenu(); break;
            case 4: quanLyNhanVien.hienThiMenu(); break;
            case 5: quanLyNhaCungCap.hienThiMenu(); break;
            case 6: quanLyHoaDon.hienThiMenu(); break;
            case 7: quanLyPhieuNhap.hienThiMenu(); break;
            case 8: traCuuTon(); break;
            case 9: menuThongKe(); break;
            case 10: luuVaThongBao(); break;
            case 0: return false;
        }
        return true;
    }

    private void luuVaThongBao() {
        try {
            ghiDuLieu();
            System.out.println("Da luu du lieu.");
        } catch (IOException e) {
            System.out.println("Chua luu duoc: " + e.getMessage());
        }
    }

    private void traCuuTon() {
        String ma = nhapChuoi("Ma SP: ");
        int ton = kho.traCuuTon(ma);
        System.out.println(ton < 0 ? "Khong tim thay SP." : "Ton hien tai: " + ton);
    }

    private void menuThongKe() {
        int chon = nhapSoNguyen("1. Tat ca\n2. Theo khoang ngay: ", 1, 2);
        if (chon == 1) {
            inTongTien(quanLyHoaDon.thongKeTongTien(), quanLyPhieuNhap.thongKeTongTien());
            return;
        }
        LocalDate tu = nhapNgay("Tu ngay");
        LocalDate den = nhapNgay("Den ngay");
        inTongTien(quanLyHoaDon.thongKeTongTien(tu, den),
                quanLyPhieuNhap.thongKeTongTien(tu, den));
    }

    private void inTongTien(BigDecimal tienBan, BigDecimal tienNhap) {
        System.out.println("Tong tien ban: " + tienBan.toPlainString());
        System.out.println("Tong tien nhap: " + tienNhap.toPlainString());
    }

    // --- Nhập liệu tiện ích (chỉ dùng cho menu chính) ---
    private String nhapChuoi(String loiNhac) {
        System.out.print(loiNhac);
        String giaTri = banPhim.nextLine().trim();
        if (giaTri.isEmpty() || giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Chuoi khong duoc rong, chua dau | hay xuong dong.");
        return giaTri;
    }

    private int nhapSoNguyen(String loiNhac, int min, int max) {
        int giaTri = Integer.parseInt(nhapChuoi(loiNhac));
        if (giaTri < min || giaTri > max)
            throw new IllegalArgumentException("Nhap so nguyen tu " + min + " den " + max + ".");
        return giaTri;
    }

    private LocalDate nhapNgay(String loiNhac) {
        return LocalDate.parse(nhapChuoi(loiNhac + " (yyyy-MM-dd): "));
    }
}
