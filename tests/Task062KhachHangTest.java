import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import models.KhachHang;
import services.DanhSachKhachHang;
import services.IQuanLyDanhSach;

public class Task062KhachHangTest {
    public static void main(String[] args) throws Exception {
        DanhSachKhachHang ds = new DanhSachKhachHang();
        IQuanLyDanhSach<KhachHang> api = ds;
        check(DanhSachKhachHang.class.getDeclaredField("ds").getType() == List.class);
        check(ds.getTatCa().isEmpty() && ds.getSoLuong() == 0);
        check(!api.suaTheoMa("NONE", null) && !api.xoaTheoMa("NONE"));
        check(ds.timTheoMa(null) == null && ds.thongKe().contains("Chua co du lieu"));

        KhachHang an = kh("Kh1", "Nguyễn Ánh", "0901234567", "Hà Nội");
        check(api.them(an));
        an.capNhatThongTin("Đã sửa", "0912345678", "Huế");
        check(ds.timTheoMa("kh1").getHoTen().equals("Nguyễn Ánh"));
        loi(() -> ds.them(kh("KH1", "Trùng", "0901234567", "Huế")));
        loi(() -> ds.them(null));
        loi(() -> ds.them(kh("", "An", "0901234567", "Huế")));
        loi(() -> ds.them(kh("K2", " ", "0901234567", "Huế")));
        loi(() -> ds.them(kh("K2", "An", "bad", "Huế")));
        loi(() -> ds.them(kh("K2", "An", "0901234567", " ")));

        ds.themNhieu(new KhachHang[]{kh("K2", "Đặng Bình", "0901234567", "Hà Nội"),
                kh("K3", "Trần Chi", "+84923456789", "Huế")});
        check(ds.getSoLuong() == 3);
        loi(() -> ds.themNhieu(new KhachHang[]{kh("K4", "Mới", "0934567890", "Huế"), null}));
        loi(() -> ds.themNhieu(new KhachHang[]{kh("K4", "Mới", "0934567890", "Huế"),
                kh("k4", "Trùng", "0945678901", "Huế")}));
        loi(() -> ds.nhapDanhSach(new KhachHang[]{an, an}));
        check(ds.getSoLuong() == 3 && ds.timTheoMa("K4") == null);
        check(ds.timTheoMa("Kh1").getHoTen().equals("Nguyễn Ánh"));

        check(ds.timGanDung("NGUYEN anh").size() == 1);
        check(ds.timGanDung("ĐẶNG").size() == 1);
        check(ds.timGanDung("k").size() == 3);
        check(ds.timTheoSoDienThoai("0901234567").size() == 2);
        check(ds.timTheoSoDienThoai("0999999999").isEmpty());
        check(ds.timKiem(null, "dang", "090", "ha noi", false).length == 1);
        check(ds.timKiem("k2", "đặng bình", null, "HÀ NỘI", true).length == 1);
        check(ds.timKiem(null, null, null, null, false).length == 3);
        loi(() -> ds.timGanDung(" "));
        loi(() -> ds.timTheoSoDienThoai("090"));
        check(ds.thongKe().contains("Hà Nội: 2 khach hang"));
        check(ds.thongKe().contains("090: 2 khach hang"));
        check(ds.thongKe().contains("849: 1 khach hang"));

        ds.getTatCa().get(0).capNhatThongTin("Sai", "0999999999", "Sai");
        ds.getDanhSach()[0].capNhatThongTin("Sai", "0999999999", "Sai");
        ds.timTheoMa("Kh1").capNhatThongTin("Sai", "0999999999", "Sai");
        ds.timGanDung("nguyen").get(0).capNhatThongTin("Sai", "0999999999", "Sai");
        ds.timTheoSoDienThoai("0901234567").get(0).capNhatThongTin("Sai", "0999999999", "Sai");
        ds.timKiem("Kh1", null, null, null, true)[0].capNhatThongTin("Sai", "0999999999", "Sai");
        check(ds.timTheoMa("Kh1").getHoTen().equals("Nguyễn Ánh"));

        check(api.suaTheoMa("kh1", kh("KH1", "Ánh mới", "0912345678", "Đà Nẵng")));
        check(ds.timTheoMa("KH1").getMaKhachHang().equals("Kh1"));
        loi(() -> api.suaTheoMa("KH1", kh("K99", "Sai mã", "0912345678", "Huế")));
        check(ds.sua("K2", "Bình mới", "0901234567", "Huế"));
        check(api.xoaTheoMa("K3") && !ds.xoa("K3"));

        Path dir = Files.createTempDirectory("task062-");
        Path file = dir.resolve("kh.txt");
        try {
            Files.writeString(file, "KHACH_HANG_V1\nKold\tKhách cũ\t0901234567\tNgõ 1\\tHà Nội\n");
            ds.docFile(file.toString());
            check(ds.getSoLuong() == 1 && ds.timTheoMa("Kold").getDiaChi().equals("Ngõ 1\tHà Nội"));
            ds.ghiFile(file.toString());
            DanhSachKhachHang read = new DanhSachKhachHang();
            read.docFile(file.toString());
            check(read.timTheoMa("Kold").getHoTen().equals("Khách cũ"));
            Files.writeString(file, "KHACH_HANG_V1\nKbad\tSai\t123\tHuế\n");
            try {
                ds.docFile(file.toString());
                throw new AssertionError("Corrupt input accepted");
            } catch (IOException expected) {
                check(ds.timTheoMa("Kold") != null);
            }
        } finally {
            Files.deleteIfExists(file);
            Files.delete(dir);
        }
        ds.nhapDanhSach(new KhachHang[]{an});
        check(ds.getSoLuong() == 1 && ds.timTheoMa("Kh1").getHoTen().equals("Đã sửa"));
        System.out.println("Task062KhachHangTest OK (7 groups: UML/empty, CRUD, batch, search/stats, copies, update/delete, files)");
    }

    private static KhachHang kh(String ma, String ten, String sdt, String diaChi) {
        return new KhachHang(ma, ten, sdt, diaChi);
    }

    private static void loi(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected invalid input rejection");
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}
