package ui;

import java.io.IOException;
import java.util.List;
import models.NhaCungCap;
import services.DanhSachKhachHang;
import services.DanhSachNhaCungCap;
import services.LuuTruFileText;
import services.VanBanConsole;

public final class MenuNhaCungCap {
    private final DanhSachNhaCungCap danhSachNhaCungCap;
    private final NhapLieuConsole nhap;
    private final LuuTruFileText luuTru;
    private final String tep;

    public MenuNhaCungCap(DanhSachNhaCungCap danhSachNhaCungCap, NhapLieuConsole nhap, LuuTruFileText luuTru, String tep) {
        this.danhSachNhaCungCap = danhSachNhaCungCap;
        this.nhap = nhap;
        this.luuTru = luuTru;
        this.tep = tep;
    }

    public void quanLy() {
        while (true) {
            System.out.println("\n===== QUAN LY NHA CUNG CAP =====");
            System.out.println("1. Them  2. Sua  3. Xoa  4. Tim kiem  0. Quay lai");
            String chon = nhap.docDong("Nhap lua chon: ");
            if (chon == null || chon.equals("0")) return;
            try {
                switch (chon) {
                    case "1": {
                        NhaCungCap ncc = nhapNhaCungCap(null, danhSachNhaCungCap);
                        if (ncc == null) return;
                        danhSachNhaCungCap.them(ncc);
                        luuTru.ghiNhaCungCap(tep, danhSachNhaCungCap);
                        System.out.println("Da them nha cung cap.");
                        break;
                    }
                    case "2": {
                        String ma = nhap.nhapBatBuoc("Ma can sua: ");
                        if (ma == null) return;
                        NhaCungCap cu = danhSachNhaCungCap.CheckMaTrung(ma);
                        if (cu == null) { System.out.println("Khong tim thay."); break; }
                        NhaCungCap moi = nhapNhaCungCap(cu.getMaNhaCungCap(), danhSachNhaCungCap);
                        if (moi == null) return;
                        danhSachNhaCungCap.suaTheoMa(ma, moi);
                        luuTru.ghiNhaCungCap(tep, danhSachNhaCungCap);
                        System.out.println("Da sua nha cung cap.");
                        break;
                    }
                    case "3": {
                        String ma = nhap.nhapBatBuoc("Ma can xoa: ");
                        if (ma == null) return;
                        if (!danhSachNhaCungCap.xoaTheoMa(ma)) { System.out.println("Khong tim thay."); break; }
                        luuTru.ghiNhaCungCap(tep, danhSachNhaCungCap);
                        System.out.println("Da xoa nha cung cap.");
                        break;
                    }
                    case "4": {
                        String khoa = nhap.docDong("Ma hoac ten (Enter de xem tat ca): ");
                        if (khoa == null) return;
                        hienThiNhaCungCap(khoa.isEmpty() ? danhSachNhaCungCap.getTatCa() : danhSachNhaCungCap.TimKiemTheoMaHoacTen(khoa));
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

    private NhaCungCap nhapNhaCungCap(String string, DanhSachNhaCungCap danhSachNhaCungCap) {
        String string2;
        String string3 = string;
        if (string3 == null) {
            while (true) {
                if ((string3 = nhap.nhapBatBuoc("Ma nha cung cap: ")) == null) {
                    return null;
                }
                if (!DanhSachKhachHang.maHopLe(string3)) {
                    System.out.println("Ma chi gom chu, so, dau _ hoac -.");
                    continue;
                }
                if (danhSachNhaCungCap.CheckMaTrung(string3) == null) break;
                System.out.println("Ma nha cung cap da ton tai. Nhap ma khac.");
            }
        }
        if ((string2 = nhap.nhapBatBuoc("Ten nha cung cap: ")) == null) {
            return null;
        }
        String string4 = nhap.nhapSoDienThoai("So dien thoai: ", false);
        if (string4 == null) {
            return null;
        }
        String string5 = nhap.nhapBatBuoc("Dia chi: ");
        return string5 == null ? null : new NhaCungCap(string3, string2, string4, string5);
    }

    private void hienThiNhaCungCap(List<NhaCungCap> list) {
        if (list.isEmpty()) {
            System.out.println("Danh sach trong.");
        }
        for (NhaCungCap nhaCungCap : list) {
            System.out.println(VanBanConsole.khongDau(nhaCungCap.hienThiThongTin()) + "\n------------------------------");
        }
    }
}
