package ui;

import java.io.IOException;
import models.KhachHang;
import services.DanhSachKhachHang;
import services.LuuTruFileText;
import services.VanBanConsole;

public final class MenuKhachHang {
    private final DanhSachKhachHang danhSachKhachHang;
    private final NhapLieuConsole nhap;
    private final LuuTruFileText luuTru;
    private final String tep;

    public MenuKhachHang(DanhSachKhachHang danhSachKhachHang, NhapLieuConsole nhap, LuuTruFileText luuTru, String tep) {
        this.danhSachKhachHang = danhSachKhachHang;
        this.nhap = nhap;
        this.luuTru = luuTru;
        this.tep = tep;
    }

    public void quanLy() {
        while (true) {
            System.out.println("\n===== QUAN LY KHACH HANG =====");
            System.out.println("1. Them  2. Sua  3. Xoa  4. Tim kiem  0. Quay lai");
            String chon = nhap.docDong("Nhap lua chon: ");
            if (chon == null || chon.equals("0")) return;
            try {
                switch (chon) {
                    case "1": {
                        KhachHang kh = nhapKhachHang(danhSachKhachHang);
                        if (kh == null) return;
                        danhSachKhachHang.them(kh);
                        luuTru.ghiKhachHang(tep, danhSachKhachHang);
                        System.out.println("Da them khach hang.");
                        break;
                    }
                    case "2":
                        if (suaKhachHang(danhSachKhachHang)) luuTru.ghiKhachHang(tep, danhSachKhachHang);
                        break;
                    case "3":
                        if (xoaKhachHang(danhSachKhachHang)) luuTru.ghiKhachHang(tep, danhSachKhachHang);
                        break;
                    case "4": {
                        String khoa = nhap.docDong("Ma hoac ten (Enter de xem tat ca): ");
                        if (khoa == null) return;
                        hienThiDanhSachKhachHang(khoa.isEmpty() ? danhSachKhachHang.getDanhSach()
                                : danhSachKhachHang.timGanDung(khoa).toArray(new KhachHang[0]));
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

    private void hienThiDanhSachKhachHang(KhachHang[] khachHangArray) {
        System.out.println("===== DANH SACH KHACH HANG =====");
        if (khachHangArray.length == 0) {
            System.out.println("Danh sach khach hang dang trong.");
            return;
        }
        for (KhachHang khachHang : khachHangArray) {
            System.out.println(VanBanConsole.khongDau(khachHang.hienThiThongTin()));
            System.out.println("------------------------------");
        }
    }

    private KhachHang nhapKhachHang(DanhSachKhachHang danhSachKhachHang) {
        String string;
        while (true) {
            if ((string = nhap.nhapBatBuoc("Nhap ma khach hang: ")) == null) {
                return null;
            }
            if (!DanhSachKhachHang.maHopLe(string)) {
                System.out.println("Ma chi gom chu, so, dau _ hoac -.");
                continue;
            }
            if (danhSachKhachHang.timTheoMa(string) == null) break;
            System.out.println("Ma khach hang da ton tai! Vui long nhap ma khac.");
        }
        String string2 = nhap.nhapBatBuoc("Nhap ho ten: ");
        if (string2 == null) {
            return null;
        }
        String string3 = nhap.nhapSoDienThoai("Nhap so dien thoai: ", false);
        if (string3 == null) {
            return null;
        }
        String string4 = nhap.nhapBatBuoc("Nhap dia chi: ");
        if (string4 == null) {
            return null;
        }
        return new KhachHang(string, string2, string3, string4);
    }

    private boolean suaKhachHang(DanhSachKhachHang danhSachKhachHang) {
        String string = nhap.nhapBatBuoc("Nhap ma khach hang can sua: ");
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
        String string2 = nhap.docDong("Ho ten moi: ");
        if (string2 == null) {
            return false;
        }
        String string3 = nhap.nhapSoDienThoai("So dien thoai moi: ", true);
        if (string3 == null) {
            return false;
        }
        String string4 = nhap.docDong("Dia chi moi: ");
        if (string4 == null) {
            return false;
        }
        danhSachKhachHang.sua(string, string2.isEmpty() ? khachHang.getHoTen() : string2, string3.isEmpty() ? khachHang.getSoDienThoai() : string3, string4.isEmpty() ? khachHang.getDiaChi() : string4);
        System.out.println("Cap nhat khach hang thanh cong!");
        return true;
    }

    private boolean xoaKhachHang(DanhSachKhachHang danhSachKhachHang) {
        String string = nhap.nhapBatBuoc("Nhap ma khach hang can xoa: ");
        if (string == null) {
            return false;
        }
        boolean xoa = danhSachKhachHang.xoaTheoMa(string);
        System.out.println(xoa ? "Xoa khach hang thanh cong!" : "Khong tim thay khach hang!");
        return xoa;
    }
}
