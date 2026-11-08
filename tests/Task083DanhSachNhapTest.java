import java.time.LocalDate;
import services.*;
import models.*;
public class Task083DanhSachNhapTest {
    public static void main(String[] args) {
        DanhSachPhieuNhap ds = new DanhSachPhieuNhap();
        assert ds.getTatCa().isEmpty();
        PhieuNhap h = Task082PhieuNhapTest.hd("DS01");
        h.themChiTiet(Task082PhieuNhapTest.ct(2));
        ds.them(h);
        try { ds.them(Task082PhieuNhapTest.hd("ds01")); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        ds.timTheoMa("ds01").xoaChiTiet(Task082PhieuNhapTest.ct(1).getSanPham().getMaSanPham());
        assert ds.timTheoMa("DS01").getDanhSachChiTiet().size() == 1;
        assert ds.timTheoNhaCungCap(h.getMaNhaCungCap()).size() == 1;
        assert ds.locTheoNgay(h.getNgayNhap(), h.getNgayNhap()).size() == 1;
        assert ds.locTheoTrangThai(TrangThaiPhieuNhap.CHO_XAC_NHAN).size() == 1;
        assert ds.tongGiaTriNhap(h.getNgayNhap(), h.getNgayNhap()).signum() == 0;
        try { ds.locTheoNgay(LocalDate.MAX, LocalDate.MIN); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        h.xoaChiTiet(Task082PhieuNhapTest.ct(1).getSanPham().getMaSanPham());
        ds.suaPhieuNhapCho(h);
        assert ds.timTheoMa("DS01").getDanhSachChiTiet().isEmpty();
        System.out.println("Task083DanhSachNhapTest OK");
    }
}
