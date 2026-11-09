import java.math.BigDecimal;
import java.time.LocalDate;
import models.*;
public class PhieuNhapTest {
    static PhieuNhap hd(String ma) {
        DuLieu.khoiTaoDanhMuc();
        return new PhieuNhap(ma, LocalDate.of(2026, 11, 1), DuLieu.taoDanhSachNhaCungCap()[0], DuLieu.taoDanhSachNhanVien()[0]);
    }
    static ChiTietPhieuNhap ct(int n) {
        return new ChiTietPhieuNhap(DuLieu.taoDanhSachSanPham()[0], n, new BigDecimal("1.25"));
    }
    public static void main(String[] args) throws Exception {
        PhieuNhap hd = hd("PN100");
        assert hd.coTheChinhSua() && hd.tinhTongTienNhap().signum() == 0;
        hd.themChiTiet(ct(2));
        try { hd.themChiTiet(ct(1)); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        assert hd.tinhTongTienNhap().compareTo(new BigDecimal("2.50")) == 0;
        hd.suaChiTiet(ct(1).getSanPham().getMaSanPham(), ct(4));
        assert hd.tinhTongTienNhap().compareTo(new BigDecimal("5.00")) == 0;
        hd.banSao().xoaChiTiet(ct(1).getSanPham().getMaSanPham());
        assert hd.getDanhSachChiTiet().size() == 1;
        try { hd.getDanhSachChiTiet().clear(); throw new AssertionError(); } catch (UnsupportedOperationException expected) { }
        assert !java.lang.reflect.Modifier.isPublic(PhieuNhap.class.getDeclaredMethod("danhDauDaNhapKho").getModifiers());
        assert Integer.parseInt(PhieuNhap.taoMaTuDong().substring(2)) > 100;
        assert !hd.xoaChiTiet("missing");
        assert hd.xoaChiTiet(ct(1).getSanPham().getMaSanPham());
        System.out.println("PhieuNhapTest OK");
    }
}
