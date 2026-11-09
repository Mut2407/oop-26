import models.NhanVien;
import services.DanhSachNhanVien;

public class DanhSachNhanVienTest {
    public static void main(String[] args) throws Exception {
        DanhSachNhanVien ds = new DanhSachNhanVien();
        check(DanhSachNhanVien.class.getDeclaredField("danhSach").getType() == NhanVien[].class);
        check(ds.getSoLuong() == 0 && ds.getDanhSach().length == 0);
        check(ds.timTheoMa(null) == null && ds.timGanDung("none").length == 0);
        check(!ds.xoa("none") && !ds.sua("none", "Tên", "Sáng", "0901234567"));
        loi(() -> ds.them(null));
        loi(() -> ds.timGanDung(" "));
        for (int i = 0; i < 37; i++) {
            ds.them(new NhanVien("NV" + i, "Nguyễn Đặng " + i, "0901234567", i % 2 == 0 ? "Sáng" : "Tối"),
                    "Địa chỉ " + i);
        }
        check(ds.getSoLuong() == 37 && ds.getDanhSach().length == 37);
        check(ds.timTheoMa("nv36").getMaNhanVien().equals("NV36"));
        check(ds.thongKeTheoCa().contains("Sáng: 19 nhan vien"));
        check(ds.thongKeTheoCa().contains("Tối: 18 nhan vien"));
        check(ds.thongKe().contains("Địa chỉ 36: 1 nhan vien"));
        loi(() -> ds.them(new NhanVien("nv1", "Trùng", "0912345678", "Chiều")));
        loi(() -> ds.them(new NhanVien("", "Sai", "0912345678", "Chiều")));
        loi(() -> ds.themNhieu(new NhanVien[]{new NhanVien("NEW", "Mới", "0912345678", "Chiều"), null}));
        loi(() -> ds.nhapDanhSach(new NhanVien[]{ds.timTheoMa("NV1"), ds.timTheoMa("NV1")}));
        check(ds.getSoLuong() == 37 && ds.getDiaChiCu("NV1").equals("Địa chỉ 1"));
        check(ds.timGanDung("NGUYEN dang").length == 37);
        check(ds.timGanDung("NV36").length == 1);
        check(ds.timKiem("nv36", "nguyen dang 36", null, "0901234567", "dia chi 36", true).length == 1);
        check(ds.timKiem(null, "đặng", "sang", "090", null, false).length == 19);
        ds.getDanhSach()[0].doiCaLamViec("Khác");
        ds.timGanDung("NV36")[0].capNhatLienHe("0999999999");
        ds.timKiem("NV1", null, null, null, null, true)[0].doiCaLamViec("Khác");
        check(ds.timTheoMa("NV0").getCaLamViec().equals("Sáng"));
        check(ds.timTheoMa("NV36").getSoDienThoai().equals("0901234567"));
        check(ds.timTheoMa("NV1").getCaLamViec().equals("Tối"));
        String truoc = ds.timTheoMa("NV1").hienThiThongTin();
        loi(() -> ds.sua("NV1", "Tên", " ", "0901234567"));
        loi(() -> ds.sua("NV1", " ", "Chiều", "0901234567"));
        loi(() -> ds.sua("NV1", "Tên", "Chiều", "bad"));
        check(ds.timTheoMa("NV1").hienThiThongTin().equals(truoc));
        check(ds.sua("nv1", "Bình", "Chiều", "0912345678"));
        check(ds.timTheoMa("NV1").getMaNhanVien().equals("NV1"));
        check(ds.timTheoMa("NV1").getHoTen().equals("Bình"));
        check(ds.getDiaChiCu("NV1").equals("Địa chỉ 1"));
        check(ds.xoa("NV0") && ds.getDiaChiCu("NV0") == null);
        check(ds.getDiaChiCu("NV1").equals("Địa chỉ 1"));
        ds.nhapDanhSach(new NhanVien[]{ds.timTheoMa("NV1")});
        check(ds.getSoLuong() == 1 && ds.getDiaChiCuTheoMa().size() == 1);
        NhanVien input = new NhanVien("NVNEW", "Mới", "0901234567", "Sáng");
        ds.themNhieu(new NhanVien[]{input});
        input.doiCaLamViec("Khác");
        check(ds.timTheoMa("NVNEW").getCaLamViec().equals("Sáng"));
        check(ds.getDiaChiCu("NVNEW") == null);
        check(ds.thongKe().contains("Metadata dia chi chua day du"));
        System.out.println("DanhSachNhanVienTest OK (6 groups: empty, growth, batches, search/stats, copies, CRUD/metadata)");
    }

    private static void loi(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected rejection");
    }

    private static void check(boolean value) {
        if (!value) {
            throw new AssertionError();
        }
    }
}
