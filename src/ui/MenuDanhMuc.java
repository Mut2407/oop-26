package ui;

import java.io.IOException;
import models.DanhMuc;
import models.DuLieu;
import models.SanPham;
import services.DanhSachSanPham;
import services.LuuTruFileText;
import services.VanBanConsole;

public final class MenuDanhMuc {
    private final DanhSachSanPham sanPham;
    private final NhapLieuConsole nhap;
    private final LuuTruFileText luuTru;
    private final String tep;

    public MenuDanhMuc(DanhSachSanPham sanPham, NhapLieuConsole nhap, LuuTruFileText luuTru, String tep) {
        this.sanPham = sanPham;
        this.nhap = nhap;
        this.luuTru = luuTru;
        this.tep = tep;
    }

    public void quanLy() {
        while (true) {
            System.out.println("\n===== QUAN LY DANH MUC =====");
            System.out.println("1. Them  2. Sua  3. Xoa  4. Tim kiem  0. Quay lai");
            String chon = nhap.docDong("Nhap lua chon: ");
            if (chon == null || chon.equals("0")) return;
            try {
                switch (chon) {
                    case "1": {
                        String ma = nhap.nhapBatBuoc("Ma danh muc: ");
                        if (ma == null) return;
                        DanhMuc dm = nhapDanhMuc(ma);
                        if (dm == null) return;
                        DuLieu.dangKyDanhMuc(dm);
                        luuTru.ghiDanhMuc(tep, DuLieu.getDanhMuc());
                        System.out.println("Da them danh muc.");
                        break;
                    }
                    case "2": {
                        String ma = nhap.nhapBatBuoc("Ma danh muc can sua: ");
                        if (ma == null) return;
                        DanhMuc cu = DuLieu.timDanhMuc(ma);
                        if (cu == null) { System.out.println("Khong tim thay."); break; }
                        DanhMuc moi = nhapDanhMuc(cu.getMaDanhMuc());
                        if (moi == null) return;
                        cu.capNhatThongTin(moi.getTenDanhMuc(), moi.getMoTa());
                        luuTru.ghiDanhMuc(tep, DuLieu.getDanhMuc());
                        System.out.println("Da sua danh muc.");
                        break;
                    }
                    case "3": {
                        String ma = nhap.nhapBatBuoc("Ma danh muc can xoa: ");
                        if (ma == null) return;
                        for (SanPham sp : sanPham.getDanhSach()) {
                            if (sp.getDanhMuc().getMaDanhMuc().equalsIgnoreCase(ma)) {
                                throw new IllegalArgumentException("Khong the xoa danh muc dang co san pham.");
                            }
                        }
                        if (!DuLieu.xoaDanhMuc(ma)) { System.out.println("Khong tim thay."); break; }
                        luuTru.ghiDanhMuc(tep, DuLieu.getDanhMuc());
                        System.out.println("Da xoa danh muc.");
                        break;
                    }
                    case "4": {
                        String khoa = nhap.docDong("Ma hoac ten (Enter de xem tat ca): ");
                        if (khoa == null) return;
                        String tuKhoa = VanBanConsole.khongDau(khoa).toLowerCase(java.util.Locale.ROOT);
                        for (DanhMuc dm : DuLieu.getDanhMuc()) {
                            String thongTin = dm.getMaDanhMuc() + " " + dm.getTenDanhMuc();
                            if (VanBanConsole.khongDau(thongTin).toLowerCase(java.util.Locale.ROOT).contains(tuKhoa)) {
                                System.out.println(VanBanConsole.khongDau(dm.hienThiThongTin()));
                            }
                        }
                        break;
                    }
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

    private DanhMuc nhapDanhMuc(String ma) {
        String ten = nhap.nhapBatBuoc("Ten danh muc: ");
        if (ten == null) return null;
        String moTa = nhap.docDong("Mo ta (co the bo trong): ");
        return moTa == null ? null : new DanhMuc(ma, ten, moTa);
    }
}
