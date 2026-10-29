import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import models.*;
import services.DanhSachKhachHang;
import services.LuuTruFile;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final String TEP_KHACH_HANG =
            "data/khachhang.txt";

    public static void main(String[] args) {
        SanPham[] danhSachSanPham =
                DuLieu.taoDanhSachSanPham();

        DanhSachKhachHang danhSachKhachHang =
                new DanhSachKhachHang(
                        DuLieu.taoDanhSachKhachHang());

        if (Files.exists(Path.of(TEP_KHACH_HANG))) {
            try {
                danhSachKhachHang.docFile(TEP_KHACH_HANG);
                System.out.println(
                        "Da doc danh sach khach hang tu file.");
            } catch (IOException e) {
                System.out.println(
                        "Khong doc duoc file: " + e.getMessage());
                System.out.println("Dang su dung du lieu mau.");
            }
        }

        NhanVien[] danhSachNhanVien =
                DuLieu.taoDanhSachNhanVien();

        NhaCungCap[] danhSachNhaCungCap =
                DuLieu.taoDanhSachNhaCungCap();

        xuLyMenu(
                danhSachSanPham,
                danhSachKhachHang,
                danhSachNhanVien,
                danhSachNhaCungCap
        );

        sc.close();
    }

    private static void xuLyMenu(
            SanPham[] danhSachSanPham,
            DanhSachKhachHang danhSachKhachHang,
            NhanVien[] danhSachNhanVien,
            NhaCungCap[] danhSachNhaCungCap) {

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
                    quanLyKhachHang(danhSachKhachHang);
                    break;

                case 4:
                    System.out.println(
                            "===== DANH SACH NHAN VIEN =====");

                    for (NhanVien nv : danhSachNhanVien) {
                        System.out.println(nv.thongTinNhanVien());
                        System.out.println(
                                "------------------------------");
                    }
                    break;

                case 5:
                    System.out.println(
                            "===== DANH SACH NHA CUNG CAP =====");

                    for (NhaCungCap ncc : danhSachNhaCungCap) {
                        System.out.println(ncc.thongTinNhaCungCap());
                        System.out.println(
                                "------------------------------");
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

    private static void quanLyKhachHang(
            DanhSachKhachHang danhSach) {

        while (true) {
            System.out.println(
                    "\n===== QUAN LY KHACH HANG =====");
            System.out.println("1. Xem danh sach khach hang");
            System.out.println("2. Them 1 khach hang");
            System.out.println("3. Sua theo ma");
            System.out.println("4. Xoa theo ma");
            System.out.println("5. Nhap danh sach moi gom n khach hang");
            System.out.println("6. Them k khach hang");
            System.out.println("7. Tim kiem theo nhieu khoa");
            System.out.println("8. Thong ke");
            System.out.println("9. Doc lai tu file (thay danh sach hien tai)");
            System.out.println("10. Luu vao file");
            System.out.println("0. Quay lai");

            String luaChon = docDong("Nhap lua chon: ");

            if (luaChon == null) {
                return;
            }

            try {
                switch (luaChon) {
                    case "1":
                        hienThiDanhSachKhachHang(
                                danhSach.getDanhSach());
                        break;

                    case "2":
                        KhachHang kh = nhapKhachHang(danhSach);

                        if (kh != null) {
                            danhSach.them(kh);
                            System.out.println(
                                    "Them khach hang thanh cong!");
                        }
                        break;

                    case "3":
                        suaKhachHang(danhSach);
                        break;

                    case "4":
                        xoaKhachHang(danhSach);
                        break;

                    case "5":
                        nhapNhieuKhachHang(danhSach, true);
                        break;

                    case "6":
                        nhapNhieuKhachHang(danhSach, false);
                        break;

                    case "7":
                        timKiemKhachHang(danhSach);
                        break;

                    case "8":
                        System.out.println(danhSach.thongKe());
                        break;

                    case "9":
                        xuLyFile(danhSach, true);
                        break;

                    case "10":
                        xuLyFile(danhSach, false);
                        break;

                    case "0":
                        return;

                    default:
                        System.out.println(
                                "Lua chon khong hop le!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void hienThiDanhSachKhachHang(
            KhachHang[] danhSach) {

        System.out.println(
                "===== DANH SACH KHACH HANG =====");

        if (danhSach.length == 0) {
            System.out.println(
                    "Danh sach khach hang dang trong.");
            return;
        }

        for (KhachHang kh : danhSach) {
            System.out.println(kh.thongTinKhachHang());
            System.out.println("------------------------------");
        }

        System.out.println(
                "Tong so khach hang: " + danhSach.length);
    }

    private static KhachHang nhapKhachHang(
            DanhSachKhachHang danhSach) {

        String ma;

        while (true) {
            ma = nhapBatBuoc("Nhap ma khach hang: ");

            if (ma == null) {
                return null;
            }

            if (!DanhSachKhachHang.maHopLe(ma)) {
                System.out.println(
                        "Ma chi gom chu, so, dau _ hoac -.");
                continue;
            }

            if (danhSach.timTheoMa(ma) != null) {
                System.out.println(
                        "Ma khach hang da ton tai! "
                        + "Vui long nhap ma khac.");
                continue;
            }

            break;
        }

        String hoTen = nhapBatBuoc("Nhap ho ten: ");
        if (hoTen == null) {
            return null;
        }

        String soDienThoai = nhapSoDienThoai(
                "Nhap so dien thoai: ", false);
        if (soDienThoai == null) {
            return null;
        }

        String diaChi = nhapBatBuoc("Nhap dia chi: ");
        if (diaChi == null) {
            return null;
        }

        return new KhachHang(
                ma, hoTen, soDienThoai, diaChi);
    }

    private static void nhapNhieuKhachHang(
            DanhSachKhachHang danhSach,
            boolean thayMoi) {

        if (thayMoi) {
            System.out.println(
                    "Danh sach nhap moi se thay the danh sach "
                    + "hien tai sau khi nhap xong.");
        }

        Integer n = nhapSoNguyen(
                thayMoi ? "Nhap n: " : "Nhap k: ",
                thayMoi ? 0 : 1
        );

        if (n == null) {
            return;
        }

        DanhSachKhachHang kiemTra = thayMoi
                ? new DanhSachKhachHang()
                : new DanhSachKhachHang(
                        danhSach.getDanhSach());

        KhachHang[] moi = new KhachHang[n];

        for (int i = 0; i < n; i++) {
            System.out.println(
                    "--- Khach hang "
                    + (i + 1) + "/" + n + " ---");

            moi[i] = nhapKhachHang(kiemTra);

            if (moi[i] == null) {
                return;
            }

            kiemTra.them(moi[i]);
        }

        if (thayMoi) {
            danhSach.nhapDanhSach(moi);
        } else {
            danhSach.themNhieu(moi);
        }

        System.out.println(
                "Da " + (thayMoi ? "nhap moi " : "them ")
                + n + " khach hang.");
    }

    private static void suaKhachHang(
            DanhSachKhachHang danhSach) {

        String ma = nhapBatBuoc(
                "Nhap ma khach hang can sua: ");

        if (ma == null) {
            return;
        }

        KhachHang kh = danhSach.timTheoMa(ma);

        if (kh == null) {
            System.out.println(
                    "Khong tim thay khach hang!");
            return;
        }

        System.out.println(kh.thongTinKhachHang());
        System.out.println(
                "Giu nguyen ma khach hang. "
                + "Nhan Enter de giu thong tin cu.");

        String hoTen = docDong("Ho ten moi: ");
        if (hoTen == null) {
            return;
        }

        String soDienThoai = nhapSoDienThoai(
                "So dien thoai moi: ", true);
        if (soDienThoai == null) {
            return;
        }

        String diaChi = docDong("Dia chi moi: ");
        if (diaChi == null) {
            return;
        }

        danhSach.sua(
                ma,
                hoTen.isEmpty()
                        ? kh.getHoTen() : hoTen,
                soDienThoai.isEmpty()
                        ? kh.getSoDienThoai() : soDienThoai,
                diaChi.isEmpty()
                        ? kh.getDiaChi() : diaChi
        );

        System.out.println(
                "Cap nhat khach hang thanh cong!");
    }

    private static void xoaKhachHang(
            DanhSachKhachHang danhSach) {

        String ma = nhapBatBuoc(
                "Nhap ma khach hang can xoa: ");

        if (ma != null) {
            System.out.println(
                    danhSach.xoa(ma)
                            ? "Xoa khach hang thanh cong!"
                            : "Khong tim thay khach hang!"
            );
        }
    }

    private static void timKiemKhachHang(
            DanhSachKhachHang danhSach) {

        System.out.println(
                "1. Chinh xac (khong phan biet hoa/thuong)");
        System.out.println(
                "2. Gan dung (chua tu khoa, "
                + "khong phan biet hoa/thuong)");

        String kieu = docDong("Chon kieu tim: ");

        if (kieu == null) {
            return;
        }

        if (!kieu.equals("1") && !kieu.equals("2")) {
            System.out.println("Lua chon khong hop le!");
            return;
        }

        System.out.println(
                "Bo trong khoa khong can loc; khach hang "
                + "phai khop tat ca khoa da nhap.");

        String ma = docDong("Ma khach hang: ");
        if (ma == null) {
            return;
        }

        String hoTen = docDong("Ho ten: ");
        if (hoTen == null) {
            return;
        }

        String soDienThoai = docDong("So dien thoai: ");
        if (soDienThoai == null) {
            return;
        }

        String diaChi = docDong("Dia chi: ");
        if (diaChi == null) {
            return;
        }

        KhachHang[] ketQua = danhSach.timKiem(
                ma,
                hoTen,
                soDienThoai,
                diaChi,
                kieu.equals("1")
        );

        hienThiDanhSachKhachHang(ketQua);
    }

    // Su dung interface cho chuc nang doc/ghi file.
    private static void xuLyFile(
            LuuTruFile danhSach, boolean doc) {

        try {
            if (doc) {
                danhSach.docFile(TEP_KHACH_HANG);
            } else {
                danhSach.ghiFile(TEP_KHACH_HANG);
            }

            System.out.println(
                    (doc ? "Doc" : "Luu")
                    + " file thanh cong: " + TEP_KHACH_HANG);

        } catch (IOException e) {
            System.out.println(
                    "Khong " + (doc ? "doc" : "luu")
                    + " duoc file: " + e.getMessage());
        }
    }

    private static String docDong(String thongBao) {
        System.out.print(thongBao);

        return sc.hasNextLine()
                ? sc.nextLine().trim()
                : null;
    }

    private static String nhapBatBuoc(String thongBao) {
        while (true) {
            String giaTri = docDong(thongBao);

            if (giaTri == null || !giaTri.isEmpty()) {
                return giaTri;
            }

            System.out.println(
                    "Thong tin khong duoc de trong!");
        }
    }

    private static Integer nhapSoNguyen(
            String thongBao, int nhoNhat) {

        while (true) {
            String giaTri = docDong(thongBao);

            if (giaTri == null) {
                return null;
            }

            try {
                int so = Integer.parseInt(giaTri);

                if (so >= nhoNhat) {
                    return so;
                }
            } catch (NumberFormatException e) {
                // Bao loi va cho nhap lai.
            }

            System.out.println(
                    "Vui long nhap so nguyen >= "
                    + nhoNhat + ".");
        }
    }

    private static String nhapSoDienThoai(
            String thongBao, boolean choPhepBoQua) {

        while (true) {
            String giaTri = docDong(thongBao);

            if (giaTri == null
                    || (choPhepBoQua && giaTri.isEmpty())) {
                return giaTri;
            }

            if (DanhSachKhachHang.soDienThoaiHopLe(giaTri)) {
                return giaTri;
            }

            System.out.println(
                    "So dien thoai phai co 9-15 chu so, "
                    + "co the bat dau bang +.");
        }
    }

    private static void quanLySanPham(
            SanPham[] danhSachSanPham) {

        while (true) {
            System.out.println(
                    "\n===== QUAN LY SAN PHAM =====");
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
                luaChon = Integer.parseInt(
                        sc.nextLine().trim());
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

            System.out.println(
                    "===== DANH SACH SAN PHAM =====");

            boolean coSanPham = false;

            for (SanPham sp : danhSachSanPham) {
                if (luaChon == 3
                        || (luaChon == 1
                            && sp instanceof GiayTheThao)
                        || (luaChon == 2
                            && sp instanceof DungCuTheThao)) {

                    System.out.println(sp.hienThiThongTin());
                    System.out.println(
                            "------------------------------");

                    coSanPham = true;
                }
            }

            if (!coSanPham) {
                System.out.println(
                        "Chua co san pham trong nhom nay.");
            }
        }
    }

    public static void hienThiMenu() {
        System.out.println(
                "\n===== QUAN LY CUA HANG DUNG CU THE THAO =====");
        System.out.println("1. Quan ly san pham");
        System.out.println("2. Quan ly loai san pham");
        System.out.println("3. Quan ly khach hang");
        System.out.println("4. Quan ly nhan vien");
        System.out.println("5. Quan ly nha cung cap");
        System.out.println("6. Quan ly hoa don");
        System.out.println("7. Quan ly phieu nhap");
        System.out.println("0. Thoat");
        System.out.println(
                "============================================");
    }
}