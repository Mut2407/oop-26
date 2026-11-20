package services;

import models.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class QuanLyPhieuNhap {
    private DanhSachPhieuNhap danhSach;
    private DanhSachSanPham danhSachSanPham;
    private DanhSachNhaCungCap danhSachNhaCungCap;
    private DanhSachNhanVien danhSachNhanVien;
    private Kho kho;
    private Scanner banPhim;

    public QuanLyPhieuNhap(DanhSachPhieuNhap danhSach, DanhSachSanPham danhSachSanPham,
            DanhSachNhaCungCap danhSachNhaCungCap, DanhSachNhanVien danhSachNhanVien,
            Kho kho, Scanner banPhim) {
        this.danhSach = danhSach;
        this.danhSachSanPham = danhSachSanPham;
        this.danhSachNhaCungCap = danhSachNhaCungCap;
        this.danhSachNhanVien = danhSachNhanVien;
        this.kho = kho;
        this.banPhim = banPhim;
    }

    public void hienThiMenu() {
        int chon;
        do {
            System.out.println("\n--- Quan ly Phieu nhap ---");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("6. Xem chi tiet");
            System.out.println("0. Quay lai");
            chon = nhapSoNguyen("Chon: ", 0, 6);
            switch (chon) {
                case 1: danhSach.hienThiDanhSach(); break;
                case 2: themTuBanPhim(); break;
                case 3: suaTuBanPhim(); break;
                case 4: thongBao(xoa(nhapChuoi("Ma can xoa: "))); break;
                case 5: timTuBanPhim(); break;
                case 6: xemChiTiet(); break;
            }
        } while (chon != 0);
    }

    public boolean them(PhieuNhap phieu) {
        if (phieu == null || !phieu.kiemTraHopLe()) return false;
        if (!danhSach.them(phieu)) return false;
        kho.capNhatTheoPhieuNhap(null, phieu);
        return true;
    }

    public boolean sua(String ma, PhieuNhap moi) {
        PhieuNhap cu = danhSach.timTheoMa(ma);
        if (cu == null || moi == null || !moi.kiemTraHopLe()) return false;
        if (!danhSach.sua(ma, moi)) return false;
        kho.capNhatTheoPhieuNhap(cu, moi);
        return true;
    }

    public boolean xoa(String ma) {
        PhieuNhap cu = danhSach.timTheoMa(ma);
        if (cu == null) return false;
        kho.capNhatTheoPhieuNhap(cu, null);
        return danhSach.xoa(ma);
    }

    public BigDecimal thongKeTongTien() {
        return danhSach.thongKeTongTien();
    }

    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        return danhSach.thongKeTongTien(tu, den);
    }

    private void themTuBanPhim() {
        PhieuNhap phieu = nhapPhieuNhap(nhapChuoi("Ma moi: "));
        soanPhieuNhap(phieu);
        thongBao(them(phieu));
    }

    private void suaTuBanPhim() {
        PhieuNhap moi = chonPhieuNhap();
        moi.setNgayLap(nhapNgay("Ngay lap moi"));
        moi.setNhaCungCap(chonNhaCungCap());
        moi.setNhanVien(chonNhanVien());
        soanPhieuNhap(moi);
        thongBao(sua(moi.getMaPhieuNhap(), moi));
    }

    private void timTuBanPhim() {
        PhieuNhap[] kq = danhSach.timKiem(nhapChuoi("Ma chung tu/ma doi tac: "));
        for (int i = 0; i < kq.length; i++)
            hienThiChiTietPN(kq[i]);
        System.out.println("Tim thay: " + kq.length);
    }

    private void xemChiTiet() {
        hienThiChiTietPN(chonPhieuNhap());
    }

    private PhieuNhap nhapPhieuNhap(String ma) {
        return new PhieuNhap(ma, nhapNgay("Ngay lap"), chonNhaCungCap(), chonNhanVien());
    }

    private PhieuNhap chonPhieuNhap() {
        PhieuNhap phieu = danhSach.timTheoMa(nhapChuoi("Ma chung tu: "));
        if (phieu == null)
            throw new IllegalArgumentException("Khong tim thay chung tu.");
        return phieu;
    }

    private void soanPhieuNhap(PhieuNhap phieu) {
        int chon;
        do {
            System.out.println("Chi tiet: 1. Xem   2. Them   3. Sua   4. Xoa   0. Luu ban soan");
            chon = nhapSoNguyen("Chon: ", 0, 4);
            switch (chon) {
                case 1: hienThiChiTietPN(phieu); break;
                case 2: thongBao(phieu.themChiTiet(nhapChiTietPhieuNhap(chonSanPham()))); break;
                case 3: suaDongPhieuNhap(phieu); break;
                case 4: thongBao(phieu.xoaChiTiet(nhapChuoi("Ma SP can xoa: "))); break;
            }
        } while (chon != 0);
    }

    private ChiTietPhieuNhap nhapChiTietPhieuNhap(SanPham sp) {
        return new ChiTietPhieuNhap(sp, nhapSoNguyen("So luong: ", 1, Integer.MAX_VALUE),
                nhapTien("Don gia nhap: "));
    }

    private void suaDongPhieuNhap(PhieuNhap phieu) {
        SanPham sp = chonSanPham();
        thongBao(phieu.suaChiTiet(sp.getMaSanPham(), nhapChiTietPhieuNhap(sp)));
    }

    private void hienThiChiTietPN(PhieuNhap phieu) {
        System.out.println(phieu.hienThiThongTin());
        ChiTietPhieuNhap[] ds = phieu.getChiTiet();
        for (int i = 0; i < ds.length; i++)
            System.out.println("  " + ds[i].hienThiThongTin());
    }

    // --- Chọn đối tượng liên quan ---
    private NhaCungCap chonNhaCungCap() {
        NhaCungCap x = danhSachNhaCungCap.timTheoMa(nhapChuoi("Ma NhaCungCap: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay NhaCungCap.");
        return x;
    }

    private NhanVien chonNhanVien() {
        NhanVien x = danhSachNhanVien.timTheoMa(nhapChuoi("Ma NhanVien: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay NhanVien.");
        return x;
    }

    private SanPham chonSanPham() {
        SanPham x = danhSachSanPham.timTheoMa(nhapChuoi("Ma SP: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay san pham.");
        return x;
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

    private BigDecimal nhapTien(String loiNhac) {
        BigDecimal giaTri = new BigDecimal(nhapChuoi(loiNhac));
        if (giaTri.signum() < 0)
            throw new IllegalArgumentException("So tien phai >= 0.");
        return giaTri;
    }

    private LocalDate nhapNgay(String loiNhac) {
        return LocalDate.parse(nhapChuoi(loiNhac + " (yyyy-MM-dd): "));
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thao tac thanh cong!"
                : "That bai: Vui long kiem tra lai ma hoac du lieu nhap vao.");
    }
}
