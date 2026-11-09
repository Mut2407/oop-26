import java.math.BigDecimal;
import java.time.LocalDate;
import models.*;
import services.*;

final class TestSupport {
    static int assertions;
    static final LocalDate NGAY = LocalDate.of(2026, 11, 9);

    static void check(boolean dieuKien) {
        assertions++;
        if (!dieuKien) {
            throw new AssertionError("Check " + assertions + " failed");
        }
    }

    static void bang(Object kyVong, Object thucTe) {
        check(java.util.Objects.equals(kyVong, thucTe));
    }

    static void tien(String kyVong, BigDecimal thucTe) {
        check(new BigDecimal(kyVong).compareTo(thucTe) == 0);
    }

    static void loi(Runnable hanhDong) {
        try {
            hanhDong.run();
        } catch (IllegalArgumentException e) {
            assertions++;
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException");
    }

    static QuanLyCuaHang cuaHang() {
        QuanLyCuaHang ql = new QuanLyCuaHang();
        ql.themLoaiSP(new LoaiSP("L1", "Giay", ""));
        ql.themKhachHang(new KhachHang("KH1", "Khach", "0901234567", "HCM"));
        ql.themNhanVien(new NhanVien("NV1", "Nhan vien", "0901234568", "Sang"));
        ql.themNhaCungCap(new NhaCungCap("NCC1", "Nha cung cap", "0901234569", "HCM"));
        ql.themSanPham(giay("SP1"));
        ql.themSanPham(giay("SP2"));
        return ql;
    }

    static GiayTheThao giay(String ma) {
        return new GiayTheThao(ma, "Giày chạy bộ", "Nike", new BigDecimal("100.10"),
                new LoaiSP("L1", "Giay", ""), 42.5, "Den", "Vai", "Cao su");
    }

    static HoaDon hoaDon(QuanLyCuaHang ql, String ma, String maSP, int soLuong) {
        HoaDon hd = new HoaDon(ma, NGAY, ql.getKhachHang().timTheoMa("KH1"),
                ql.getNhanVien().timTheoMa("NV1"));
        hd.themChiTiet(new ChiTietHoaDon(ql.getSanPham().timTheoMa(maSP), soLuong, new BigDecimal("100.10")));
        return hd;
    }

    static PhieuNhap phieuNhap(QuanLyCuaHang ql, String ma, String maSP, int soLuong) {
        PhieuNhap pn = new PhieuNhap(ma, NGAY, ql.getNhaCungCap().timTheoMa("NCC1"),
                ql.getNhanVien().timTheoMa("NV1"));
        pn.themChiTiet(new ChiTietPhieuNhap(ql.getSanPham().timTheoMa(maSP), soLuong, new BigDecimal("60.25")));
        return pn;
    }
}
