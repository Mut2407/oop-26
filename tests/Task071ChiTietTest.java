import java.math.BigDecimal;
import models.*;
public class Task071ChiTietTest {
    public static void main(String[] args) {
        DuLieu.khoiTaoDanhMuc();
        SanPham sp = DuLieu.taoDanhSachSanPham()[0];
        ChiTietHoaDon ct = new ChiTietHoaDon(sp, 3, new BigDecimal("0.10"));
        assert ct.thanhTien().compareTo(new BigDecimal("0.30")) == 0;
        sp.capNhatGiaBan(BigDecimal.TEN);
        ct.getSanPham().giamTon(1);
        assert ct.getSoLuong() == 3 && ct.getDonGiaBan().compareTo(new BigDecimal("0.10")) == 0;
        assert ct.getSanPham().getSoLuongTon() == sp.getSoLuongTon();
        for (int n : new int[]{0, -1}) {
            try { new ChiTietHoaDon(sp, n, BigDecimal.ZERO); throw new AssertionError(); }
            catch (IllegalArgumentException expected) { }
        }
        try { new ChiTietHoaDon(null, 1, BigDecimal.ONE); throw new AssertionError(); }
        catch (IllegalArgumentException expected) { }
        try { new ChiTietHoaDon(sp, 1, new BigDecimal("-1")); throw new AssertionError(); }
        catch (IllegalArgumentException expected) { }
        assert new ChiTietHoaDon(sp, 1, BigDecimal.ZERO).thanhTien().signum() == 0;
        System.out.println("Task071ChiTietTest OK");
    }
}
