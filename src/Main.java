import java.util.Scanner;
import models.*;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int luaChon = 0;

        do {
            hienThiMenu();

            System.out.print("Nhập lựa chọn: ");

            if (!sc.hasNextInt()) {
                System.out.println("Vui lòng nhập một số!");
                sc.nextLine();
                continue;
            }

            luaChon = sc.nextInt();
            sc.nextLine();

            switch (luaChon) {
                case 1:
                    System.out.println("Quản lý sản phẩm");
                    break;

                case 2:
                    System.out.println("Quản lý loại sản phẩm");
                    break;

                case 3:
                    System.out.println("Quản lý khách hàng");
                    break;

                case 4:
                    System.out.println("Quản lý nhân viên");
                    break;

                case 5:
                    System.out.println("Quản lý nhà cung cấp");
                    break;

                case 6:
                    System.out.println("Quản lý hóa đơn");
                    break;

                case 7:
                    System.out.println("Quản lý phiếu nhập");
                    break;

                case 0:
                    System.out.println("Thoát chương trình!");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
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
