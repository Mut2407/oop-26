import java.util.Scanner;
import models.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        SanPham[] danhSachSanPham = DuLieu.taoDanhSachSanPham();
        KhachHang[] danhSachKhachHang = DuLieu.taoDanhSachKhachHang();
        NhanVien[] danhSachNhanVien = DuLieu.taoDanhSachNhanVien();
        NhaCungCap[] danhSachNhaCungCap = DuLieu.taoDanhSachNhaCungCap();
        xuLyMenu(danhSachSanPham, danhSachKhachHang, danhSachNhanVien, danhSachNhaCungCap);
        sc.close();
    }

    private static void xuLyMenu(SanPham[] danhSachSanPham, KhachHang[] danhSachKhachHang, NhanVien[] danhSachNhanVien, NhaCungCap[] danhSachNhaCungCap) { {
        int luaChon = -1;
        do {
            hienThiMenu();
            System.out.print("Nhap lua chon: ");

            if (!sc.hasNextLine()) {
                return;
            }
            if (!sc.hasNextInt()) {
                System.out.println("Vui long nhap mot so!");
                sc.nextLine();
                continue;
            }

            luaChon = sc.nextInt();
            sc.nextLine();

            switch (luaChon) {
                case 1:
                    quanLySanPham(danhSachSanPham);
                    break;

                case 2:
                    System.out.println("Quan ly loai san pham");
                    break;

                case 3:
                    System.out.println("===== DANH SACH KHACH HANG =====");
                    for (KhachHang kh : danhSachKhachHang) {
                        System.out.println(kh.thongTinKhachHang());
                        System.out.println("------------------------------");
                    }
                    break;

                case 4:
                    System.out.println("===== DANH SACH NHAN VIEN =====");
                    for (NhanVien nv : danhSachNhanVien) {
                        System.out.println(nv.thongTinNhanVien());
                        System.out.println("------------------------------");
                }
                break;

                case 5:
                    System.out.println("===== DANH SACH NHA CUNG CAP =====");
                    for (NhaCungCap ncc : danhSachNhaCungCap) {
                        System.out.println(ncc.thongTinNhaCungCap());
                        System.out.println("------------------------------");
                }
                break;

                case 6:
                    System.out.println("Quan ly hoa don");
                    break;

                case 7:
                    System.out.println("Quan ly phieu nhap");
                    break;

                case 0:
                    System.out.println("Thoat chuong trinh!");
                    break;

                default:
                    System.out.println("Lua chon khong hop le!");
            }

        } while (luaChon != 0);
    }
    }

    private static void quanLySanPham(SanPham[] danhSachSanPham) {
        while (true) {
            System.out.println("\n===== QUAN LY SAN PHAM =====");
            System.out.println("1. Giay the thao");
            System.out.println("2. Dung cu the thao");
            System.out.println("3. Tat ca san pham");
            System.out.println("0. Quay lai");
            System.out.print("Nhap lua chon: ");

            if (!sc.hasNextLine()) {
                return;
            }
            int luaChon;
            try {
                luaChon = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap mot so!");
                continue;
            }
            if (luaChon == 0) {
                return;
            }
            if (luaChon < 1 || luaChon > 3) {
                System.out.println("Lua chon khong hop le!");
                continue;
            }

            System.out.println("===== DANH SACH SAN PHAM =====");
            boolean coSanPham = false;
            for (SanPham sp : danhSachSanPham) {
                if (luaChon == 3
                        || (luaChon == 1 && sp instanceof GiayTheThao)
                        || (luaChon == 2 && sp instanceof DungCuTheThao)) {
                    System.out.println(sp.hienThiThongTin());
                    System.out.println("------------------------------");
                    coSanPham = true;
                }
            }
            if (!coSanPham) {
                System.out.println("Chua co san pham trong nhom nay.");
            }
        }
    }

    public static void hienThiMenu() {
        System.out.println("\n===== QUAN LY CUA HANG DUNG CU THE THAO =====");
        System.out.println("1. Quan ly san pham");
        System.out.println("2. Quan ly loai san pham");
        System.out.println("3. Quan ly khach hang");
        System.out.println("4. Quan ly nhan vien");
        System.out.println("5. Quan ly nha cung cap");
        System.out.println("6. Quan ly hoa don");
        System.out.println("7. Quan ly phieu nhap");
        System.out.println("0. Thoat");
        System.out.println("============================================");
    }
}
