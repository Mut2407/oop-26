package quanly;

import models.*;
import danhsach.*;
import java.math.BigDecimal;
import java.util.Scanner;

public class QuanLySanPham extends QuanLyCuaHang {
    private Scanner sc = new Scanner(System.in, "UTF-8");

    @Override
    public void menuChinh() {
        int chon;
        do {
            System.out.println("\n=== QUAN LY SAN PHAM ===");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them");
            System.out.println("3. Sua");
            System.out.println("4. Xoa");
            System.out.println("5. Tim kiem");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            chon = nhapSoNguyen("Chon: ", 0, 5);
            switch (chon) {
                case 1 -> dsSanPham.xuat();
                case 2 -> themSanPham();
                case 3 -> suaSanPham();
                case 4 -> xoaSanPham();
                case 5 -> timSanPham();
                case 0 -> System.out.println("Quay lai...");
                default -> System.out.println("Lua chon khong hop le.");
            }
        } while (chon != 0);
    }

    private void themSanPham() {
        int n = nhapSoNguyen("Nhap so luong can them (1-100): ", 1, 100);
        for (int i = 0; i < n; i++) {
            if (n > 1) System.out.println("\n-- Nhap phan tu thu " + (i + 1) + " --");
            String ma = nhapChuoi("Ma moi: ");
            boolean ok = dsSanPham.them(nhapSanPham(ma, null));
            System.out.println(ok ? "Them san pham thanh cong!" : "Them that bai (ma da ton tai hoac sai du lieu).");
        }
    }

    private void suaSanPham() {
        String ma = nhapChuoi("Ma can sua: ");
        SanPham cu = dsSanPham.timTheoMa(ma);
        if (cu == null) {
            System.out.println("Khong tim thay san pham!");
            return;
        }
        SanPham moi = nhapSanPham(cu.getMaSanPham(), cu);
        boolean ok = dsSanPham.sua(ma, moi);
        if (ok) {
            cu.capNhatTon(moi.getSoLuongTon() - cu.getSoLuongTon());
            System.out.println("Sua san pham thanh cong!");
        } else {
            System.out.println("Sua that bai!");
        }
    }

    private void xoaSanPham() {
        String ma = nhapChuoi("Ma can xoa: ");
        boolean ok = dsSanPham.xoa(ma);
        System.out.println(ok ? "Xoa thanh cong!" : "Khong tim thay ma can xoa!");
    }

    private void timSanPham() {
        System.out.println("1. Tim theo ma hoac ten");
        System.out.println("2. Tim theo khoang gia");
        System.out.println("3. Loc theo loai san pham");
        int chon = nhapSoNguyen("Chon: ", 1, 3);
        SanPham[] kq;
        if (chon == 1) {
            kq = dsSanPham.timKiem(nhapChuoi("Nhap tu khoa ma/ten: "));
        } else if (chon == 2) {
            kq = dsSanPham.timKiem(nhapTien("Gia tu: "), nhapTien("Gia den: "));
        } else {
            kq = dsSanPham.locTheoLoaiSP(nhapChuoi("Ma loai SP: "));
        }
        for (SanPham sp : kq) {
            System.out.println(sp.hienThiThongTin());
        }
        System.out.println("Tim thay: " + kq.length + " san pham.");
    }

    private SanPham nhapSanPham(String ma, SanPham cu) {
        int loai = cu == null ? nhapSoNguyen("1. Giay  2. Dung cu: ", 1, 2) : (cu instanceof GiayTheThao ? 1 : 2);
        LoaiSP loaiSP = chonLoaiSP();
        SanPham sp;
        if (loai == 1) {
            sp = new GiayTheThao(ma, nhapChuoi("Ten SP: "), nhapChuoi("Thuong hieu: "),
                    nhapTien("Gia ban: "), loaiSP, nhapSoThuc("Co giay: "),
                    nhapChuoi("Mau: "), nhapChuoi("Chat lieu: "), nhapChuoi("Loai de: "));
        } else {
            sp = new DungCuTheThao(ma, nhapChuoi("Ten SP: "), nhapChuoi("Thuong hieu: "),
                    nhapTien("Gia ban: "), loaiSP, nhapChuoi("Mon the thao: "),
                    nhapChuoi("Vat lieu: "), nhapSoThuc("Khoi luong (kg): "));
        }
        sp.capNhatTon(nhapSoNguyen("So luong ton kho: ", 0, Integer.MAX_VALUE));
        return sp;
    }

    private LoaiSP chonLoaiSP() {
        String ma = nhapChuoi("Ma LoaiSP: ");
        LoaiSP x = dsLoaiSP.timTheoMa(ma);
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay LoaiSP co ma: " + ma);
        return x;
    }

    private String nhapChuoi(String loiNhac) {
        System.out.print(loiNhac);
        String giaTri = sc.nextLine().trim();
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
}
