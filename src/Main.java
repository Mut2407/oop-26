import java.io.IOException;
import java.util.Scanner;
import models.DanhMuc;
import models.DuLieu;
import services.DanhSachKhachHang;
import services.DanhSachNhaCungCap;
import services.DanhSachNhanVien;
import services.DanhSachSanPham;
import services.LuuTruFileText;
import ui.MenuDanhMuc;
import ui.MenuKhachHang;
import ui.MenuNhaCungCap;
import ui.MenuNhanVien;
import ui.MenuSanPham;
import ui.NhapLieuConsole;

public class Main {
    private static final LuuTruFileText LUU_TRU = new LuuTruFileText("data");

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            chay(new NhapLieuConsole(scanner));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Khong the khoi dong: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void chay(NhapLieuConsole nhap) throws IOException {
        for (DanhMuc dm : LUU_TRU.docDanhMuc("danhmuc.txt")) {
            DuLieu.dangKyDanhMuc(dm);
        }
        DanhSachSanPham sanPham = LUU_TRU.docSanPham("sanpham.txt", DuLieu.getDanhMuc());
        DanhSachKhachHang khachHang = LUU_TRU.docKhachHang("khachhang.txt");
        DanhSachNhanVien nhanVien = LUU_TRU.docNhanVien("nhanvien.txt");
        DanhSachNhaCungCap nhaCungCap = LUU_TRU.docNhaCungCap("nhacungcap.txt");
        xuLyMenu(nhap, sanPham, khachHang, nhanVien, nhaCungCap);
    }

    private static void xuLyMenu(NhapLieuConsole nhap, DanhSachSanPham sanPham,
            DanhSachKhachHang khachHang, DanhSachNhanVien nhanVien, DanhSachNhaCungCap nhaCungCap) {
        while (true) {
            hienThiMenu();
            String chon = nhap.docDong("Nhap lua chon: ");
            if (chon == null || chon.equals("0")) {
                System.out.println("Thoat chuong trinh!");
                return;
            }
            switch (chon) {
                case "1": new MenuSanPham(sanPham, nhap, LUU_TRU, "sanpham.txt").quanLy(); break;
                case "2": new MenuDanhMuc(sanPham, nhap, LUU_TRU, "danhmuc.txt").quanLy(); break;
                case "3": new MenuKhachHang(khachHang, nhap, LUU_TRU, "khachhang.txt").quanLy(); break;
                case "4": new MenuNhanVien(nhanVien, nhap, LUU_TRU, "nhanvien.txt").quanLy(); break;
                case "5": new MenuNhaCungCap(nhaCungCap, nhap, LUU_TRU, "nhacungcap.txt").quanLy(); break;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }

    public static void hienThiMenu() {
        System.out.println("\n===== QUAN LY CUA HANG DUNG CU THE THAO =====");
        System.out.println("1. San pham");
        System.out.println("2. Danh muc");
        System.out.println("3. Khach hang");
        System.out.println("4. Nhan vien");
        System.out.println("5. Nha cung cap");
        System.out.println("0. Thoat");
    }
}
