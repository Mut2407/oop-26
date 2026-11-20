package services;

import models.*;
import java.math.BigDecimal;
import java.util.Scanner;

public class QuanLySanPham {
    private DanhSachSanPham danhSach;
    private DanhSachLoaiSP danhSachLoaiSP;
    private Scanner banPhim;

    public QuanLySanPham(DanhSachSanPham danhSach, DanhSachLoaiSP danhSachLoaiSP, Scanner banPhim) {
        this.danhSach = danhSach;
        this.danhSachLoaiSP = danhSachLoaiSP;
        this.banPhim = banPhim;
    }

    public void hienThiMenu() {
        int chon;
        do {
            System.out.println("\n--- Quan ly San pham ---");
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

    public boolean them(SanPham sp) {
        if (sp == null) return false;
        return danhSach.them(sp);
    }

    public boolean sua(String ma, SanPham moi) {
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
            thongBao(them(nhapSanPham(ma, null)));
        }
    }

    private void suaTuBanPhim() {
        String ma = nhapChuoi("Ma can sua: ");
        SanPham cu = danhSach.timTheoMa(ma);
        if (cu == null)
            throw new IllegalArgumentException("Khong tim thay SP.");
        SanPham moi = nhapSanPham(cu.getMaSanPham(), cu);
        boolean thanhCong = sua(ma, moi);
        if (thanhCong)
            cu.capNhatTon(moi.getSoLuongTon() - cu.getSoLuongTon());
        thongBao(thanhCong);
    }

    private void timTuBanPhim() {
        int chon = nhapSoNguyen("1. Ma chinh xac  2. Ma/ten gan dung  3. Loai  4. Khoang gia: ", 1, 4);
        SanPham[] kq = locSanPham(chon);
        for (int i = 0; i < kq.length; i++)
            System.out.println(kq[i].hienThiThongTin());
        System.out.println("Tim thay: " + kq.length);
    }

    private SanPham[] locSanPham(int chon) {
        if (chon == 1) {
            SanPham sp = danhSach.timTheoMa(nhapChuoi("Ma: "));
            return sp == null ? new SanPham[0] : new SanPham[] {sp};
        }
        if (chon == 2)
            return danhSach.timKiem(nhapChuoi("Ma/ten: "));
        if (chon == 3)
            return danhSach.locTheoLoaiSP(nhapChuoi("Ma loai: "));
        return danhSach.locTheoKhoangGia(nhapTien("Gia tu: "), nhapTien("Gia den: "));
    }

    private SanPham nhapSanPham(String ma, SanPham cu) {
        int loai = cu == null ? nhapSoNguyen("1. Giay  2. Dung cu: ", 1, 2) : loaiLop(cu);
        SanPham sp = loai == 1 ? nhapGiay(ma) : nhapDungCu(ma);
        sp.capNhatTon(nhapSoNguyen("So luong ton kho: ", 0, Integer.MAX_VALUE));
        return sp;
    }

    private int loaiLop(SanPham sp) {
        if (sp instanceof GiayTheThao) return 1;
        return 2;
    }

    private LoaiSP chonLoaiSP() {
        LoaiSP x = danhSachLoaiSP.timTheoMa(nhapChuoi("Ma LoaiSP: "));
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay LoaiSP.");
        return x;
    }

    private GiayTheThao nhapGiay(String ma) {
        return new GiayTheThao(ma, nhapChuoi("Ten SP: "), nhapChuoi("Thuong hieu: "),
                nhapTien("Gia ban: "), chonLoaiSP(), nhapSoThuc("Co giay: "),
                nhapChuoi("Mau: "), nhapChuoi("Chat lieu: "), nhapChuoi("Loai de: "));
    }

    private DungCuTheThao nhapDungCu(String ma) {
        return new DungCuTheThao(ma, nhapChuoi("Ten SP: "), nhapChuoi("Thuong hieu: "),
                nhapTien("Gia ban: "), chonLoaiSP(), nhapChuoi("Mon the thao: "),
                nhapChuoi("Vat lieu: "), nhapSoThuc("Khoi luong (kg): "));
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

    private double nhapSoThuc(String loiNhac) {
        double giaTri = Double.parseDouble(nhapChuoi(loiNhac));
        if (!Double.isFinite(giaTri) || giaTri <= 0)
            throw new IllegalArgumentException("Nhap so duong huu han.");
        return giaTri;
    }

    private BigDecimal nhapTien(String loiNhac) {
        BigDecimal giaTri = new BigDecimal(nhapChuoi(loiNhac));
        if (giaTri.signum() < 0)
            throw new IllegalArgumentException("So tien phai >= 0.");
        return giaTri;
    }

    private void thongBao(boolean thanhCong) {
        System.out.println(thanhCong ? "Thao tac thanh cong!"
                : "That bai: Vui long kiem tra lai ma hoac du lieu nhap vao.");
    }
}
