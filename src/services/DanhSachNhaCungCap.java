package services;

import models.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DanhSachNhaCungCap implements IQuanLyDanhSach<NhaCungCap> {
    private ArrayList<NhaCungCap> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public NhaCungCap[] getTatCa() {
        return ds.toArray(new NhaCungCap[0]);
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timTheoMa(ma) == null;
    }

    public NhaCungCap timTheoMa(String ma) {
        if (ma == null) return null;
        for (NhaCungCap ncc : ds)
            if (ncc.getMaNhaCungCap().equalsIgnoreCase(ma.trim()))
                return ncc;
        return null;
    }

    @Override
    public boolean them(NhaCungCap doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe()) return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaNhaCungCap())) return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(NhaCungCap[] cacDoiTuong) {
        int daThem = 0;
        for (NhaCungCap ncc : cacDoiTuong)
            if (them(ncc)) daThem++;
        return daThem;
    }

    @Override
    public boolean sua(String ma, NhaCungCap moi) {
        NhaCungCap cu = timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!cu.getMaNhaCungCap().equalsIgnoreCase(moi.getMaNhaCungCap())) return false;
        cu.capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        NhaCungCap ncc = timTheoMa(ma);
        if (ncc == null) return false;
        return ds.remove(ncc);
    }

    @Override
    public NhaCungCap[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<NhaCungCap> kq = new ArrayList<>();
        for (NhaCungCap ncc : ds)
            if (phuHop(ncc, key)) kq.add(ncc);
        return kq.toArray(new NhaCungCap[0]);
    }

    private boolean phuHop(NhaCungCap x, String key) {
        return x.getMaNhaCungCap().toLowerCase().contains(key)
                || x.getTenNhaCungCap().toLowerCase().contains(key)
                || x.getSoDienThoai().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) { System.out.println("Danh sach rong."); return; }
        for (NhaCungCap ncc : ds)
            System.out.println(ncc.hienThiThongTin());
    }

    // ================= MENU & NHẬP XUẤT =================
    public void menu(Scanner banPhim) {
        int chon;
        do {
            System.out.println("\n--- Quan ly Nha cung cap ---");
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
            thongBao(them(nhapNhaCungCap(banPhim, ma)));
        }
    }

    private void suaTuBanPhim(Scanner banPhim) {
        String ma = nhapChuoi(banPhim, "Ma can sua: ");
        thongBao(sua(ma, nhapNhaCungCap(banPhim, ma)));
    }

    private void timTuBanPhim(Scanner banPhim) {
        NhaCungCap[] kq = timKiem(nhapChuoi(banPhim, "Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private NhaCungCap nhapNhaCungCap(Scanner banPhim, String ma) {
        return new NhaCungCap(ma, nhapChuoi(banPhim, "Ten NCC: "),
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
