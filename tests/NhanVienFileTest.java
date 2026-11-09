import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import models.NhanVien;
import services.DanhSachNhanVien;
import services.LuuTruFileText;

public class NhanVienFileTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("nhan-vien-file-");
        String cu = "\uFEFFmaNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi\n"
                + "NV1\tNguyễn An\tSáng\t0901234567\t  Hà Nội\\tNgõ 1\\\\A  \n";
        try {
            Path file = dir.resolve("nv.txt");
            Files.writeString(file, cu);
            LuuTruFileText store = new LuuTruFileText(dir.toString());
            DanhSachNhanVien ds = store.docNhanVien("nv.txt");
            String diaChi = ds.getDiaChiCu("NV1");
            check(diaChi.equals("  Hà Nội\tNgõ 1\\A  "));
            check(ds.sua("nv1", "Tên mới", "Tối", "0912345678"));
            ds.them(new NhanVien("NV2", "Nhân viên mới", "0923456789", "Chiều"));
            store.ghiNhanVien("nv.txt", ds);
            check(Files.readString(dir.resolve("nv.txt.bak")).equals(cu));
            DanhSachNhanVien moLai = store.docNhanVien("nv.txt");
            check(moLai.timTheoMa("NV1").getHoTen().equals("Tên mới"));
            check(diaChi.equals(moLai.getDiaChiCu("NV1")) && moLai.getDiaChiCu("NV2") == null);
            store.ghiNhanVien("copy.txt", moLai);
            check(store.docNhanVien("copy.txt").getDiaChiCu("NV1").equals(diaChi));
            String truoc = Files.readString(file);
            DanhSachNhanVien thieu = new DanhSachNhanVien(new NhanVien[]{moLai.timTheoMa("NV1")});
            loi(() -> store.ghiNhanVien("nv.txt", thieu));
            check(Files.readString(file).equals(truoc));
            moLai.xoa("NV1");
            store.ghiNhanVien("nv.txt", moLai);
            check(store.docNhanVien("nv.txt").getDiaChiCu("NV1") == null);
            for (String hong : new String[]{"", "wrong\n", cu.replace("0901234567", "bad"),
                    cu.replace("Ngõ 1", "\\q"), cu + "nv1\tTrùng\tSang\t0901234567\tHue\n"}) {
                Files.writeString(file, hong);
                loi(() -> store.docNhanVien("nv.txt"));
                loi(() -> store.ghiNhanVien("nv.txt", ds));
                check(Files.readString(file).equals(hong));
            }
            store.ghiNhanVien("empty.txt", new DanhSachNhanVien());
            check(store.docNhanVien("empty.txt").getSoLuong() == 0);
            Files.writeString(dir.resolve("four.txt"), "maNhanVien\thoTen\tsoDienThoai\tcaLamViec\n"
                    + "NV1\tKhac\t0901234567\tSang\n");
            check(store.docNhanVien("four.txt").getDiaChiCu("NV1") == null);
            System.out.println("NhanVienFileTest OK (CRUD persistence, old addresses/escaping, backups, corrupt files, empty data)");
        } finally {
            ConsoleTestSupport.deleteTree(dir);
        }
    }
    private interface Action { void run() throws Exception; }
    private static void loi(Action action) throws Exception {
        try { action.run(); } catch (IOException expected) { return; }
        throw new AssertionError("Corrupt or incomplete data accepted");
    }
    private static void check(boolean value) { if (!value) throw new AssertionError(); }
}
