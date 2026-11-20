package quanly;

import models.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class QuanLyPhieuNhap extends QuanLyCuaHang {

    @Override
    public void menuChinh() {
        int chon;
        do {
            System.out.println("\n=== QUAN LY PHIEU NHAP ===");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them phieu nhap");
            System.out.println("3. Sua phieu nhap");
            System.out.println("4. Xoa phieu nhap");
            System.out.println("5. Tim kiem");
            System.out.println("6. Xem chi tiet phieu nhap");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            chon = nhapSoNguyen("Chon: ", 0, 6);
            switch (chon) {
                case 1 -> dsPhieuNhap.xuat();
                case 2 -> themPhieuNhap();
                case 3 -> suaPhieuNhap();
                case 4 -> xoaPhieuNhap();
                case 5 -> timPhieuNhap();
                case 6 -> xemChiTiet();
                case 0 -> System.out.println("Quay lai...");
                default -> System.out.println("Lua chon khong hop le.");
            }
        } while (chon != 0);
    }

    private void themPhieuNhap() {
        System.out.println("\n=== TAO PHIEU NHAP MOI ===");
        String ma = nhapChuoi("Nhap ma phieu nhap: ");
        if (dsPhieuNhap.timTheoMa(ma) != null) {
            System.out.println("Ma phieu nhap da ton tai!");
            return;
        }

        NhaCungCap ncc = chonNhaCungCap();
        NhanVien nv = chonNhanVien();
        LocalDate ngay = nhapNgay("Ngay lap");

        PhieuNhap pn = new PhieuNhap(ma, ngay, ncc, nv);
        soanChiTietPhieuNhap(pn);

        if (!pn.kiemTraHopLe()) {
            System.out.println("Phieu nhap khong hop le hoac chua co mat hang nao!");
            return;
        }

        if (dsPhieuNhap.them(pn)) {
            kho.capNhatTheoPhieuNhap(null, pn);
            System.out.println("=> Da them phieu nhap thanh cong!");
            hienThiChiTiet(pn);
        } else {
            System.out.println("Them phieu nhap that bai!");
        }
    }

    private void suaPhieuNhap() {
        String ma = nhapChuoi("Nhap ma phieu nhap can sua: ");
        PhieuNhap cu = dsPhieuNhap.timTheoMa(ma);
        if (cu == null) {
            System.out.println("Khong tim thay phieu nhap!");
            return;
        }

        PhieuNhap moi = new PhieuNhap(cu);
        moi.setNgayLap(nhapNgay("Ngay lap moi"));
        moi.setNhaCungCap(chonNhaCungCap());
        moi.setNhanVien(chonNhanVien());
        soanChiTietPhieuNhap(moi);

        if (dsPhieuNhap.sua(ma, moi)) {
            kho.capNhatTheoPhieuNhap(cu, moi);
            System.out.println("=> Da cap nhat phieu nhap thanh cong!");
        } else {
            System.out.println("Cap nhat that bai!");
        }
    }

    private void xoaPhieuNhap() {
        String ma = nhapChuoi("Nhap ma phieu nhap can xoa: ");
        PhieuNhap cu = dsPhieuNhap.timTheoMa(ma);
        if (cu == null) {
            System.out.println("Khong tim thay phieu nhap!");
            return;
        }
        kho.capNhatTheoPhieuNhap(cu, null);
        dsPhieuNhap.xoa(ma);
        System.out.println("=> Da xoa phieu nhap thanh cong va tru ton kho!");
    }

    private void timPhieuNhap() {
        System.out.println("1. Tim theo ma phieu nhap / ma nha cung cap");
        System.out.println("2. Tim theo khoang ngay");
        int chon = nhapSoNguyen("Chon: ", 1, 2);
        PhieuNhap[] kq;
        if (chon == 1) {
            kq = dsPhieuNhap.timKiem(nhapChuoi("Nhap tu khoa: "));
        } else {
            kq = dsPhieuNhap.timKiem(nhapNgay("Tu ngay"), nhapNgay("Den ngay"));
        }
        for (PhieuNhap pn : kq) {
            hienThiChiTiet(pn);
        }
        System.out.println("Tim thay: " + kq.length + " phieu nhap.");
    }

    private void xemChiTiet() {
        String ma = nhapChuoi("Nhap ma phieu nhap: ");
        PhieuNhap pn = dsPhieuNhap.timTheoMa(ma);
        if (pn == null) {
            System.out.println("Khong tim thay phieu nhap!");
            return;
        }
        hienThiChiTiet(pn);
    }

    private void soanChiTietPhieuNhap(PhieuNhap pn) {
        int chon;
        do {
            System.out.println("Chi tiet: 1. Xem  2. Them SP  3. Sua SP  4. Xoa SP  0. Luu danh sach chi tiet");
            chon = nhapSoNguyen("Chon: ", 0, 4);
            switch (chon) {
                case 1 -> hienThiChiTiet(pn);
                case 2 -> {
                    SanPham sp = chonSanPham();
                    int sl = nhapSoNguyen("So luong nhap: ", 1, Integer.MAX_VALUE);
                    BigDecimal donGia = nhapTien("Don gia nhap: ");
                    boolean ok = pn.themChiTiet(new ChiTietPhieuNhap(sp, sl, donGia));
                    System.out.println(ok ? "Them chi tiet thanh cong!" : "Them that bai (SP da co trong phieu).");
                }
                case 3 -> {
                    SanPham sp = chonSanPham();
                    int sl = nhapSoNguyen("So luong nhap moi: ", 1, Integer.MAX_VALUE);
                    BigDecimal donGia = nhapTien("Don gia nhap moi: ");
                    boolean ok = pn.suaChiTiet(sp.getMaSanPham(), new ChiTietPhieuNhap(sp, sl, donGia));
                    System.out.println(ok ? "Sua chi tiet thanh cong!" : "Sua that bai.");
                }
                case 4 -> {
                    String maSP = nhapChuoi("Ma SP can xoa khoi phieu nhap: ");
                    boolean ok = pn.xoaChiTiet(maSP);
                    System.out.println(ok ? "Xoa chi tiet thanh cong!" : "Khong tim thay SP trong phieu.");
                }
            }
        } while (chon != 0);
    }

    private void hienThiChiTiet(PhieuNhap pn) {
        System.out.println("\n-------------------------------------------");
        System.out.println(pn.hienThiThongTin());
        System.out.println("Chi tiet mat hang nhap:");
        for (ChiTietPhieuNhap ct : pn.getChiTiet()) {
            System.out.println("  - " + ct.hienThiThongTin());
        }
        System.out.println("-------------------------------------------");
    }

    private NhaCungCap chonNhaCungCap() {
        String ma = nhapChuoi("Ma NhaCungCap: ");
        NhaCungCap x = dsNhaCungCap.timTheoMa(ma);
        if (x == null) throw new IllegalArgumentException("Khong tim thay NhaCungCap co ma: " + ma);
        return x;
    }

    private NhanVien chonNhanVien() {
        String ma = nhapChuoi("Ma NhanVien: ");
        NhanVien x = dsNhanVien.timTheoMa(ma);
        if (x == null) throw new IllegalArgumentException("Khong tim thay NhanVien co ma: " + ma);
        return x;
    }

    private SanPham chonSanPham() {
        String ma = nhapChuoi("Ma SanPham: ");
        SanPham x = dsSanPham.timTheoMa(ma);
        if (x == null) throw new IllegalArgumentException("Khong tim thay SanPham co ma: " + ma);
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

    private BigDecimal nhapTien(String loiNhac) {
        BigDecimal giaTri = new BigDecimal(nhapChuoi(loiNhac));
        if (giaTri.signum() < 0)
            throw new IllegalArgumentException("So tien phai >= 0.");
        return giaTri;
    }

    private LocalDate nhapNgay(String loiNhac) {
        return LocalDate.parse(nhapChuoi(loiNhac + " (yyyy-MM-dd): "));
    }
}
