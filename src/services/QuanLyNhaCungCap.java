package services;

import models.*;
import java.util.Scanner;

public class QuanLyNhaCungCap {
    private DanhSachNhaCungCap danhSach;
    private Scanner banPhim;

    public QuanLyNhaCungCap(DanhSachNhaCungCap danhSach, Scanner banPhim) {
        this.danhSach = danhSach;
        this.banPhim = banPhim;
    }

    public void hienThiMenu() {
        int chon;
        do {
            System.out.println("\n--- Quan ly Nha cung cap ---");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("0. Quay lai");
            chon = nhapSoNguyen("Chon: ", 0, 5);
            switch (chon) {
                case 1: danhSach.hienThiDanhSach(); break;
                case 2: themTuBanPhim(); break;
                case 3: suaTuBanPhim(); break;
                case 4: thongBao(danhSach.xoa(nhapChuoi("Ma can xoa: "))); break;
                case 5: timTuBanPhim(); break;
            }
        } while (chon != 0);
    }

    public boolean them(NhaCungCap ncc) {
        if (ncc == null) return false;
        return danhSach.them(ncc);
    }

    public boolean sua(String ma, NhaCungCap moi) {
        if (moi == null) return false;
        return danhSach.sua(ma, moi);
    }

    public boolean xoa(String ma) {
        return danhSach.xoa(ma);
    }

    private void themTuBanPhim() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            thongBao(danhSach.them(nhapNhaCungCap(ma)));
        }
    }

    private void suaTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        thongBao(danhSach.sua(ma, nhapNhaCungCap(ma)));
    }

    private void timTuBanPhim() {
        NhaCungCap[] kq = danhSach.timKiem(nhapChuoi("Ma/ten/SDT (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private NhaCungCap nhapNhaCungCap(String ma) {
        return new NhaCungCap(ma, nhapChuoi("Ten NCC: "),
                nhapChuoi("SDT (9-11 chu so): "),
                nhapChuoi("Email: "));
    }

    // --- Nhập liệu tiện ích ---
    private String nhapChuoi(String loiNhac) {
        System.out.print(loiNhac);
        String giaTri = banPhim.nextLine().trim();
        if (giaTri.isEmpty() || giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Chuoi khong duoc rong, chua dau | hay xuong dong.");
        return giaTri;
    }

    private int nhapSoNguyen(String loiNhac, int min, int max) {
        int giaTri = Integer.parseInt(nhapChuoi(loiNhac));
        if (giaTri < min || giaTri > max)
            throw new IllegalArgumentException("Nhap so nguyen tu " + min + " den " + max + ".");
        return giaTri;
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thao tac thanh cong!"
                : "That bai: Vui long kiem tra lai ma hoac du lieu nhap vao.");
    }
}
