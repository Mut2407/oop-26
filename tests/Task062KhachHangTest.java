import models.KhachHang;
import services.DanhSachKhachHang;
import services.IQuanLyDanhSach;

public class Task062KhachHangTest {
    public static void main(String[] args) {
        DanhSachKhachHang ds = new DanhSachKhachHang();
        IQuanLyDanhSach<KhachHang> api = ds;
        KhachHang kh = new KhachHang("K1", "Nguyễn Ánh", "0901234567", "Hà Nội");
        check(api.them(kh));
        kh.capNhatThongTin("Da sua", "0912345678", "Hue");
        check(ds.timTheoMa("k1").getHoTen().equals("Nguyễn Ánh"));
        loi(() -> api.them(new KhachHang("k1", "Trung", "0901234567", "Hue")));
        loi(() -> api.them(null));
        check(api.timGanDung("nguyen anh").size() == 1);
        ds.timTheoMa("K1").capNhatThongTin("Sai", "0999999999", "Sai");
        check(ds.timTheoMa("K1").getHoTen().equals("Nguyễn Ánh"));
        check(api.suaTheoMa("k1", new KhachHang("K1", "Ten moi", "0912345678", "Hue")));
        loi(() -> api.suaTheoMa("K1", new KhachHang("K2", "Sai ma", "0901234567", "Hue")));
        check(ds.timTheoMa("K1").getHoTen().equals("Ten moi"));
        check(api.xoaTheoMa("k1") && ds.getTatCa().isEmpty());
        check(!api.xoaTheoMa("NONE") && !api.suaTheoMa("NONE", null));
        System.out.println("Task062KhachHangTest OK (basic CRUD, duplicate IDs, search, defensive copies)");
    }
    private static void loi(Runnable action) {
        try { action.run(); } catch (IllegalArgumentException e) { return; }
        throw new AssertionError("Invalid input accepted");
    }
    private static void check(boolean value) { if (!value) throw new AssertionError(); }
}
