package ui;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;
import models.*;
import services.*;

public final class MenuCuaHang {
    private final QuanLyCuaHang cuaHang;
    private final LuuTruFileText luuTru;
    private final NhapLieuConsole nhap;
    private final NhapSanPham nhapSanPham;
    private final NhapChungTu nhapChungTu;

    public MenuCuaHang(QuanLyCuaHang cuaHang, Scanner scanner, LuuTruFileText luuTru) {
        this.cuaHang = cuaHang;
        this.luuTru = luuTru;
        nhap = new NhapLieuConsole(scanner);
        nhapSanPham = new NhapSanPham(cuaHang, nhap);
        nhapChungTu = new NhapChungTu(cuaHang, nhap);
    }

    public void chay() throws IOException {
        try {
            vongLapMenu();
        } catch (NoSuchElementException e) {
            luuTru.ghiDuLieu(cuaHang);
            System.out.println("Ket thuc nhap lieu; da luu cac thao tac hoan tat.");
        }
    }

    private void vongLapMenu() {
        while (true) {
            hienThiMenu();
            try {
                String chon = nhap.docDong("Lua chon: ");
                if (chon.equals("0")) {
                    luu();
                    System.out.println("Da luu. Thoat chuong trinh.");
                    return;
                }
                xuLy(chon);
            } catch (IllegalArgumentException e) {
                System.out.println("Khong thuc hien: " + e.getMessage());
            } catch (UncheckedIOException e) {
                System.out.println("CHUA LUU DUOC: " + e.getMessage() + ". Du lieu van o RAM; chon 8 de thu lai.");
            }
        }
    }

    private void hienThiMenu() {
        System.out.println("\n===== SPORT STORE =====");
        System.out.println("1. San pham  2. Loai SP  3. Khach hang  4. Nhan vien  5. Nha cung cap");
        System.out.println("6. Hoa don  7. Phieu nhap  8. Luu  9. Thong ke tong tien");
        System.out.println("10. Nhap n/them k san pham  11. Loc san pham  0. Luu va thoat");
    }

    private void xuLy(String chon) {
        switch (chon) {
            case "1": menuSanPham(); break;
            case "2": menuLoaiSP(); break;
            case "3": menuKhachHang(); break;
            case "4": menuNhanVien(); break;
            case "5": menuNhaCungCap(); break;
            case "6": menuHoaDon(); break;
            case "7": menuPhieuNhap(); break;
            case "8": luu(); System.out.println("Da luu."); break;
            case "9": thongKe(); break;
            case "10": nhapSanPham.themNhieu(); luu(); System.out.println("Da them va luu ca lo."); break;
            case "11": nhapSanPham.loc(); break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void luu() {
        try {
            luuTru.ghiDuLieu(cuaHang);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void menuSanPham() {
        new MenuDanhSach<>("SAN PHAM", cuaHang::getSanPham, nhapSanPham::nhap,
                SanPham::hienThiThongTin, cuaHang::themSanPham, cuaHang::suaSanPham,
                cuaHang::xoaSanPham, nhap, this::luu).chay();
    }

    private void menuLoaiSP() {
        new MenuDanhSach<>("LOAI SP", cuaHang::getLoaiSP, this::nhapLoaiSP,
                LoaiSP::hienThiThongTin, cuaHang::themLoaiSP, cuaHang::suaLoaiSP,
                cuaHang::xoaLoaiSP, nhap, this::luu).chay();
    }

    private LoaiSP nhapLoaiSP(String ma) {
        return new LoaiSP(ma == null ? nhap.ma("Ma loai SP: ") : ma,
                nhap.batBuoc("Ten loai SP: "), nhap.docDong("Mo ta: "));
    }

    private void menuKhachHang() {
        new MenuDanhSach<>("KHACH HANG", cuaHang::getKhachHang, this::nhapKhachHang,
                KhachHang::hienThiThongTin, cuaHang::themKhachHang, cuaHang::suaKhachHang,
                cuaHang::xoaKhachHang, nhap, this::luu).chay();
    }

    private KhachHang nhapKhachHang(String ma) {
        return new KhachHang(ma == null ? nhap.ma("Ma khach hang: ") : ma,
                nhap.batBuoc("Ho ten: "), nhap.dienThoai(), nhap.batBuoc("Dia chi: "));
    }

    private void menuNhanVien() {
        new MenuDanhSach<>("NHAN VIEN", cuaHang::getNhanVien, this::nhapNhanVien,
                NhanVien::hienThiThongTin, cuaHang::themNhanVien, cuaHang::suaNhanVien,
                cuaHang::xoaNhanVien, nhap, this::luu).chay();
    }

    private NhanVien nhapNhanVien(String ma) {
        return new NhanVien(ma == null ? nhap.ma("Ma nhan vien: ") : ma,
                nhap.batBuoc("Ho ten: "), nhap.dienThoai(), nhap.batBuoc("Ca lam viec: "));
    }

    private void menuNhaCungCap() {
        new MenuDanhSach<>("NHA CUNG CAP", cuaHang::getNhaCungCap, this::nhapNhaCungCap,
                NhaCungCap::hienThiThongTin, cuaHang::themNhaCungCap, cuaHang::suaNhaCungCap,
                cuaHang::xoaNhaCungCap, nhap, this::luu).chay();
    }

    private NhaCungCap nhapNhaCungCap(String ma) {
        return new NhaCungCap(ma == null ? nhap.ma("Ma nha cung cap: ") : ma,
                nhap.batBuoc("Ten nha cung cap: "), nhap.dienThoai(), nhap.batBuoc("Dia chi: "));
    }

    private void menuHoaDon() {
        new MenuDanhSach<>("HOA DON", cuaHang::getHoaDon, nhapChungTu::nhapHoaDon,
                NhapChungTu::hienThiHoaDon, cuaHang::themHoaDon, cuaHang::suaHoaDon,
                cuaHang::xoaHoaDon, nhap, this::luu).chay();
    }

    private void menuPhieuNhap() {
        new MenuDanhSach<>("PHIEU NHAP", cuaHang::getPhieuNhap, nhapChungTu::nhapPhieuNhap,
                NhapChungTu::hienThiPhieuNhap, cuaHang::themPhieuNhap, cuaHang::suaPhieuNhap,
                cuaHang::xoaPhieuNhap, nhap, this::luu).chay();
    }

    private void thongKe() {
        String chon = nhap.docDong("1. Tat ca  2. Theo khoang ngay: ");
        if (chon.equals("1")) {
            System.out.println("Tong tien ban: " + cuaHang.thongKeTienBan().toPlainString());
            System.out.println("Tong tien nhap: " + cuaHang.thongKeTienNhap().toPlainString());
        } else if (chon.equals("2")) {
            LocalDate tu = nhap.ngay("Tu ngay yyyy-MM-dd: ");
            LocalDate den = nhap.ngay("Den ngay yyyy-MM-dd: ");
            System.out.println("Tong tien ban: " + cuaHang.thongKeTienBan(tu, den).toPlainString());
            System.out.println("Tong tien nhap: " + cuaHang.thongKeTienNhap(tu, den).toPlainString());
        } else {
            throw new IllegalArgumentException("Lua chon khong hop le.");
        }
    }
}
