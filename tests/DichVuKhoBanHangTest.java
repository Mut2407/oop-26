import java.math.BigDecimal;
import models.*;
import services.*;
public class DichVuKhoBanHangTest {
    public static void main(String[] args) {
        HoaDon h = HoaDonTest.hd("KHO1");
        DanhSachSanPham sp = new DanhSachSanPham(DuLieu.taoDanhSachSanPham());
        SanPham a = sp.getDanhSach().get(0), b = sp.getDanhSach().get(1);
        h.themChiTiet(new ChiTietHoaDon(a, 1, BigDecimal.TEN));
        h.themChiTiet(new ChiTietHoaDon(b, b.getSoLuongTon()+1, BigDecimal.ONE));
        DichVuKho kho = new DichVuKho(sp);
        try { kho.xacNhanBanHang(h); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        assert sp.timTheoMa(a.getMaSanPham()).getSoLuongTon() == a.getSoLuongTon() && h.coTheChinhSua();
        h.suaChiTiet(b.getMaSanPham(), new ChiTietHoaDon(b, b.getSoLuongTon(), BigDecimal.ONE));
        DanhSachHoaDon ds = new DanhSachHoaDon();
        ds.them(h);
        sp.ganHoaDon(ds);
        ds.xacNhanBanHang(h.getMaHoaDon(), kho);
        assert sp.timTheoMa(a.getMaSanPham()).getSoLuongTon() == a.getSoLuongTon()-1;
        assert sp.timTheoMa(b.getMaSanPham()).getSoLuongTon() == 0;
        assert !ds.timTheoMa(h.getMaHoaDon()).coTheChinhSua();
        try { ds.xacNhanBanHang(h.getMaHoaDon(), kho); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        try { ds.timTheoMa(h.getMaHoaDon()).xoaChiTiet(a.getMaSanPham()); throw new AssertionError(); } catch (IllegalStateException expected) { }
        try { sp.xoaTheoMa(a.getMaSanPham()); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        try { sp.nhapBanDau(new SanPham[0]); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        DanhSachKhachHang kh = new DanhSachKhachHang(DuLieu.taoDanhSachKhachHang()); kh.ganHoaDon(ds);
        try { kh.xoaTheoMa(h.getMaKhachHang()); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        try { kh.nhapDanhSach(new KhachHang[0]); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        DanhSachNhanVien nv = new DanhSachNhanVien(DuLieu.taoDanhSachNhanVien(), DuLieu.taoDiaChiNhanVienCu()); nv.ganHoaDon(ds);
        try { nv.nhapDanhSach(new NhanVien[0]); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        assert nv.getDiaChiCu(h.getMaNhanVien()) != null;
        assert ds.tongDoanhThu(h.getNgayLap(),h.getNgayLap()).compareTo(h.tinhTongTien()) == 0;
        System.out.println("DichVuKhoBanHangTest OK");
    }
}
