package ui;

import java.io.IOException;
import models.NhanVien;
import services.DanhSachKhachHang;
import services.DanhSachNhanVien;
import services.LuuTruFileText;
import services.VanBanConsole;

public final class MenuNhanVien {
    private final DanhSachNhanVien danhSach;
    private final NhapLieuConsole nhap;
    private final LuuTruFileText luuTru;
    private final String tep;

    public MenuNhanVien(DanhSachNhanVien danhSach, NhapLieuConsole nhap, LuuTruFileText luuTru, String tep) {
        this.danhSach = danhSach;
        this.nhap = nhap;
        this.luuTru = luuTru;
        this.tep = tep;
    }

    public void quanLy() {
        while (true) {
            System.out.println("\n===== QUAN LY NHAN VIEN =====");
            System.out.println("1. Them  2. Sua  3. Xoa  4. Tim kiem  0. Quay lai");
            String chon = nhap.docDong("Nhap lua chon: ");
            if (chon == null || chon.equals("0")) return;
            try {
                switch (chon) {
                    case "1": {
                        NhanVien nv = nhapNhanVien();
                        if (nv == null) return;
                        danhSach.them(nv);
                        luuTru.ghiNhanVien(tep, danhSach);
                        System.out.println("Da them nhan vien.");
                        break;
                    }
                    case "2":
                        if (suaTuBanPhim()) luuTru.ghiNhanVien(tep, danhSach);
                        break;
                    case "3": {
                        String ma = nhap.nhapBatBuoc("Ma nhan vien can xoa: ");
                        if (ma == null) return;
                        if (!danhSach.xoa(ma)) { System.out.println("Khong tim thay nhan vien!"); break; }
                        luuTru.ghiNhanVien(tep, danhSach);
                        System.out.println("Da xoa nhan vien.");
                        break;
                    }
                    case "4": {
                        String khoa = nhap.docDong("Ma hoac ten (Enter de xem tat ca): ");
                        if (khoa == null) return;
                        hienThi(khoa.isEmpty() ? danhSach.getDanhSach() : danhSach.TimKiemTheoMaHoacTen(khoa));
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

    private NhanVien nhapNhanVien() {
        String ma;
        while (true) {
            ma = nhap.nhapBatBuoc("Ma nhan vien: ");
            if (ma == null) return null;
            if (!DanhSachKhachHang.maHopLe(ma)) {
                System.out.println("Ma khong hop le.");
            } else if (danhSach.CheckMaTrung(ma) != null) {
                System.out.println("Ma nhan vien da ton tai!");
            } else break;
        }
        String ten = nhap.nhapBatBuoc("Ho ten: ");
        if (ten == null) return null;
        String ca = nhap.nhapBatBuoc("Ca lam viec: ");
        if (ca == null) return null;
        String sdt = nhap.nhapSoDienThoai("So dien thoai: ", false);
        return sdt == null ? null : new NhanVien(ma, ten, sdt, ca);
    }

    private boolean suaTuBanPhim() {
        String string = nhap.nhapBatBuoc("Ma nhan vien can sua: ");
        if (string == null) {
            return false;
        }
        NhanVien nhanVien = danhSach.CheckMaTrung(string);
        if (nhanVien == null) {
            System.out.println("Khong tim thay nhan vien!");
            return false;
        }
        hienThi(new NhanVien[]{nhanVien});
        System.out.println("Nhan Enter de giu gia tri cu. Ma nhan vien duoc giu nguyen.");
        String string2 = nhap.docDong("Ho ten moi: ");
        if (string2 == null) {
            return false;
        }
        String string3 = nhap.docDong("Ca lam viec moi: ");
        if (string3 == null) {
            return false;
        }
        String string4 = nhap.nhapSoDienThoai("So dien thoai moi: ", true);
        if (string4 == null) {
            return false;
        }
        String ten = string2.isEmpty() ? nhanVien.getHoTen() : string2;
        String ca = string3.isEmpty() ? nhanVien.getCaLamViec() : string3;
        String sdt = string4.isEmpty() ? nhanVien.getSoDienThoai() : string4;
        danhSach.sua(string, ten, ca, sdt);
        System.out.println("Da cap nhat nhan vien.");
        return true;
    }

    private void hienThi(NhanVien[] nhanVienArray) {
        System.out.println("===== DANH SACH NHAN VIEN =====");
        if (nhanVienArray.length == 0) {
            System.out.println("Danh sach nhan vien dang trong.");
        }
        for (NhanVien nhanVien : nhanVienArray) {
            System.out.println(VanBanConsole.khongDau(nhanVien.hienThiThongTin()));
            String diaChi = danhSach.getDiaChiCu(nhanVien.getMaNhanVien());
            if (diaChi != null) {
                System.out.println(VanBanConsole.khongDau("Dia chi (du lieu chuyen tiep): " + diaChi));
            }
            System.out.println("------------------------------");
        }
    }
}
