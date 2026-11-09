import java.math.BigDecimal;
import models.*;
import services.*;
public class DichVuKhoNhapHangTest {
    public static void main(String[] args) {
        PhieuNhap h = PhieuNhapTest.hd("KHO1");
        DanhSachSanPham sp = new DanhSachSanPham(DuLieu.taoDanhSachSanPham());
        SanPham a = sp.getDanhSach().get(0), b = sp.getDanhSach().get(1);
        h.themChiTiet(new ChiTietPhieuNhap(a, 1, BigDecimal.TEN));
        h.themChiTiet(new ChiTietPhieuNhap(b, Integer.MAX_VALUE, BigDecimal.ONE));
        DichVuKho kho = new DichVuKho(sp);
        try { kho.xacNhanNhapHang(h); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        assert sp.timTheoMa(a.getMaSanPham()).getSoLuongTon() == a.getSoLuongTon() && h.coTheChinhSua();
        h.suaChiTiet(b.getMaSanPham(), new ChiTietPhieuNhap(b, 1, BigDecimal.ONE));
        DanhSachPhieuNhap ds = new DanhSachPhieuNhap();
        ds.them(h);
        sp.ganPhieuNhap(ds);
        ds.xacNhanNhapHang(h.getMaPhieuNhap(), kho);
        assert sp.timTheoMa(a.getMaSanPham()).getSoLuongTon() == a.getSoLuongTon()+1;
        assert sp.timTheoMa(b.getMaSanPham()).getSoLuongTon() == b.getSoLuongTon()+1;
        assert !ds.timTheoMa(h.getMaPhieuNhap()).coTheChinhSua();
        try { ds.xacNhanNhapHang(h.getMaPhieuNhap(), kho); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        try { ds.timTheoMa(h.getMaPhieuNhap()).xoaChiTiet(a.getMaSanPham()); throw new AssertionError(); } catch (IllegalStateException expected) { }
        try { sp.xoaTheoMa(a.getMaSanPham()); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        try { sp.nhapBanDau(new SanPham[0]); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        DanhSachNhaCungCap ncc = new DanhSachNhaCungCap(DuLieu.taoDanhSachNhaCungCap());
        ncc.ganPhieuNhap(ds);
        try { ncc.xoaTheoMa(h.getMaNhaCungCap()); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        try { ncc.thayDuLieu(new DanhSachNhaCungCap()); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        assert ds.tongGiaTriNhap(h.getNgayNhap(),h.getNgayNhap()).compareTo(h.tinhTongTienNhap()) == 0;
        System.out.println("DichVuKhoNhapHangTest OK");
    }
}
