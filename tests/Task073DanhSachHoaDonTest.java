import java.time.LocalDate;
import services.*;
import models.*;
public class Task073DanhSachHoaDonTest {
    public static void main(String[] args) {
        DanhSachHoaDon ds = new DanhSachHoaDon();
        assert ds.getTatCa().isEmpty();
        HoaDon h = Task072HoaDonTest.hd("DS01");
        h.themChiTiet(Task072HoaDonTest.ct(2));
        ds.them(h);
        try { ds.them(Task072HoaDonTest.hd("ds01")); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        ds.timTheoMa("ds01").xoaChiTiet(Task072HoaDonTest.ct(1).getSanPham().getMaSanPham());
        assert ds.timTheoMa("DS01").getDanhSachChiTiet().size() == 1;
        assert ds.timTheoKhachHang(h.getMaKhachHang()).size() == 1;
        assert ds.locTheoNgay(h.getNgayLap(), h.getNgayLap()).size() == 1;
        assert ds.locTheoTrangThai(TrangThaiHoaDon.CHO_XAC_NHAN).size() == 1;
        assert ds.tongDoanhThu(h.getNgayLap(), h.getNgayLap()).signum() == 0;
        try { ds.locTheoNgay(LocalDate.MAX, LocalDate.MIN); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        h.xoaChiTiet(Task072HoaDonTest.ct(1).getSanPham().getMaSanPham());
        ds.suaHoaDonCho(h);
        assert ds.timTheoMa("DS01").getDanhSachChiTiet().isEmpty();
        System.out.println("Task073DanhSachHoaDonTest OK");
    }
}
