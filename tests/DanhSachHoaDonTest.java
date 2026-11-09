import java.time.LocalDate;
import services.*;
import models.*;
public class DanhSachHoaDonTest {
    public static void main(String[] args) {
        DanhSachHoaDon ds = new DanhSachHoaDon();
        assert ds.getTatCa().isEmpty();
        HoaDon h = HoaDonTest.hd("DS01");
        h.themChiTiet(HoaDonTest.ct(2));
        ds.them(h);
        try { ds.them(HoaDonTest.hd("ds01")); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        ds.timTheoMa("ds01").xoaChiTiet(HoaDonTest.ct(1).getSanPham().getMaSanPham());
        assert ds.timTheoMa("DS01").getDanhSachChiTiet().size() == 1;
        assert ds.timTheoKhachHang(h.getMaKhachHang()).size() == 1;
        assert ds.locTheoNgay(h.getNgayLap(), h.getNgayLap()).size() == 1;
        assert ds.locTheoTrangThai(TrangThaiHoaDon.CHO_XAC_NHAN).size() == 1;
        assert ds.tongDoanhThu(h.getNgayLap(), h.getNgayLap()).signum() == 0;
        try { ds.locTheoNgay(LocalDate.MAX, LocalDate.MIN); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        h.xoaChiTiet(HoaDonTest.ct(1).getSanPham().getMaSanPham());
        ds.suaHoaDonCho(h);
        assert ds.timTheoMa("DS01").getDanhSachChiTiet().isEmpty();
        System.out.println("DanhSachHoaDonTest OK");
    }
}
