import java.math.BigDecimal;
import models.*;
import services.*;

public class DanhSachSanPhamTest {
    public static void main(String[] args) {
        DanhMuc dm = new DanhMuc("GIAY", "Giay", "");
        DuLieu.dangKyDanhMuc(dm);
        DanhSachSanPham ds = new DanhSachSanPham(0);
        check(ds.thongKeTonKho().contains("Tong so luong ton: 0"));
        SanPham giay = giay("G1", 2, dm);
        IQuanLyDanhSach<SanPham> quanLy = ds;
        check(quanLy.them(giay));
        check(ds.timGanDung("chay bo").size() == 1 && ds.timTheoMa("g1") instanceof GiayTheThao);
        loi(() -> ds.them(giay));
        loi(() -> ds.them(null));
        loi(() -> ds.suaTheoMa("G1", giay("G2", 3, dm)));
        check(ds.suaTheoMa("G1", giay("G1", 3, dm)));
        check(ds.thongKeTonKho().contains("Tong so luong ton: 3"));
        ds.timTheoMa("G1").capNhatGiaBan(BigDecimal.ONE);
        check(ds.timTheoMa("G1").getGiaBan().equals(BigDecimal.TEN));
        ds.them(giay("G2", Integer.MAX_VALUE, dm));
        ds.them(giay("G3", Integer.MAX_VALUE, dm));
        check(ds.thongKeTonKho().contains("Tong so luong ton: 4294967297"));
        check(ds.xoaTheoMa("G1") && ds.timTheoMa("G1") == null);
        check(!ds.xoaTheoMa("NONE") && !ds.suaTheoMa("NONE", giay));
        loi(() -> giay("BAD", -1, dm));
        System.out.println("DanhSachSanPhamTest OK (CRUD, stock totals/overflow, validation, polymorphism)");
    }
    private static SanPham giay(String ma, int ton, DanhMuc dm) {
        return new GiayTheThao(ma, "Giày chạy bộ", "Hang", BigDecimal.TEN, ton, "Den", null, dm, "42", "De");
    }
    private static void loi(Runnable action) {
        try { action.run(); } catch (IllegalArgumentException e) { return; }
        throw new AssertionError("Invalid input accepted");
    }
    private static void check(boolean value) { if (!value) throw new AssertionError(); }
}
