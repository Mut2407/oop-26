package quanly;

import models.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class QuanLyHoaDon extends QuanLyCuaHang {

    @Override
    public void menuChinh() {
        int chon;
        do {
            System.out.println("\n=== QUAN LY HOA DON ===");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Them hoa don");
            System.out.println("3. Sua hoa don");
            System.out.println("4. Xoa hoa don");
            System.out.println("5. Tim kiem");
            System.out.println("6. Xem chi tiet hoa don");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            chon = nhapSoNguyen("Chon: ", 0, 6);
            switch (chon) {
                case 1 -> dsHoaDon.xuat();
                case 2 -> themHoaDon();
                case 3 -> suaHoaDon();
                case 4 -> xoaHoaDon();
                case 5 -> timHoaDon();
                case 6 -> xemChiTiet();
                case 0 -> System.out.println("Quay lai...");
                default -> System.out.println("Lua chon khong hop le.");
            }
        } while (chon != 0);
    }

    private void themHoaDon() {
        System.out.println("\n=== TAO HOA DON MOI ===");
        String ma = nhapChuoi("Nhap ma hoa don: ");
        if (dsHoaDon.timTheoMa(ma) != null) {
            System.out.println("Ma hoa don da ton tai!");
            return;
        }

        KhachHang kh = chonKhachHang();
        NhanVien nv = chonNhanVien();
        LocalDate ngay = nhapNgay("Ngay lap");

        HoaDon hd = new HoaDon(ma, ngay, kh, nv);
        soanChiTietHoaDon(hd);

        if (!hd.kiemTraHopLe()) {
            System.out.println("Hoa don khong hop le hoac chua co san pham nao!");
            return;
        }

        // Cập nhật kho và thêm vào danh sách
        if (dsHoaDon.them(hd)) {
            kho.capNhatTheoHoaDon(null, hd);
            System.out.println("=> Da them hoa don thanh cong!");
            hienThiChiTiet(hd);
        } else {
            System.out.println("Them hoa don that bai!");
        }
    }

    private void suaHoaDon() {
        String ma = nhapChuoi("Nhap ma hoa don can sua: ");
        HoaDon cu = dsHoaDon.timTheoMa(ma);
        if (cu == null) {
            System.out.println("Khong tim thay hoa don!");
            return;
        }

        HoaDon moi = new HoaDon(cu);
        moi.setNgayLap(nhapNgay("Ngay lap moi"));
        moi.setKhachHang(chonKhachHang());
        moi.setNhanVien(chonNhanVien());
        soanChiTietHoaDon(moi);

        if (dsHoaDon.sua(ma, moi)) {
            kho.capNhatTheoHoaDon(cu, moi);
            System.out.println("=> Da cap nhat hoa don thanh cong!");
        } else {
            System.out.println("Cap nhat that bai!");
        }
    }

    private void xoaHoaDon() {
        String ma = nhapChuoi("Nhap ma hoa don can xoa: ");
        HoaDon cu = dsHoaDon.timTheoMa(ma);
        if (cu == null) {
            System.out.println("Khong tim thay hoa don!");
            return;
        }
        kho.capNhatTheoHoaDon(cu, null);
        dsHoaDon.xoa(ma);
        System.out.println("=> Da xoa hoa don thanh cong va hoan ton kho!");
    }

    private void timHoaDon() {
        System.out.println("1. Tim theo ma hoa don / ma khach hang");
        System.out.println("2. Tim theo khoang ngay");
        int chon = nhapSoNguyen("Chon: ", 1, 2);
        HoaDon[] kq;
        if (chon == 1) {
            kq = dsHoaDon.timKiem(nhapChuoi("Nhap tu khoa: "));
        } else {
            kq = dsHoaDon.timKiem(nhapNgay("Tu ngay"), nhapNgay("Den ngay"));
        }
        for (HoaDon hd : kq) {
            hienThiChiTiet(hd);
        }
        System.out.println("Tim thay: " + kq.length + " hoa don.");
    }

    private void xemChiTiet() {
        String ma = nhapChuoi("Nhap ma hoa don: ");
        HoaDon hd = dsHoaDon.timTheoMa(ma);
        if (hd == null) {
            System.out.println("Khong tim thay hoa don!");
            return;
        }
        hienThiChiTiet(hd);
    }

    private void soanChiTietHoaDon(HoaDon hd) {
        int chon;
        do {
            System.out.println("Chi tiet: 1. Xem  2. Them SP  3. Sua SP  4. Xoa SP  0. Luu danh sach chi tiet");
            chon = nhapSoNguyen("Chon: ", 0, 4);
            switch (chon) {
                case 1 -> hienThiChiTiet(hd);
                case 2 -> {
                    SanPham sp = chonSanPham();
                    int sl = nhapSoNguyen("So luong ban (Ton hien tai: " + sp.getSoLuongTon() + "): ", 1,
                            Integer.MAX_VALUE);
                    BigDecimal donGia = nhapTien("Don gia ban: ");
                    boolean ok = hd.themChiTiet(new ChiTietHoaDon(sp, sl, donGia));
                    System.out.println(ok ? "Them chi tiet thanh cong!" : "Them that bai (SP da co trong hoa don).");
                }
                case 3 -> {
                    SanPham sp = chonSanPham();
                    int sl = nhapSoNguyen("So luong moi: ", 1, Integer.MAX_VALUE);
                    BigDecimal donGia = nhapTien("Don gia ban moi: ");
                    boolean ok = hd.suaChiTiet(sp.getMaSanPham(), new ChiTietHoaDon(sp, sl, donGia));
                    System.out.println(ok ? "Sua chi tiet thanh cong!" : "Sua that bai.");
                }
                case 4 -> {
                    String maSP = nhapChuoi("Ma SP can xoa khoi hoa don: ");
                    boolean ok = hd.xoaChiTiet(maSP);
                    System.out.println(ok ? "Xoa chi tiet thanh cong!" : "Khong tim thay SP trong hoa don.");
                }
            }
        } while (chon != 0);
    }

    private void hienThiChiTiet(HoaDon hd) {
        System.out.println("\n-------------------------------------------");
        System.out.println(hd.hienThiThongTin());
        System.out.println("Chi tiet mat hang:");
        for (ChiTietHoaDon ct : hd.getChiTiet()) {
            System.out.println("  - " + ct.hienThiThongTin());
        }
        System.out.println("-------------------------------------------");
    }

    private KhachHang chonKhachHang() {
        String ma = nhapChuoi("Ma KhachHang: ");
        KhachHang x = dsKhachHang.timTheoMa(ma);
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay KhachHang co ma: " + ma);
        return x;
    }

    private NhanVien chonNhanVien() {
        String ma = nhapChuoi("Ma NhanVien: ");
        NhanVien x = dsNhanVien.timTheoMa(ma);
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay NhanVien co ma: " + ma);
        return x;
    }

    private SanPham chonSanPham() {
        String ma = nhapChuoi("Ma SanPham: ");
        SanPham x = dsSanPham.timTheoMa(ma);
        if (x == null)
            throw new IllegalArgumentException("Khong tim thay SanPham co ma: " + ma);
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
