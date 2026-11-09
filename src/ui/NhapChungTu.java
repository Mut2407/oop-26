package ui;

import models.*;
import services.QuanLyCuaHang;

/** Mọi thay đổi chi tiết nằm trên bản đang soạn, chỉ ghi sổ khi kết thúc. */
public final class NhapChungTu {
    private final QuanLyCuaHang cuaHang;
    private final NhapLieuConsole nhap;

    public NhapChungTu(QuanLyCuaHang cuaHang, NhapLieuConsole nhap) {
        this.cuaHang = cuaHang;
        this.nhap = nhap;
    }

    public HoaDon nhapHoaDon(String maCu) {
        HoaDon cu = maCu == null ? null : cuaHang.getHoaDon().timTheoMa(maCu);
        String ma = maCu == null ? nhap.ma("Ma chung tu: ") : maCu;
        java.time.LocalDate ngay = nhap.ngay("Ngay lap yyyy-MM-dd: ");
        KhachHang doiTac = cuaHang.getKhachHang().timTheoMa(nhap.ma("Ma KhachHang: "));
        NhanVien nv = cuaHang.getNhanVien().timTheoMa(nhap.ma("Ma nhan vien: "));
        HoaDon moi = new HoaDon(ma, ngay, doiTac, nv);
        if (cu != null) {
            for (ChiTietHoaDon dong : cu.getChiTiet()) {
                moi.themChiTiet(dong);
            }
        }
        suaChiTiet(() -> moi.themChiTiet(nhapChiTietHoaDon()),
                () -> bao(moi.suaChiTiet(nhap.ma("Ma SP can sua: "), nhapChiTietHoaDon())),
                () -> bao(moi.xoaChiTiet(nhap.ma("Ma SP can xoa: "))),
                () -> System.out.println(hienThiHoaDon(moi)));
        return moi;
    }

    private ChiTietHoaDon nhapChiTietHoaDon() {
        String maSP = nhap.ma("Ma SP: ");
        SanPham sp = KiemTra.khacNull(cuaHang.getSanPham().timTheoMa(maSP), "San pham");
        System.out.println(sp.hienThiThongTin());
        return new ChiTietHoaDon(sp, nhap.soNguyen("So luong: ", 1), nhap.tien("DonGiaBan: "));
    }

    public static String hienThiHoaDon(HoaDon chungTu) {
        StringBuilder ketQua = new StringBuilder(chungTu.hienThiThongTin());
        for (ChiTietHoaDon dong : chungTu.getChiTiet()) {
            ketQua.append("\n  ").append(dong.getSanPham().getMaSanPham()).append(" x ")
                    .append(dong.getSoLuong()).append(" @ ").append(dong.getDonGiaBan().toPlainString())
                    .append(" = ").append(dong.tinhThanhTien().toPlainString());
        }
        return ketQua.toString();
    }

    public PhieuNhap nhapPhieuNhap(String maCu) {
        PhieuNhap cu = maCu == null ? null : cuaHang.getPhieuNhap().timTheoMa(maCu);
        String ma = maCu == null ? nhap.ma("Ma chung tu: ") : maCu;
        java.time.LocalDate ngay = nhap.ngay("Ngay lap yyyy-MM-dd: ");
        NhaCungCap doiTac = cuaHang.getNhaCungCap().timTheoMa(nhap.ma("Ma NhaCungCap: "));
        NhanVien nv = cuaHang.getNhanVien().timTheoMa(nhap.ma("Ma nhan vien: "));
        PhieuNhap moi = new PhieuNhap(ma, ngay, doiTac, nv);
        if (cu != null) {
            for (ChiTietPhieuNhap dong : cu.getChiTiet()) {
                moi.themChiTiet(dong);
            }
        }
        suaChiTiet(() -> moi.themChiTiet(nhapChiTietPhieuNhap()),
                () -> bao(moi.suaChiTiet(nhap.ma("Ma SP can sua: "), nhapChiTietPhieuNhap())),
                () -> bao(moi.xoaChiTiet(nhap.ma("Ma SP can xoa: "))),
                () -> System.out.println(hienThiPhieuNhap(moi)));
        return moi;
    }

    private ChiTietPhieuNhap nhapChiTietPhieuNhap() {
        String maSP = nhap.ma("Ma SP: ");
        SanPham sp = KiemTra.khacNull(cuaHang.getSanPham().timTheoMa(maSP), "San pham");
        System.out.println(sp.hienThiThongTin());
        return new ChiTietPhieuNhap(sp, nhap.soNguyen("So luong: ", 1), nhap.tien("DonGiaNhap: "));
    }

    public static String hienThiPhieuNhap(PhieuNhap chungTu) {
        StringBuilder ketQua = new StringBuilder(chungTu.hienThiThongTin());
        for (ChiTietPhieuNhap dong : chungTu.getChiTiet()) {
            ketQua.append("\n  ").append(dong.getSanPham().getMaSanPham()).append(" x ")
                    .append(dong.getSoLuong()).append(" @ ").append(dong.getDonGiaNhap().toPlainString())
                    .append(" = ").append(dong.tinhThanhTien().toPlainString());
        }
        return ketQua.toString();
    }

    private void suaChiTiet(Runnable them, Runnable sua, Runnable xoa, Runnable xem) {
        while (true) {
            System.out.println("1. Them dong  2. Sua dong  3. Xoa dong  4. Xem chi tiet  0. Ghi so");
            String chon = nhap.docDong("Chi tiet: ");
            if (chon.equals("0")) {
                return;
            }
            try {
                xuLyDong(chon, them, sua, xoa, xem);
            } catch (IllegalArgumentException e) {
                System.out.println("Dong khong hop le: " + e.getMessage());
            }
        }
    }

    private void xuLyDong(String chon, Runnable them, Runnable sua, Runnable xoa, Runnable xem) {
        switch (chon) {
            case "1": them.run(); break;
            case "2": sua.run(); break;
            case "3": xoa.run(); break;
            case "4": xem.run(); break;
            default: System.out.println("Lua chon khong hop le.");
        }
    }

    private void bao(boolean thanhCong) {
        System.out.println(thanhCong ? "Da cap nhat ban dang soan." : "Khong tim thay dong.");
    }
}
