package ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import models.DanhMuc;
import models.DuLieu;
import models.SanPham;
import models.GiayTheThao;
import models.DungCuTheThao;
import models.QuanAoTheThao;
import services.DanhSachKhachHang;
import services.DanhSachSanPham;
import services.LuuTruFileText;
import services.VanBanConsole;

public final class MenuSanPham {
    private final DanhSachSanPham danhSachSanPham;
    private final NhapLieuConsole nhap;
    private final LuuTruFileText luuTru;
    private final String tep;

    public MenuSanPham(DanhSachSanPham danhSachSanPham, NhapLieuConsole nhap, LuuTruFileText luuTru, String tep) {
        this.danhSachSanPham = danhSachSanPham;
        this.nhap = nhap;
        this.luuTru = luuTru;
        this.tep = tep;
    }

    public void quanLy() {
        while (true) {
            System.out.println("\n===== QUAN LY SAN PHAM =====");
            System.out.println("1. Them  2. Sua  3. Xoa  4. Tim kiem  5. Thong ke ton kho  0. Quay lai");
            String chon = nhap.docDong("Nhap lua chon: ");
            if (chon == null || chon.equals("0")) return;
            try {
                switch (chon) {
                    case "1": {
                        SanPham sp = nhapSanPham(null, danhSachSanPham);
                        if (sp == null) return;
                        danhSachSanPham.them(sp);
                        luuTru.ghiSanPham(tep, danhSachSanPham);
                        System.out.println("Da them san pham.");
                        break;
                    }
                    case "2": {
                        String ma = nhap.nhapBatBuoc("Ma can sua: ");
                        if (ma == null) return;
                        SanPham cu = danhSachSanPham.CheckMaTrung(ma);
                        if (cu == null) { System.out.println("Khong tim thay."); break; }
                        SanPham moi = nhapSanPham(cu.getMaSanPham(), danhSachSanPham);
                        if (moi == null) return;
                        danhSachSanPham.suaTheoMa(ma, moi);
                        luuTru.ghiSanPham(tep, danhSachSanPham);
                        System.out.println("Da sua san pham.");
                        break;
                    }
                    case "3": {
                        String ma = nhap.nhapBatBuoc("Ma can xoa: ");
                        if (ma == null) return;
                        if (!danhSachSanPham.xoaTheoMa(ma)) { System.out.println("Khong tim thay."); break; }
                        luuTru.ghiSanPham(tep, danhSachSanPham);
                        System.out.println("Da xoa san pham.");
                        break;
                    }
                    case "4": {
                        String khoa = nhap.docDong("Ma hoac ten (Enter de xem tat ca): ");
                        if (khoa == null) return;
                        hienThiSanPham(khoa.isEmpty() ? danhSachSanPham.getDanhSach() : danhSachSanPham.TimKiemTheoMaHoacTen(khoa));
                        break;
                    }
                    case "5": System.out.println(VanBanConsole.khongDau(danhSachSanPham.thongKeTonKho())); break;
                    default: System.out.println("Lua chon khong hop le!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("CHUA LUU DUOC: " + e.getMessage());
                System.out.println("Thay doi chi dang trong bo nho.");
            }
        }
    }

    private void hienThiSanPham(List<SanPham> list) {
        if (list.isEmpty()) {
            System.out.println("Danh sach trong.");
        }
        for (SanPham sanPham : list) {
            System.out.println(VanBanConsole.khongDau(sanPham.hienThiThongTin()) + "\n------------------------------");
        }
    }

    private SanPham nhapSanPham(String string, DanhSachSanPham danhSachSanPham) {
        DanhMuc danhMuc;
        String string2;
        String string3 = string;
        if (string3 == null) {
            while (true) {
                if ((string3 = nhap.nhapBatBuoc("Ma san pham: ")) == null) {
                    return null;
                }
                if (!DanhSachKhachHang.maHopLe(string3)) {
                    System.out.println("Ma chi gom chu, so, dau _ hoac -.");
                    continue;
                }
                if (danhSachSanPham.CheckMaTrung(string3) == null) break;
                System.out.println("Ma san pham da ton tai. Nhap ma khac.");
            }
        }
        if ((string2 = nhap.nhapBatBuoc("Ten san pham: ")) == null) {
            return null;
        }
        BigDecimal bigDecimal = nhap.nhapGia();
        if (bigDecimal == null) {
            return null;
        }
        String string4 = nhap.nhapBatBuoc("Thuong hieu: ");
        if (string4 == null) {
            return null;
        }
        Integer n = nhap.nhapSoNguyen("So luong ton: ", 0);
        if (n == null) {
            return null;
        }
        System.out.println("Danh muc hien co:");
        for (DanhMuc object2 : DuLieu.getDanhMuc()) {
            System.out.println(object2.getMaDanhMuc() + " - " + VanBanConsole.khongDau(object2.getTenDanhMuc()));
        }
        while (true) {
            String string5;
            if ((string5 = nhap.nhapBatBuoc("Ma danh muc: ")) == null) {
                return null;
            }
            danhMuc = DuLieu.timDanhMuc(string5);
            if (danhMuc != null) break;
            System.out.println("Ma danh muc chua ton tai: " + string5);
        }
        String string6 = nhap.docDong("Mau sac (bo trong neu chua biet): ");
        if (string6 == null) {
            return null;
        }
        String string7 = nhap.docDong("Chat lieu (bo trong neu chua biet): ");
        if (string7 == null) {
            return null;
        }
        String string8;
        while ((string8 = nhap.docDong("Kieu (1=giay, 2=dung cu, 3=quan ao): ")) != null) {
            switch (string8) {
                case "1": {
                    String string9 = nhap.nhapBatBuoc("Kich co giay: ");
                    if (string9 == null) {
                        return null;
                    }
                    String string10 = nhap.nhapBatBuoc("Loai de giay: ");
                    return string10 == null ? null : new GiayTheThao(string3, string2, string4, bigDecimal, n, string6, string7, danhMuc, string9, string10);
                }
                case "2": {
                    Double d = nhap.nhapSoThuc("Khoi luong (kg): ", 0.0);
                    return d == null ? null : new DungCuTheThao(string3, string2, string4, bigDecimal, n, string6, string7, danhMuc, d);
                }
                case "3": {
                    String string11 = nhap.nhapBatBuoc("Kich co quan ao: ");
                    return string11 == null ? null : new QuanAoTheThao(string3, string2, string4, bigDecimal, n, string6, string7, danhMuc, string11);
                }
            }
            System.out.println("Kieu khong hop le.");
        }
        return null;
    }
}
