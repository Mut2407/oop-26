import java.math.BigDecimal;
import models.*;
import services.*;

public final class NghiepVuTest {
    public static void main(String[] args) {
        giaoDichVaChenhlech();
        thatBaiKhongDoiDuLieu();
        suaPhieuDoiSanPham();
        bienSoLuong();
        thamChieuVaBanSao();
        thongKeVaGiaLichSu();
        System.out.println("NghiepVuTest OK: " + TestSupport.assertions + " assertions");
    }

    private static void giaoDichVaChenhlech() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        TestSupport.bang(10, ql.traCuuTon("sp1"));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 3));
        TestSupport.bang(7, ql.traCuuTon("SP1"));
        HoaDon sua = ql.getHoaDon().timTheoMa("HD1");
        sua.suaChiTiet("SP1", new ChiTietHoaDon(ql.getSanPham().timTheoMa("SP1"), 5, new BigDecimal("120")));
        TestSupport.check(ql.suaHoaDon("HD1", sua));
        TestSupport.bang(5, ql.traCuuTon("SP1"));
        TestSupport.check(ql.xoaHoaDon("HD1"));
        TestSupport.bang(10, ql.traCuuTon("SP1"));
        TestSupport.check(ql.suaPhieuNhap("PN1", TestSupport.phieuNhap(ql, "PN1", "SP1", 8)));
        TestSupport.bang(8, ql.traCuuTon("SP1"));
        TestSupport.check(ql.xoaPhieuNhap("PN1"));
        TestSupport.bang(0, ql.traCuuTon("SP1"));
        TestSupport.check(!ql.xoaHoaDon("KHONGCO"));
        TestSupport.check(!ql.xoaPhieuNhap("KHONGCO"));
    }

    private static void thatBaiKhongDoiDuLieu() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN2", "SP2", 2));
        HoaDon thieu = TestSupport.hoaDon(ql, "HD1", "SP1", 3);
        thieu.themChiTiet(new ChiTietHoaDon(ql.getSanPham().timTheoMa("SP2"), 3, BigDecimal.ONE));
        TestSupport.loi(() -> ql.themHoaDon(thieu));
        TestSupport.bang(10, ql.traCuuTon("SP1"));
        TestSupport.bang(2, ql.traCuuTon("SP2"));
        TestSupport.bang(0, ql.getHoaDon().getSoLuong());
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 7));
        TestSupport.loi(() -> ql.xoaPhieuNhap("PN1"));
        TestSupport.loi(() -> ql.suaPhieuNhap("PN1", TestSupport.phieuNhap(ql, "PN1", "SP1", 6)));
        TestSupport.bang(3, ql.traCuuTon("SP1"));
        TestSupport.bang(10, ql.getPhieuNhap().timTheoMa("PN1").timChiTiet("SP1").getSoLuong());
        TestSupport.loi(() -> ql.suaHoaDon("HD1", TestSupport.hoaDon(ql, "HD1", "SP2", 3)));
        TestSupport.bang(3, ql.traCuuTon("SP1"));
        TestSupport.bang(7, ql.getHoaDon().timTheoMa("HD1").timChiTiet("SP1").getSoLuong());
        TestSupport.check(ql.suaHoaDon("HD1", TestSupport.hoaDon(ql, "HD1", "SP2", 2)));
        TestSupport.bang(10, ql.traCuuTon("SP1"));
        TestSupport.bang(0, ql.traCuuTon("SP2"));
        TestSupport.loi(() -> ql.themHoaDon(TestSupport.hoaDon(ql, "hd1", "SP1", 1)));
        TestSupport.bang(10, ql.traCuuTon("SP1"));
        TestSupport.loi(() -> ql.suaHoaDon("HD1", TestSupport.hoaDon(ql, "HD2", "SP1", 1)));
        TestSupport.bang(10, ql.traCuuTon("SP1"));
        TestSupport.loi(() -> ql.themHoaDon(new HoaDon("EMPTY", TestSupport.NGAY,
                ql.getKhachHang().timTheoMa("KH1"), ql.getNhanVien().timTheoMa("NV1"))));
        PhieuNhap loi = TestSupport.phieuNhap(ql, "PN3", "SP1", 1);
        loi.setNhaCungCap(new NhaCungCap("NCC404", "Khong co", "0901234567", "HCM"));
        TestSupport.loi(() -> ql.themPhieuNhap(loi));
        TestSupport.bang(10, ql.traCuuTon("SP1"));
    }

    private static void suaPhieuDoiSanPham() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.suaPhieuNhap("PN1", TestSupport.phieuNhap(ql, "PN1", "SP2", 4));
        TestSupport.bang(0, ql.traCuuTon("SP1"));
        TestSupport.bang(4, ql.traCuuTon("SP2"));
        PhieuNhap moi = TestSupport.phieuNhap(ql, "PN1", "SP2", 6);
        moi.themChiTiet(new ChiTietPhieuNhap(ql.getSanPham().timTheoMa("SP1"), 5, BigDecimal.ONE));
        ql.suaPhieuNhap("PN1", moi);
        moi.xoaChiTiet("SP1");
        TestSupport.bang(5, ql.traCuuTon("SP1"));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 5));
        TestSupport.loi(() -> ql.xoaPhieuNhap("PN1"));
        TestSupport.bang(6, ql.traCuuTon("SP2"));
        TestSupport.bang(2, ql.getPhieuNhap().timTheoMa("PN1").getSoLuongChiTiet());
    }

    private static void bienSoLuong() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PNMAX", "SP1", Integer.MAX_VALUE));
        TestSupport.loi(() -> ql.themPhieuNhap(TestSupport.phieuNhap(ql, "OVER", "SP1", 1)));
        TestSupport.bang(Integer.MAX_VALUE, ql.traCuuTon("SP1"));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HDMAX", "SP1", Integer.MAX_VALUE));
        TestSupport.bang(0, ql.traCuuTon("SP1"));
        ql.xoaHoaDon("HDMAX");
        ql.xoaPhieuNhap("PNMAX");
        TestSupport.bang(0, ql.traCuuTon("SP1"));
    }

    private static void thamChieuVaBanSao() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        TestSupport.loi(() -> ql.xoaLoaiSP("L1"));
        TestSupport.loi(() -> ql.themSanPham(TestSupport.giay("SP3").voiTonKho(10)));
        TestSupport.loi(() -> ql.suaSanPham("SP1", TestSupport.giay("SP1").voiTonKho(10)));
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 2));
        TestSupport.loi(() -> ql.xoaKhachHang("KH1"));
        TestSupport.loi(() -> ql.xoaNhanVien("NV1"));
        TestSupport.loi(() -> ql.xoaNhaCungCap("NCC1"));
        TestSupport.loi(() -> ql.xoaSanPham("SP1"));
        HoaDon banSao = ql.getHoaDon().timTheoMa("HD1");
        banSao.xoaChiTiet("SP1");
        ql.getSanPham().xoa("SP1");
        TestSupport.bang(8, ql.traCuuTon("SP1"));
        TestSupport.bang(1, ql.getHoaDon().timTheoMa("HD1").getSoLuongChiTiet());
        ql.suaLoaiSP("L1", new LoaiSP("L1", "Loai moi", "Mo ta moi"));
        TestSupport.bang("Loai moi", ql.getSanPham().timTheoMa("SP1").getLoaiSP().getTenLoaiSP());
        ql.xoaHoaDon("HD1");
        ql.xoaPhieuNhap("PN1");
        TestSupport.check(ql.xoaSanPham("SP1"));
        ql.xoaSanPham("SP2");
        TestSupport.check(ql.xoaLoaiSP("L1"));
        TestSupport.check(ql.xoaKhachHang("KH1"));
        TestSupport.check(ql.xoaNhanVien("NV1"));
        TestSupport.check(ql.xoaNhaCungCap("NCC1"));
    }

    private static void thongKeVaGiaLichSu() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        TestSupport.tien("0", ql.thongKeTienBan());
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 3));
        SanPham sp = ql.getSanPham().timTheoMa("SP1");
        sp.setGiaBan(new BigDecimal("999.99"));
        ql.suaSanPham("SP1", sp);
        TestSupport.tien("300.30", ql.thongKeTienBan());
        TestSupport.tien("602.50", ql.thongKeTienNhap());
        TestSupport.tien("300.30", ql.thongKeTienBan(TestSupport.NGAY, TestSupport.NGAY));
        TestSupport.tien("0", ql.thongKeTienBan(TestSupport.NGAY.plusDays(1), TestSupport.NGAY.plusDays(2)));
        TestSupport.tien("602.50", ql.thongKeTienNhap(TestSupport.NGAY, TestSupport.NGAY));
        TestSupport.loi(() -> ql.thongKeTienBan(TestSupport.NGAY.plusDays(1), TestSupport.NGAY));
        TestSupport.loi(() -> ql.thongKeTienNhap(null, TestSupport.NGAY));
    }
}
