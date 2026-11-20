package services;

import models.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DanhSachLoaiSP implements IQuanLyDanhSach<LoaiSP> {
    private ArrayList<LoaiSP> ds = new ArrayList<>();

    public int getSoLuong() {
        return ds.size();
    }

    public LoaiSP[] getTatCa() {
        return ds.toArray(new LoaiSP[0]);
    }

    private int timViTri(String ma) {
        if (ma == null)
            return -1;
        String maCanTim = ma.trim();
        for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).getMaLoaiSP().equalsIgnoreCase(maCanTim))
                return i;
        }
        return -1;
    }

    public boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) < 0;
    }

    public LoaiSP timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return null;
        return ds.get(viTri);
    }

    @Override
    public boolean them(LoaiSP doiTuong) {
        if (doiTuong == null || !doiTuong.kiemTraHopLe())
            return false;
        if (!kiemTraMaDuyNhat(doiTuong.getMaLoaiSP()))
            return false;
        return ds.add(doiTuong);
    }

    public int themNhieu(LoaiSP[] cacDoiTuong) {
        if (cacDoiTuong == null)
            return 0;
        int daThem = 0;
        for (LoaiSP loaiSP : cacDoiTuong) {
            if (them(loaiSP))
                daThem++;
        }
        return daThem;
    }

    @Override
    public boolean sua(String ma, LoaiSP moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!ds.get(viTri).getMaLoaiSP().equalsIgnoreCase(moi.getMaLoaiSP()))
            return false;
        ds.get(viTri).capNhatTu(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0)
            return false;
        ds.remove(viTri);
        return true;
    }

    @Override
    public LoaiSP[] timKiem(String tuKhoa) {
        String key = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        ArrayList<LoaiSP> kq = new ArrayList<>();
        for (LoaiSP x : ds) {
            if (phuHop(x, key))
                kq.add(x);
        }
        return kq.toArray(new LoaiSP[0]);
    }

    private boolean phuHop(LoaiSP x, String key) {
        return x.getMaLoaiSP().toLowerCase().contains(key)
                || x.getTenLoaiSP().toLowerCase().contains(key);
    }

    public void hienThiDanhSach() {
        if (ds.isEmpty()) {
            System.out.println("Danh sach rong.");
            return;
        }
        for (LoaiSP x : ds) {
            System.out.println(x.hienThiThongTin());
        }
    }

    // ================= MENU & NHẬP XUẤT =================
    public void menu(Scanner banPhim) {
        int chon;
        do {
            System.out.println("\n--- Quan ly Loai san pham ---");
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
            thongBao(them(nhapLoaiSP(banPhim, ma)));
        }
    }

    private void suaTuBanPhim(Scanner banPhim) {
        String ma = nhapChuoi(banPhim, "Ma can sua: ");
        thongBao(sua(ma, nhapLoaiSP(banPhim, ma)));
    }

    private void timTuBanPhim(Scanner banPhim) {
        LoaiSP[] kq = timKiem(nhapChuoi(banPhim, "Ma/ten (gan dung): "));
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private LoaiSP nhapLoaiSP(Scanner banPhim, String ma) {
        return new LoaiSP(ma, nhapChuoi(banPhim, "Ten loai: "), nhapChuoi(banPhim, "Mo ta: "));
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
