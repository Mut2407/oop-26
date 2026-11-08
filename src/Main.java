/*
 * Decompiled with CFR 0.152.
 */
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import models.DanhMuc;
import models.DuLieu;
import models.DungCuTheThao;
import models.GiayTheThao;
import models.KhachHang;
import models.NhaCungCap;
import models.QuanAoTheThao;
import models.SanPham;
import services.DanhSachKhachHang;
import services.DanhSachNhaCungCap;
import services.DanhSachNhanVien;
import services.DanhSachSanPham;
import services.LuuTruFileText;
import services.VanBanConsole;
import services.MenuHoaDon;
import services.MenuPhieuNhap;
import services.GiaoDichBanHang;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final String TEP_KHACH_HANG = "khachhang.txt";
    private static final String TEP_DANH_MUC = "danhmuc.txt";
    private static final String TEP_SAN_PHAM = "sanpham.txt";
    private static final String TEP_NHA_CUNG_CAP = "nhacungcap.txt";
    private static final String TEP_NHAN_VIEN = "nhanvien.txt";
    private static final LuuTruFileText LUU_TRU = new LuuTruFileText("data");

    public static void main(String[] args) {
        Main.chay();
    }

    private static void chay() {
        MenuHoaDon hoaDon;
        MenuPhieuNhap phieuNhap;
        DanhSachNhaCungCap danhSachNhaCungCap;
        DanhSachSanPham danhSachSanPham;
        DuLieu.khoiTaoDanhMuc();
        DanhSachKhachHang danhSachKhachHang = new DanhSachKhachHang(DuLieu.taoDanhSachKhachHang());
        DanhSachNhanVien danhSachNhanVien = new DanhSachNhanVien(DuLieu.taoDanhSachNhanVien(), DuLieu.taoDiaChiNhanVienCu());
        try {
            GiaoDichBanHang.phucHoi(Path.of("data"));
            if (tonTaiDuLieu("hoadon.txt") && (!tonTaiDuLieu(TEP_KHACH_HANG)
                    || !tonTaiDuLieu(TEP_NHAN_VIEN) || !tonTaiDuLieu(TEP_SAN_PHAM) || !tonTaiDuLieu(TEP_DANH_MUC))) {
                throw new IOException("Thieu file nguon cua hoa don; khong tao du lieu mau thay the.");
            }
            if (tonTaiDuLieu("phieunhap.txt") && (!tonTaiDuLieu(TEP_NHA_CUNG_CAP)
                    || !tonTaiDuLieu(TEP_NHAN_VIEN) || !tonTaiDuLieu(TEP_SAN_PHAM) || !tonTaiDuLieu(TEP_DANH_MUC))) {
                throw new IOException("Thieu file nguon cua phieu nhap; khong tao du lieu mau.");
            }
            if (Main.tonTaiDuLieu(TEP_DANH_MUC)) {
                Main.napDanhMuc();
            }
            danhSachSanPham = Main.tonTaiDuLieu(TEP_SAN_PHAM) ? LUU_TRU.docSanPham(TEP_SAN_PHAM, DuLieu.getDanhMuc()) : new DanhSachSanPham(DuLieu.taoDanhSachSanPham());
            danhSachNhaCungCap = Main.tonTaiDuLieu(TEP_NHA_CUNG_CAP) ? LUU_TRU.docNhaCungCap(TEP_NHA_CUNG_CAP) : new DanhSachNhaCungCap(DuLieu.taoDanhSachNhaCungCap());
            if (tonTaiDuLieu(TEP_KHACH_HANG)) {
                danhSachKhachHang = LUU_TRU.docKhachHang(TEP_KHACH_HANG);
            }
            if (tonTaiDuLieu(TEP_NHAN_VIEN)) {
                danhSachNhanVien = DanhSachNhanVien.tuDanhSach(LUU_TRU.docNhanVien(TEP_NHAN_VIEN));
            }
            hoaDon = new MenuHoaDon(danhSachSanPham, danhSachKhachHang, danhSachNhanVien, LUU_TRU, Path.of("data"));
            phieuNhap = new MenuPhieuNhap(danhSachSanPham, danhSachNhaCungCap, danhSachNhanVien, LUU_TRU, Path.of("data"));
            // Validate all existing data before creating any missing files.
            if (!tonTaiDuLieu(TEP_KHACH_HANG)) {
                LUU_TRU.ghiKhachHang(TEP_KHACH_HANG, danhSachKhachHang);
            }
            if (!tonTaiDuLieu(TEP_NHAN_VIEN)) {
                LUU_TRU.ghiNhanVien(TEP_NHAN_VIEN, danhSachNhanVien.getTatCa());
            }
            if (!Main.tonTaiDuLieu(TEP_DANH_MUC)) {
                LUU_TRU.ghiDanhMuc(TEP_DANH_MUC, DuLieu.getDanhMuc());
            }
            if (!Main.tonTaiDuLieu(TEP_SAN_PHAM)) {
                LUU_TRU.ghiSanPham(TEP_SAN_PHAM, danhSachSanPham);
            }
            if (!Main.tonTaiDuLieu(TEP_NHA_CUNG_CAP)) {
                LUU_TRU.ghiNhaCungCap(TEP_NHA_CUNG_CAP, danhSachNhaCungCap);
            }
        }
        catch (IOException | IllegalArgumentException exception) {
            System.err.println("Khong the khoi dong: " + exception.getMessage());
            System.err.println("Hay sua file du lieu; chuong trinh chua ghi de len file loi.");
            sc.close();
            System.exit(1);
            return;
        }
        Main.xuLyMenu(danhSachSanPham, danhSachKhachHang, danhSachNhanVien, danhSachNhaCungCap, hoaDon, phieuNhap);
        sc.close();
    }

    private static boolean tonTaiDuLieu(String string) {
        return Files.exists(Path.of("data", string), new LinkOption[0]);
    }

    private static void napDanhMuc() throws IOException {
        for (DanhMuc danhMuc : LUU_TRU.docDanhMuc(TEP_DANH_MUC)) {
            DanhMuc danhMuc2 = DuLieu.timDanhMuc(danhMuc.getMaDanhMuc());
            if (danhMuc2 == null) {
                DuLieu.dangKyDanhMuc(danhMuc);
                continue;
            }
            danhMuc2.capNhatThongTin(danhMuc.getTenDanhMuc(), danhMuc.getMoTa());
        }
    }

    private static void tuLuu(DanhSachKhachHang ds, String string) {
        try {
            LUU_TRU.ghiKhachHang(string, ds);
        }
        catch (IOException iOException) {
            System.err.println("CHUA LUU DUOC " + string + ": " + iOException.getMessage());
            System.err.println("Du lieu moi chi dang trong bo nho. Hay thu luu lai truoc khi thoat.");
        }
    }

    private static void xuLyMenu(DanhSachSanPham danhSachSanPham, DanhSachKhachHang danhSachKhachHang, DanhSachNhanVien danhSachNhanVien, DanhSachNhaCungCap danhSachNhaCungCap, MenuHoaDon hoaDon, MenuPhieuNhap phieuNhap) {
        int n = -1;
        block14: do {
            Main.hienThiMenu();
            System.out.print("Nhap lua chon: ");
            if (!sc.hasNextLine()) {
                hoaDon.nhacChuaLuu();
                phieuNhap.nhacChuaLuu();
                return;
            }
            if (!sc.hasNextInt()) {
                System.out.println("Vui long nhap mot so!");
                sc.nextLine();
                continue;
            }
            n = sc.nextInt();
            sc.nextLine();
            switch (n) {
                case 1: {
                    Main.quanLySanPham(danhSachSanPham);
                    break;
                }
                case 2: {
                    Main.quanLyDanhMuc();
                    break;
                }
                case 3: {
                    Main.quanLyKhachHang(danhSachKhachHang);
                    break;
                }
                case 4: {
                    danhSachNhanVien.quanLy(sc, LUU_TRU, TEP_NHAN_VIEN);
                    break;
                }
                case 5: {
                    Main.quanLyNhaCungCap(danhSachNhaCungCap);
                    break;
                }
                case 6: {
                    hoaDon.chay(sc);
                    break;
                }
                case 7: {
                    phieuNhap.chay(sc);
                    break;
                }
                case 8: {
                    System.out.println("Bao cao doanh thu chua trien khai; thong ke san pham o menu san pham.");
                    break;
                }
                case 9: {
                    String string = Main.docDong("1. Luu danh muc/san pham/NCC  2. Doc lai danh muc/san pham/NCC: ");
                    try {
                        if ("1".equals(string)) {
                            LUU_TRU.ghiDanhMuc(TEP_DANH_MUC, DuLieu.getDanhMuc());
                            LUU_TRU.ghiSanPham(TEP_SAN_PHAM, danhSachSanPham);
                            LUU_TRU.ghiNhaCungCap(TEP_NHA_CUNG_CAP, danhSachNhaCungCap);
                            System.out.println("Da luu danh muc, san pham va nha cung cap.");
                            break;
                        }
                        if ("2".equals(string)) {
                            Main.napDanhMuc();
                            DanhSachSanPham danhSachSanPham2 = LUU_TRU.docSanPham(TEP_SAN_PHAM, DuLieu.getDanhMuc());
                            DanhSachNhaCungCap danhSachNhaCungCap2 = LUU_TRU.docNhaCungCap(TEP_NHA_CUNG_CAP);
                            danhSachSanPham.thayDuLieu(danhSachSanPham2);
                            danhSachNhaCungCap.thayDuLieu(danhSachNhaCungCap2);
                            System.out.println("Da doc lai danh muc, san pham va nha cung cap.");
                            break;
                        }
                        System.out.println("Lua chon khong hop le!");
                    }
                    catch (IOException | IllegalArgumentException exception) {
                        System.out.println("Khong the luu/doc du lieu: " + exception.getMessage());
                    }
                    continue block14;
                }
                case 0: {
                    hoaDon.nhacChuaLuu();
                    phieuNhap.nhacChuaLuu();
                    System.out.println("Thoat chuong trinh!");
                    break;
                }
                default: {
                    System.out.println("Lua chon khong hop le!");
                }
            }
        } while (n != 0);
    }

    private static void quanLyDanhMuc() {
        while (true) {
            System.out.println("\n1. Xem tat ca");
            System.out.println("2. Them danh muc");
            System.out.println("3. Sua danh muc");
            System.out.println("4. Tim theo ma");
            System.out.println("0. Quay lai");
            System.out.println("===== QUAN LY DANH MUC =====");
            String string = Main.docDong("Nhap lua chon: ");
            if (string == null || string.equals("0")) {
                return;
            }
            try {
                switch (string) {
                    case "1": {
                        for (DanhMuc danhMuc : DuLieu.getDanhMuc()) {
                            System.out.println(VanBanConsole.khongDau(danhMuc.hienThiThongTin()) + "\n------------------------------");
                        }
                        System.out.println("Tong so danh muc: " + DuLieu.getDanhMuc().size());
                        break;
                    }
                    case "2": {
                        Object object = Main.nhapBatBuoc("Ma danh muc: ");
                        if (object == null) {
                            return;
                        }
                        String string2 = Main.nhapBatBuoc("Ten danh muc: ");
                        if (string2 == null) {
                            return;
                        }
                        String string3 = Main.docDong("Mo ta (co the bo trong): ");
                        if (string3 == null) {
                            return;
                        }
                        DuLieu.dangKyDanhMuc(new DanhMuc((String)object, string2, string3));
                        LUU_TRU.ghiDanhMuc(TEP_DANH_MUC, DuLieu.getDanhMuc());
                        System.out.println("Da them danh muc.");
                        break;
                    }
                    case "3": {
                        Object object = Main.nhapBatBuoc("Ma danh muc can sua: ");
                        if (object == null) {
                            return;
                        }
                        DanhMuc danhMuc = DuLieu.timDanhMuc((String)object);
                        if (danhMuc == null) {
                            System.out.println("Khong tim thay.");
                            break;
                        }
                        String string3 = Main.nhapBatBuoc("Ten moi: ");
                        if (string3 == null) {
                            return;
                        }
                        String string4 = Main.docDong("Mo ta moi (co the bo trong): ");
                        if (string4 == null) {
                            return;
                        }
                        danhMuc.capNhatThongTin(string3, string4);
                        LUU_TRU.ghiDanhMuc(TEP_DANH_MUC, DuLieu.getDanhMuc());
                        System.out.println("Da sua danh muc.");
                        break;
                    }
                    case "4": {
                        Object object = Main.nhapBatBuoc("Ma danh muc can tim: ");
                        if (object == null) {
                            return;
                        }
                        DanhMuc danhMuc = DuLieu.timDanhMuc((String)object);
                        System.out.println(danhMuc == null ? "Khong tim thay." : VanBanConsole.khongDau(danhMuc.hienThiThongTin()));
                        break;
                    }
                    default: {
                        System.out.println("Lua chon khong hop le!");
                        break;
                    }
                }
                continue;
            }
            catch (IOException | IllegalArgumentException exception) {
                System.out.println("Khong the thuc hien: " + exception.getMessage());
                continue;
            }
        }
    }

    private static void quanLyKhachHang(DanhSachKhachHang danhSachKhachHang) {
        block28: while (true) {
            System.out.println("\n1. Xem danh sach khach hang");
            System.out.println("2. Them 1 khach hang");
            System.out.println("3. Sua theo ma");
            System.out.println("4. Xoa theo ma");
            System.out.println("5. Nhap danh sach moi gom n khach hang");
            System.out.println("6. Them k khach hang");
            System.out.println("7. Tim kiem theo nhieu khoa");
            System.out.println("8. Thong ke");
            System.out.println("9. Doc lai tu file (thay danh sach hien tai)");
            System.out.println("10. Luu vao file");
            System.out.println("11. Tim chinh xac theo ma");
            System.out.println("12. Tim gan dung theo ma/ten");
            System.out.println("13. Tim theo so dien thoai");
            System.out.println("0. Quay lai");
            System.out.println("===== QUAN LY KHACH HANG =====");
            String string = Main.docDong("Nhap lua chon: ");
            if (string == null) {
                return;
            }
            try {
                switch (string) {
                    case "1": {
                        Main.hienThiDanhSachKhachHang(danhSachKhachHang.getDanhSach());
                        break;
                    }
                    case "2": {
                        KhachHang khachHang = Main.nhapKhachHang(danhSachKhachHang);
                        if (khachHang == null) continue block28;
                        danhSachKhachHang.them(khachHang);
                        Main.tuLuu(danhSachKhachHang, TEP_KHACH_HANG);
                        System.out.println("Them khach hang thanh cong!");
                        break;
                    }
                    case "3": {
                        if (Main.suaKhachHang(danhSachKhachHang)) {
                            Main.tuLuu(danhSachKhachHang, TEP_KHACH_HANG);
                        }
                        break;
                    }
                    case "4": {
                        if (Main.xoaKhachHang(danhSachKhachHang)) {
                            Main.tuLuu(danhSachKhachHang, TEP_KHACH_HANG);
                        }
                        break;
                    }
                    case "5": {
                        if (Main.nhapNhieuKhachHang(danhSachKhachHang, true)) {
                            Main.tuLuu(danhSachKhachHang, TEP_KHACH_HANG);
                        }
                        break;
                    }
                    case "6": {
                        if (Main.nhapNhieuKhachHang(danhSachKhachHang, false)) {
                            Main.tuLuu(danhSachKhachHang, TEP_KHACH_HANG);
                        }
                        break;
                    }
                    case "7": {
                        Main.timKiemKhachHang(danhSachKhachHang);
                        break;
                    }
                    case "8": {
                        System.out.println(VanBanConsole.khongDau(danhSachKhachHang.thongKe()));
                        break;
                    }
                    case "9": {
                        Main.xuLyFile(danhSachKhachHang, true);
                        break;
                    }
                    case "10": {
                        Main.xuLyFile(danhSachKhachHang, false);
                        break;
                    }
                    case "11": {
                        String ma = nhapBatBuoc("Ma khach hang can tim: ");
                        if (ma == null) {
                            return;
                        }
                        KhachHang kh = danhSachKhachHang.timTheoMa(ma);
                        hienThiDanhSachKhachHang(kh == null ? new KhachHang[0] : new KhachHang[]{kh});
                        break;
                    }
                    case "12": {
                        String key = nhapBatBuoc("Tu khoa ma/ten: ");
                        if (key == null) {
                            return;
                        }
                        hienThiDanhSachKhachHang(danhSachKhachHang.timGanDung(key).toArray(new KhachHang[0]));
                        break;
                    }
                    case "13": {
                        String sdt = nhapSoDienThoai("So dien thoai can tim: ", false);
                        if (sdt == null) {
                            return;
                        }
                        hienThiDanhSachKhachHang(danhSachKhachHang.timTheoSoDienThoai(sdt).toArray(new KhachHang[0]));
                        break;
                    }
                    case "0": {
                        return;
                    }
                    default: {
                        System.out.println("Lua chon khong hop le!");
                        break;
                    }
                }
                continue;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println(illegalArgumentException.getMessage());
                continue;
            }
        }
    }

    private static void hienThiDanhSachKhachHang(KhachHang[] khachHangArray) {
        System.out.println("===== DANH SACH KHACH HANG =====");
        if (khachHangArray.length == 0) {
            System.out.println("Danh sach khach hang dang trong.");
            return;
        }
        for (KhachHang khachHang : khachHangArray) {
            System.out.println(VanBanConsole.khongDau(khachHang.hienThiThongTin()));
            System.out.println("------------------------------");
        }
        System.out.println("Tong so khach hang: " + khachHangArray.length);
    }

    private static KhachHang nhapKhachHang(DanhSachKhachHang danhSachKhachHang) {
        String string;
        while (true) {
            if ((string = Main.nhapBatBuoc("Nhap ma khach hang: ")) == null) {
                return null;
            }
            if (!DanhSachKhachHang.maHopLe(string)) {
                System.out.println("Ma chi gom chu, so, dau _ hoac -.");
                continue;
            }
            if (danhSachKhachHang.timTheoMa(string) == null) break;
            System.out.println("Ma khach hang da ton tai! Vui long nhap ma khac.");
        }
        String string2 = Main.nhapBatBuoc("Nhap ho ten: ");
        if (string2 == null) {
            return null;
        }
        String string3 = Main.nhapSoDienThoai("Nhap so dien thoai: ", false);
        if (string3 == null) {
            return null;
        }
        String string4 = Main.nhapBatBuoc("Nhap dia chi: ");
        if (string4 == null) {
            return null;
        }
        return new KhachHang(string, string2, string3, string4);
    }

    private static boolean nhapNhieuKhachHang(DanhSachKhachHang danhSachKhachHang, boolean bl) {
        Integer n;
        if (bl) {
            System.out.println("Danh sach nhap moi se thay the danh sach hien tai sau khi nhap xong.");
        }
        if ((n = Main.nhapSoNguyen(bl ? "Nhap n: " : "Nhap k: ", bl ? 0 : 1)) == null) {
            return false;
        }
        DanhSachKhachHang danhSachKhachHang2 = bl ? new DanhSachKhachHang() : new DanhSachKhachHang(danhSachKhachHang.getDanhSach());
        KhachHang[] khachHangArray = new KhachHang[n.intValue()];
        for (int i = 0; i < n; ++i) {
            System.out.println("--- Khach hang " + (i + 1) + "/" + n + " ---");
            khachHangArray[i] = Main.nhapKhachHang(danhSachKhachHang2);
            if (khachHangArray[i] == null) {
                return false;
            }
            danhSachKhachHang2.them(khachHangArray[i]);
        }
        if (bl) {
            danhSachKhachHang.nhapDanhSach(khachHangArray);
        } else {
            danhSachKhachHang.themNhieu(khachHangArray);
        }
        System.out.println("Da " + (bl ? "nhap moi " : "them ") + n + " khach hang.");
        return true;
    }

    private static boolean suaKhachHang(DanhSachKhachHang danhSachKhachHang) {
        String string = Main.nhapBatBuoc("Nhap ma khach hang can sua: ");
        if (string == null) {
            return false;
        }
        KhachHang khachHang = danhSachKhachHang.timTheoMa(string);
        if (khachHang == null) {
            System.out.println("Khong tim thay khach hang!");
            return false;
        }
        System.out.println(VanBanConsole.khongDau(khachHang.hienThiThongTin()));
        System.out.println("Giu nguyen ma khach hang. Nhan Enter de giu thong tin cu.");
        String string2 = Main.docDong("Ho ten moi: ");
        if (string2 == null) {
            return false;
        }
        String string3 = Main.nhapSoDienThoai("So dien thoai moi: ", true);
        if (string3 == null) {
            return false;
        }
        String string4 = Main.docDong("Dia chi moi: ");
        if (string4 == null) {
            return false;
        }
        danhSachKhachHang.sua(string, string2.isEmpty() ? khachHang.getHoTen() : string2, string3.isEmpty() ? khachHang.getSoDienThoai() : string3, string4.isEmpty() ? khachHang.getDiaChi() : string4);
        System.out.println("Cap nhat khach hang thanh cong!");
        return true;
    }

    private static boolean xoaKhachHang(DanhSachKhachHang danhSachKhachHang) {
        String string = Main.nhapBatBuoc("Nhap ma khach hang can xoa: ");
        if (string == null) {
            return false;
        }
        boolean xoa = danhSachKhachHang.xoaTheoMa(string);
        System.out.println(xoa ? "Xoa khach hang thanh cong!" : "Khong tim thay khach hang!");
        return xoa;
    }

    private static void timKiemKhachHang(DanhSachKhachHang danhSachKhachHang) {
        System.out.println("1. Chinh xac (khong phan biet hoa/thuong)");
        System.out.println("2. Gan dung (chua tu khoa, khong phan biet hoa/thuong)");
        String string = Main.docDong("Chon kieu tim: ");
        if (string == null) {
            return;
        }
        if (!string.equals("1") && !string.equals("2")) {
            System.out.println("Lua chon khong hop le!");
            return;
        }
        System.out.println("Bo trong khoa khong can loc; khach hang phai khop tat ca khoa da nhap.");
        String string2 = Main.docDong("Ma khach hang: ");
        if (string2 == null) {
            return;
        }
        String string3 = Main.docDong("Ho ten: ");
        if (string3 == null) {
            return;
        }
        String string4 = Main.docDong("So dien thoai: ");
        if (string4 == null) {
            return;
        }
        String string5 = Main.docDong("Dia chi: ");
        if (string5 == null) {
            return;
        }
        KhachHang[] khachHangArray = danhSachKhachHang.timKiem(string2, string3, string4, string5, string.equals("1"));
        Main.hienThiDanhSachKhachHang(khachHangArray);
    }

    private static void xuLyFile(DanhSachKhachHang ds, boolean bl) {
        try {
            if (bl) {
                ds.nhapDanhSach(LUU_TRU.docKhachHang(TEP_KHACH_HANG).getDanhSach());
            } else {
                LUU_TRU.ghiKhachHang(TEP_KHACH_HANG, ds);
            }
            System.out.println((bl ? "Doc" : "Luu") + " file thanh cong: data/khachhang.txt");
        }
        catch (IOException iOException) {
            System.out.println("Khong " + (bl ? "doc" : "luu") + " duoc file: " + iOException.getMessage());
        }
    }

    private static String docDong(String string) {
        System.out.print(string);
        return sc.hasNextLine() ? sc.nextLine().trim() : null;
    }

    private static String nhapBatBuoc(String string) {
        String string2;
        while ((string2 = Main.docDong(string)) != null && string2.isEmpty()) {
            System.out.println("Thong tin khong duoc de trong!");
        }
        return string2;
    }

    private static Integer nhapSoNguyen(String string, int n) {
        String string2;
        while ((string2 = Main.docDong(string)) != null) {
            try {
                int n2 = Integer.parseInt(string2);
                if (n2 >= n) {
                    return n2;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Vui long nhap so nguyen >= " + n + ".");
        }
        return null;
    }

    private static String nhapSoDienThoai(String string, boolean bl) {
        String string2;
        while (!((string2 = Main.docDong(string)) == null || bl && string2.isEmpty())) {
            if (DanhSachKhachHang.soDienThoaiHopLe(string2)) {
                return string2;
            }
            System.out.println("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        return string2;
    }

    private static void quanLySanPham(DanhSachSanPham danhSachSanPham) {
        block30: while (true) {
            System.out.println("\n1. Xem tat ca san pham");
            System.out.println("2. Khoi tao danh sach san pham");
            System.out.println("3. Them mot san pham");
            System.out.println("4. Them nhieu san pham");
            System.out.println("5. Sua san pham");
            System.out.println("6. Xoa san pham");
            System.out.println("7. Tim theo ma");
            System.out.println("8. Tim gan dung");
            System.out.println("9. Loc theo danh muc");
            System.out.println("10. Loc theo gia");
            System.out.println("11. Thong ke theo danh muc");
            System.out.println("12. San pham sap het hang");
            System.out.println("0. Quay lai");
            System.out.println("===== QUAN LY SAN PHAM =====");
            String string2 = Main.docDong("Nhap lua chon: ");
            if (string2 == null || string2.equals("0")) {
                return;
            }
            try {
                switch (string2) {
                    case "1": {
                        Main.hienThiSanPham(danhSachSanPham.getDanhSach());
                        break;
                    }
                    case "2": {
                        if (!Main.nhapNhieuSanPham(danhSachSanPham, true)) continue block30;
                        LUU_TRU.ghiSanPham(TEP_SAN_PHAM, danhSachSanPham);
                        break;
                    }
                    case "3": {
                        Object object = Main.nhapSanPham(null, danhSachSanPham);
                        if (object == null || !danhSachSanPham.them((SanPham)object)) continue block30;
                        LUU_TRU.ghiSanPham(TEP_SAN_PHAM, danhSachSanPham);
                        System.out.println("Da them san pham.");
                        break;
                    }
                    case "4": {
                        if (!Main.nhapNhieuSanPham(danhSachSanPham, false)) continue block30;
                        LUU_TRU.ghiSanPham(TEP_SAN_PHAM, danhSachSanPham);
                        break;
                    }
                    case "5": {
                        Object object = Main.nhapBatBuoc("Ma can sua: ");
                        if (object == null) {
                            return;
                        }
                        SanPham object2 = danhSachSanPham.timTheoMa((String)object);
                        if (object2 == null) {
                            System.out.println("Khong tim thay.");
                            break;
                        }
                        SanPham sanPham = Main.nhapSanPham(((SanPham)object2).getMaSanPham(), danhSachSanPham);
                        if (sanPham == null || !danhSachSanPham.suaTheoMa((String)object, sanPham)) continue block30;
                        LUU_TRU.ghiSanPham(TEP_SAN_PHAM, danhSachSanPham);
                        System.out.println("Da sua san pham.");
                        break;
                    }
                    case "6": {
                        Object object = Main.nhapBatBuoc("Ma can xoa: ");
                        if (object == null) {
                            return;
                        }
                        if (danhSachSanPham.xoaTheoMa((String)object)) {
                            LUU_TRU.ghiSanPham(TEP_SAN_PHAM, danhSachSanPham);
                            System.out.println("Da xoa san pham.");
                            break;
                        }
                        System.out.println("Khong tim thay.");
                        break;
                    }
                    case "7": {
                        Object object = Main.nhapBatBuoc("Ma san pham can tim: ");
                        if (object == null) {
                            return;
                        }
                        SanPham object2 = danhSachSanPham.timTheoMa((String)object);
                        Main.hienThiSanPham(object2 == null ? List.of() : List.of(object2));
                        break;
                    }
                    case "8": {
                        Object object = Main.nhapBatBuoc("Ma hoac ten: ");
                        if (object == null) {
                            return;
                        }
                        Main.hienThiSanPham(danhSachSanPham.timGanDung((String)object));
                        break;
                    }
                    case "9": {
                        Object object = Main.nhapBatBuoc("Ma danh muc: ");
                        if (object == null) {
                            return;
                        }
                        Main.hienThiSanPham(danhSachSanPham.locTheoDanhMuc((String)object));
                        break;
                    }
                    case "10": {
                        Object object = Main.nhapGia("Gia tu: ");
                        if (object == null) {
                            return;
                        }
                        Object object2 = Main.nhapGia("Gia den: ");
                        if (object2 == null) {
                            return;
                        }
                        Main.hienThiSanPham(danhSachSanPham.locTheoKhoangGia((BigDecimal)object, (BigDecimal)object2));
                        break;
                    }
                    case "11": {
                        danhSachSanPham.thongKeTheoDanhMuc().forEach((string, n) -> System.out.println(string + ": " + n + " san pham"));
                        break;
                    }
                    case "12": {
                        Object object = Main.nhapSoNguyen("Nguong ton kho: ", 0);
                        if (object == null) {
                            return;
                        }
                        Main.hienThiSanPham(danhSachSanPham.lietKeSapHetHang((Integer)object));
                        break;
                    }
                    default: {
                        System.out.println("Lua chon khong hop le!");
                        break;
                    }
                }
                continue;
            }
            catch (IOException | IllegalArgumentException exception) {
                System.out.println("Khong the thuc hien: " + exception.getMessage());
                continue;
            }
        }
    }

    private static boolean nhapNhieuSanPham(DanhSachSanPham danhSachSanPham, boolean bl) {
        Integer n = Main.nhapSoNguyen(bl ? "Nhap n: " : "Nhap k: ", bl ? 0 : 1);
        if (n == null) {
            return false;
        }
        DanhSachSanPham danhSachSanPham2 = bl ? new DanhSachSanPham(0) : new DanhSachSanPham(danhSachSanPham.getDanhSach().toArray(new SanPham[0]));
        SanPham[] sanPhamArray = new SanPham[n.intValue()];
        for (int i = 0; i < n; ++i) {
            System.out.println("--- San pham " + (i + 1) + "/" + n + " ---");
            sanPhamArray[i] = Main.nhapSanPham(null, danhSachSanPham2);
            if (sanPhamArray[i] == null) {
                return false;
            }
            danhSachSanPham2.them(sanPhamArray[i]);
        }
        if (bl) {
            danhSachSanPham.nhapBanDau(sanPhamArray);
        } else {
            danhSachSanPham.themNhieu(sanPhamArray);
        }
        System.out.println("Da " + (bl ? "nhap moi " : "them ") + n + " san pham.");
        return true;
    }

    private static void hienThiSanPham(List<SanPham> list) {
        if (list.isEmpty()) {
            System.out.println("Danh sach trong.");
        }
        for (SanPham sanPham : list) {
            System.out.println(VanBanConsole.khongDau(sanPham.hienThiThongTin()) + "\n------------------------------");
        }
        System.out.println("Tong so san pham: " + list.size());
    }

    private static SanPham nhapSanPham(String string, DanhSachSanPham danhSachSanPham) {
        DanhMuc danhMuc;
        String string2;
        String string3 = string;
        if (string3 == null) {
            while (true) {
                if ((string3 = Main.nhapBatBuoc("Ma san pham: ")) == null) {
                    return null;
                }
                if (!DanhSachKhachHang.maHopLe(string3)) {
                    System.out.println("Ma chi gom chu, so, dau _ hoac -.");
                    continue;
                }
                if (danhSachSanPham.timTheoMa(string3) == null) break;
                System.out.println("Ma san pham da ton tai. Nhap ma khac.");
            }
        }
        if ((string2 = Main.nhapBatBuoc("Ten san pham: ")) == null) {
            return null;
        }
        BigDecimal bigDecimal = Main.nhapGia();
        if (bigDecimal == null) {
            return null;
        }
        String string4 = Main.nhapBatBuoc("Thuong hieu: ");
        if (string4 == null) {
            return null;
        }
        Integer n = Main.nhapSoNguyen("So luong ton: ", 0);
        if (n == null) {
            return null;
        }
        System.out.println("Danh muc hien co:");
        for (DanhMuc object2 : DuLieu.getDanhMuc()) {
            System.out.println(object2.getMaDanhMuc() + " - " + VanBanConsole.khongDau(object2.getTenDanhMuc()));
        }
        while (true) {
            String string5;
            if ((string5 = Main.nhapBatBuoc("Ma danh muc: ")) == null) {
                return null;
            }
            danhMuc = DuLieu.timDanhMuc(string5);
            if (danhMuc != null) break;
            System.out.println("Ma danh muc chua ton tai: " + string5);
        }
        String string6 = Main.docDong("Mau sac (bo trong neu chua biet): ");
        if (string6 == null) {
            return null;
        }
        String string7 = Main.docDong("Chat lieu (bo trong neu chua biet): ");
        if (string7 == null) {
            return null;
        }
        String string8;
        while ((string8 = Main.docDong("Kieu (1=giay, 2=dung cu, 3=quan ao): ")) != null) {
            switch (string8) {
                case "1": {
                    String string9 = Main.nhapBatBuoc("Kich co giay: ");
                    if (string9 == null) {
                        return null;
                    }
                    String string10 = Main.nhapBatBuoc("Loai de giay: ");
                    return string10 == null ? null : new GiayTheThao(string3, string2, string4, bigDecimal, n, string6, string7, danhMuc, string9, string10);
                }
                case "2": {
                    Double d = Main.nhapSoThuc("Khoi luong (kg): ", 0.0);
                    return d == null ? null : new DungCuTheThao(string3, string2, string4, bigDecimal, n, string6, string7, danhMuc, d);
                }
                case "3": {
                    String string11 = Main.nhapBatBuoc("Kich co quan ao: ");
                    return string11 == null ? null : new QuanAoTheThao(string3, string2, string4, bigDecimal, n, string6, string7, danhMuc, string11);
                }
            }
            System.out.println("Kieu khong hop le.");
        }
        return null;
    }

    private static BigDecimal nhapGia() {
        return Main.nhapGia("Gia ban: ");
    }

    private static BigDecimal nhapGia(String string) {
        String string2;
        while ((string2 = Main.docDong(string)) != null) {
            try {
                BigDecimal bigDecimal = new BigDecimal(string2);
                if (bigDecimal.signum() >= 0) {
                    return bigDecimal;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Vui long nhap gia >= 0.");
        }
        return null;
    }

    private static Double nhapSoThuc(String string, double d) {
        String string2;
        while ((string2 = Main.docDong(string)) != null) {
            try {
                double d2 = Double.parseDouble(string2);
                if (Double.isFinite(d2) && d2 >= d) {
                    return d2;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            System.out.println("Vui long nhap so >= " + d + ".");
        }
        return null;
    }

    private static void quanLyNhaCungCap(DanhSachNhaCungCap danhSachNhaCungCap) {
        block20: while (true) {
            System.out.println("\n1. Xem tat ca nha cung cap");
            System.out.println("2. Them nha cung cap");
            System.out.println("3. Sua nha cung cap");
            System.out.println("4. Xoa nha cung cap");
            System.out.println("5. Tim gan dung");
            System.out.println("6. Tim theo ma");
            System.out.println("7. Luu lai");
            System.out.println("0. Quay lai");
            System.out.println("===== QUAN LY NHA CUNG CAP =====");
            String string = Main.docDong("Nhap lua chon: ");
            if (string == null || string.equals("0")) {
                return;
            }
            try {
                switch (string) {
                    case "1": {
                        Main.hienThiNhaCungCap(danhSachNhaCungCap.getTatCa());
                        break;
                    }
                    case "2": {
                        Object object = Main.nhapNhaCungCap(null, danhSachNhaCungCap);
                        if (object == null || !danhSachNhaCungCap.them((NhaCungCap)object)) continue block20;
                        LUU_TRU.ghiNhaCungCap(TEP_NHA_CUNG_CAP, danhSachNhaCungCap);
                        System.out.println("Da them nha cung cap.");
                        break;
                    }
                    case "3": {
                        Object object = Main.nhapBatBuoc("Ma can sua: ");
                        if (object == null) {
                            return;
                        }
                        NhaCungCap nhaCungCap = danhSachNhaCungCap.timTheoMa((String)object);
                        if (nhaCungCap == null) {
                            System.out.println("Khong tim thay.");
                            break;
                        }
                        NhaCungCap nhaCungCap2 = Main.nhapNhaCungCap(nhaCungCap.getMaNhaCungCap(), danhSachNhaCungCap);
                        if (nhaCungCap2 == null || !danhSachNhaCungCap.suaTheoMa((String)object, nhaCungCap2)) continue block20;
                        LUU_TRU.ghiNhaCungCap(TEP_NHA_CUNG_CAP, danhSachNhaCungCap);
                        System.out.println("Da sua nha cung cap.");
                        break;
                    }
                    case "4": {
                        Object object = Main.nhapBatBuoc("Ma can xoa: ");
                        if (object == null) {
                            return;
                        }
                        if (danhSachNhaCungCap.xoaTheoMa((String)object)) {
                            LUU_TRU.ghiNhaCungCap(TEP_NHA_CUNG_CAP, danhSachNhaCungCap);
                            System.out.println("Da xoa.");
                            break;
                        }
                        System.out.println("Khong tim thay.");
                        break;
                    }
                    case "5": {
                        Object object = Main.nhapBatBuoc("Ma hoac ten: ");
                        if (object == null) {
                            return;
                        }
                        Main.hienThiNhaCungCap(danhSachNhaCungCap.timGanDung((String)object));
                        break;
                    }
                    case "6": {
                        Object object = Main.nhapBatBuoc("Ma nha cung cap can tim: ");
                        if (object == null) {
                            return;
                        }
                        NhaCungCap nhaCungCap = danhSachNhaCungCap.timTheoMa((String)object);
                        if (nhaCungCap == null) {
                            System.out.println("Khong tim thay.");
                            break;
                        }
                        Main.hienThiNhaCungCap(List.of(nhaCungCap));
                        break;
                    }
                    case "7": {
                        LUU_TRU.ghiNhaCungCap(TEP_NHA_CUNG_CAP, danhSachNhaCungCap);
                        break;
                    }
                    default: {
                        System.out.println("Lua chon khong hop le!");
                        break;
                    }
                }
                continue;
            }
            catch (IOException | IllegalArgumentException exception) {
                System.out.println("Khong the thuc hien: " + exception.getMessage());
                continue;
            }
        }
    }

    private static NhaCungCap nhapNhaCungCap(String string, DanhSachNhaCungCap danhSachNhaCungCap) {
        String string2;
        String string3 = string;
        if (string3 == null) {
            while (true) {
                if ((string3 = Main.nhapBatBuoc("Ma nha cung cap: ")) == null) {
                    return null;
                }
                if (!DanhSachKhachHang.maHopLe(string3)) {
                    System.out.println("Ma chi gom chu, so, dau _ hoac -.");
                    continue;
                }
                if (danhSachNhaCungCap.timTheoMa(string3) == null) break;
                System.out.println("Ma nha cung cap da ton tai. Nhap ma khac.");
            }
        }
        if ((string2 = Main.nhapBatBuoc("Ten nha cung cap: ")) == null) {
            return null;
        }
        String string4 = Main.nhapSoDienThoai("So dien thoai: ", false);
        if (string4 == null) {
            return null;
        }
        String string5 = Main.nhapBatBuoc("Dia chi: ");
        return string5 == null ? null : new NhaCungCap(string3, string2, string4, string5);
    }

    private static void hienThiNhaCungCap(List<NhaCungCap> list) {
        if (list.isEmpty()) {
            System.out.println("Danh sach trong.");
        }
        for (NhaCungCap nhaCungCap : list) {
            System.out.println(VanBanConsole.khongDau(nhaCungCap.hienThiThongTin()) + "\n------------------------------");
        }
        System.out.println("Tong so nha cung cap: " + list.size());
    }

    public static void hienThiMenu() {
        System.out.println("\n1. Quan ly san pham");
        System.out.println("2. Quan ly danh muc");
        System.out.println("3. Quan ly khach hang");
        System.out.println("4. Quan ly nhan vien");
        System.out.println("5. Quan ly nha cung cap");
        System.out.println("6. Quan ly hoa don");
        System.out.println("7. Quan ly phieu nhap");
        System.out.println("8. Thong ke/bao cao");
        System.out.println("9. Luu/doc du lieu");
        System.out.println("0. Thoat");
        System.out.println("===== QUAN LY CUA HANG DUNG CU THE THAO =====");
    }
}

