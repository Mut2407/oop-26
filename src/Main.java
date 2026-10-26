import java.util.Scanner;
import models.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int luaChon = 0;
        SanPham[] danhSachSanPham = {
            new DungCuTheThao(
                "SP01", "Vot cau long", 350000, "Yonex",
                10, "DC", "cau long", "Carbon", 0.085
            ),
            new DungCuTheThao(
                "SP02", "Bong da", 250000, "Dong Luc",
                20, "DC", "Bong da", "Da tong hop", 0.45
            )
        };
        do {
            hienThiMenu();

            System.out.print("Nhap lua chon: ");

            if (!sc.hasNextInt()) {
                System.out.println("Vui long nhap mot so!");
                sc.nextLine();
                continue;
            }

            luaChon = sc.nextInt();
            sc.nextLine();

            switch (luaChon) {
                case 1:
                    System.out.println("===== DANH SACH SAN PHAM =====");
                    for (SanPham sp : danhSachSanPham) {
                        System.out.println(sp); 
                        System.out.println(sp.hienThiThongTin()); 
                        System.out.println("------------------------------");
                    }
                    break;

                case 2:
                    System.out.println("Quan ly loai san pham");
                    break;

                case 3:
                    System.out.println("Quan ly khach hang");
                    break;

                case 4:
                    System.out.println("Quan ly nhan vien");
                    break;

                case 5:
                    System.out.println("Quan ly nha cung cap");
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

        sc.close();
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
