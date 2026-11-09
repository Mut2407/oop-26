import java.math.BigDecimal;
import models.*;
public class ChiTietPhieuNhapTest {
    public static void main(String[] args) {
        DuLieu.khoiTaoDanhMuc();
        SanPham sp = DuLieu.taoDanhSachSanPham()[0];
        ChiTietPhieuNhap ct = new ChiTietPhieuNhap(sp, 3, new BigDecimal("0.10"));
        assert ct.thanhTienNhap().compareTo(new BigDecimal("0.30")) == 0;
        sp.capNhatGiaBan(BigDecimal.TEN);
        ct.getSanPham().giamTon(1);
        assert ct.getSoLuong() == 3 && ct.getDonGiaNhap().compareTo(new BigDecimal("0.10")) == 0;
        assert ct.getSanPham().getSoLuongTon() == sp.getSoLuongTon();
        for (int n : new int[]{0, -1}) {
            try { new ChiTietPhieuNhap(sp, n, BigDecimal.ZERO); throw new AssertionError(); }
            catch (IllegalArgumentException expected) { }
        }
        try { new ChiTietPhieuNhap(null, 1, BigDecimal.ONE); throw new AssertionError(); }
        catch (IllegalArgumentException expected) { }
        try { new ChiTietPhieuNhap(sp, 1, new BigDecimal("-1")); throw new AssertionError(); }
        catch (IllegalArgumentException expected) { }
        assert new ChiTietPhieuNhap(sp, 1, BigDecimal.ZERO).thanhTienNhap().signum() == 0;
        System.out.println("ChiTietPhieuNhapTest OK");
    }
}
