import java.math.BigDecimal;
import java.time.LocalDate;
import models.*;
public class Task072HoaDonTest {
    static HoaDon hd(String ma) {
        DuLieu.khoiTaoDanhMuc();
        return new HoaDon(ma, LocalDate.of(2026, 11, 1), DuLieu.taoDanhSachKhachHang()[0], DuLieu.taoDanhSachNhanVien()[0]);
    }
    static ChiTietHoaDon ct(int n) {
        return new ChiTietHoaDon(DuLieu.taoDanhSachSanPham()[0], n, new BigDecimal("1.25"));
    }
    public static void main(String[] args) throws Exception {
        HoaDon hd = hd("HD100");
        assert hd.coTheChinhSua() && hd.tinhTongTien().signum() == 0;
        hd.themChiTiet(ct(2));
        try { hd.themChiTiet(ct(1)); throw new AssertionError(); } catch (IllegalArgumentException expected) { }
        assert hd.tinhTongTien().compareTo(new BigDecimal("2.50")) == 0;
        hd.suaChiTiet(ct(1).getSanPham().getMaSanPham(), ct(4));
        assert hd.tinhTongTien().compareTo(new BigDecimal("5.00")) == 0;
        hd.banSao().xoaChiTiet(ct(1).getSanPham().getMaSanPham());
        assert hd.getDanhSachChiTiet().size() == 1;
        try { hd.getDanhSachChiTiet().clear(); throw new AssertionError(); } catch (UnsupportedOperationException expected) { }
        assert !java.lang.reflect.Modifier.isPublic(HoaDon.class.getDeclaredMethod("danhDauDaXacNhan").getModifiers());
        assert Integer.parseInt(HoaDon.taoMaTuDong().substring(2)) > 100;
        assert !hd.xoaChiTiet("missing");
        assert hd.xoaChiTiet(ct(1).getSanPham().getMaSanPham());
        System.out.println("Task072HoaDonTest OK");
    }
}
