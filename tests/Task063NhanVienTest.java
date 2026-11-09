import models.NhanVien;
import services.DanhSachNhanVien;

public class Task063NhanVienTest {
    public static void main(String[] args) throws Exception {
        DanhSachNhanVien ds = new DanhSachNhanVien();
        check(DanhSachNhanVien.class.getDeclaredField("danhSach").getType() == NhanVien[].class);
        for (int i = 0; i < 37; i++) {
            ds.them(new NhanVien("NV" + i, "Nguyễn Đặng " + i, "0901234567", "Sang"), "Dia chi " + i);
        }
        check(ds.getSoLuong() == 37 && ds.TimKiemTheoMaHoacTen("nguyen dang").length == 37);
        loi(() -> ds.them(new NhanVien("nv1", "Trung", "0912345678", "Chieu")));
        loi(() -> ds.them(null));
        String truoc = ds.CheckMaTrung("NV1").hienThiThongTin();
        loi(() -> ds.sua("NV1", "Ten", " ", "0901234567"));
        loi(() -> ds.sua("NV1", "Ten", "Chieu", "bad"));
        check(ds.CheckMaTrung("NV1").hienThiThongTin().equals(truoc));
        check(ds.sua("nv1", "Ten moi", "Chieu", "0912345678"));
        check(ds.getDiaChiCu("NV1").equals("Dia chi 1"));
        ds.getDanhSach()[0].doiCaLamViec("Khac");
        check(ds.CheckMaTrung("NV0").getCaLamViec().equals("Sang"));
        check(ds.xoa("NV0") && ds.getDiaChiCu("NV0") == null && ds.getSoLuong() == 36);
        check(!ds.xoa("NONE") && !ds.sua("NONE", "Ten", "Sang", "0901234567"));
        System.out.println("Task063NhanVienTest OK (array growth, basic CRUD, search, validation, legacy addresses)");
    }
    private static void loi(Runnable action) {
        try { action.run(); } catch (IllegalArgumentException e) { return; }
        throw new AssertionError("Invalid input accepted");
    }
    private static void check(boolean value) { if (!value) throw new AssertionError(); }
}
