package quanly;

import java.time.LocalDate;

public class HeThong {
    // Biến đa hình: Khai báo kiểu lớp cha QuanLyCuaHang
    private QuanLyCuaHang quanLy;

    public void menuChinh() {
        // Khi khởi động hệ thống, nạp dữ liệu thật
        QuanLyCuaHang.docDuLieu();

        int chon;
        do {
            System.out.println("\n===== HE THONG QUAN LY CUA HANG DUNG CU THE THAO =====");
            System.out.println("1. Quan ly San pham");
            System.out.println("2. Quan ly Loai san pham");
            System.out.println("3. Quan ly Khach hang");
            System.out.println("4. Quan ly Nhan vien");
            System.out.println("5. Quan ly Nha cung cap");
            System.out.println("6. Quan ly Hoa don");
            System.out.println("7. Quan ly Phieu nhap");
            System.out.println("8. Tra cuu ton kho");
            System.out.println("9. Thong ke doanh thu");
            System.out.println("10. Luu du lieu vao file");
            System.out.println("0. Thoat chuong trinh");
            System.out.print("Chon: ");
            try {
                chon = Integer.parseInt(QuanLyCuaHang.sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Vui long nhap so nguyen hop le!");
                chon = -1;
                continue;
            }

            // ĐA HÌNH LÚC CHẠY (Run-time Polymorphism)
            switch (chon) {
                case 1 -> { quanLy = new QuanLySanPham(); quanLy.menuChinh(); }
                case 2 -> { quanLy = new QuanLyLoaiSP(); quanLy.menuChinh(); }
                case 3 -> { quanLy = new QuanLyKhachHang(); quanLy.menuChinh(); }
                case 4 -> { quanLy = new QuanLyNhanVien(); quanLy.menuChinh(); }
                case 5 -> { quanLy = new QuanLyNhaCungCap(); quanLy.menuChinh(); }
                case 6 -> { quanLy = new QuanLyHoaDon(); quanLy.menuChinh(); }
                case 7 -> { quanLy = new QuanLyPhieuNhap(); quanLy.menuChinh(); }
                case 8 -> menuTraCuuTon();
                case 9 -> menuThongKe();
                case 10 -> QuanLyCuaHang.ghiDuLieu();
                case 0 -> {
                    // Trước khi thoát, tự động ghi dữ liệu
                    QuanLyCuaHang.ghiDuLieu();
                    System.out.println("Thoat chuong trinh. Tam biet!");
                }
                default -> System.out.println("Lua chon khong hop le.");
            }
        } while (chon != 0);
    }

    private void menuTraCuuTon() {
        System.out.print("Nhap ma san pham can tra cuu ton: ");
        String ma = QuanLyCuaHang.sc.nextLine().trim();
        QuanLyCuaHang.traCuuTonKho(ma);
    }

    private void menuThongKe() {
        System.out.println("\n=== MENU THONG KE DOANH THU ===");
        System.out.println("1. Thong ke tong tien toan bo");
        System.out.println("2. Thong ke tong tien theo khoang ngay");
        System.out.print("Chon: ");
        try {
            int opt = Integer.parseInt(QuanLyCuaHang.sc.nextLine().trim());
            if (opt == 1) {
                QuanLyCuaHang.thongKeTongTien();
            } else if (opt == 2) {
                System.out.print("Nhap ngay bat dau (yyyy-MM-dd): ");
                LocalDate tu = LocalDate.parse(QuanLyCuaHang.sc.nextLine().trim());
                System.out.print("Nhap ngay ket thuc (yyyy-MM-dd): ");
                LocalDate den = LocalDate.parse(QuanLyCuaHang.sc.nextLine().trim());
                QuanLyCuaHang.thongKeTheoKhoang(tu, den);
            } else {
                System.out.println("Lua chon khong dung!");
            }
        } catch (Exception e) {
            System.out.println("Loi nhap lieu thong ke: " + e.getMessage());
        }
    }
}
