package services;

import models.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DanhSachKhachHang implements IQuanLyDanhSach<KhachHang> {
    private ArrayList<KhachHang> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public KhachHang[] getTatCa() {
        return ds.toArray(new KhachHang[0]);
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public KhachHang timTheoMa(String ma) {
        if (ma == null) return null;
        for (KhachHang kh : ds)
            if (kh.getMaKhachHang().equalsIgnoreCase(ma.trim()))
                return kh;
        return null;
    }

    @Override
    public boolean them(KhachHang doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaKhachHang())) return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(KhachHang[] cacDoiTuong) {
        int daThem = 0;
        for (KhachHang kh : cacDoiTuong)
            if (them(kh)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, KhachHang moi) {
        KhachHang cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!cu.getMaKhachHang().equalsIgnoreCase(moi.getMaKhachHang())) return false;
        cu.capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        KhachHang kh = timTheoMa(ma);
        if (kh == null) return false;
        return ds.remove(kh);
    }

    @Override
    public KhachHang[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<KhachHang> kq = new ArrayList<>();
        for (KhachHang kh : ds)
            if (phuHop(kh, key)) kq.add(kh);
        return kq.toArray(new KhachHang[0]);
    }

    private boolean phuHop(KhachHang x, String key) {
        return x.getMaKhachHang().toLowerCase().contains(key)
                || x.getHoTen().toLowerCase().contains(key)
                || x.getSoDienThoai().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (KhachHang kh : ds)
            System.out.println(kh.hienThiThongTin());
    }

    // ================= MENU & NHẬP XUẤT =================
    public void menu(Scanner banPhim) {
        int chon;
        do {
            System.out.println("\n--- Quan ly Khach hang ---");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("0. Quay lai");
            chon = nhapSoNguyen(banPhim, "Chon: ", 0, 5);
            switch (chon) {
                case 1: hienThiDanhSach(); break;
                case 2: themTuBanPhim(banPhim); break;
                case 3: suaTuBanPhim(banPhim); break;
                case 4: thongBao(xoa(nhapChuoi(banPhim, "Ma can xoa: "))); break;
                case 5: timTuBanPhim(banPhim); break;
            }
        } while (chon != 0);
    }

    private void themTuBanPhim(Scanner banPhim) {
        int n = nhapSoNguyen(banPhim, "Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi(banPhim, "Ma moi: ");
            thongBao(them(nhapKhachHang(banPhim, ma)));
        }
    }

    private void suaTuBanPhim(Scanner banPhim) {
        String ma = nhapChuoi(banPhim, "Ma can sua: ");
        thongBao(sua(ma, nhapKhachHang(banPhim, ma)));
    }

    private void timTuBanPhim(Scanner banPhim) {
        KhachHang[] kq = timKiem(nhapChuoi(banPhim, "Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private KhachHang nhapKhachHang(Scanner banPhim, String ma) {
        return new KhachHang(ma, nhapChuoi(banPhim, "Ho ten: "),
                nhapChuoi(banPhim, "SDT (9-11 chu so): "),
                nhapChuoi(banPhim, "Email: "));
    }

    private String nhapChuoi(Scanner banPhim, String loiNhac) {
        System.out.print(loiNhac);
        String giaTri = banPhim.nextLine().trim();
        if (giaTri.isEmpty() || giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Chuoi khong duoc rong, chua dau | hay xuong dong.");
        return giaTri;
    }

    private int nhapSoNguyen(Scanner banPhim, String loiNhac, int min, int max) {
        int giaTri = Integer.parseInt(nhapChuoi(banPhim, loiNhac));
        if (giaTri < min || giaTri > max)
            throw new IllegalArgumentException("Nhap so nguyen tu " + min + " den " + max + ".");
        return giaTri;
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thao tac thanh cong!"
                : "That bai: Vui long kiem tra lai ma hoac du lieu nhap vao.");
    }
}
