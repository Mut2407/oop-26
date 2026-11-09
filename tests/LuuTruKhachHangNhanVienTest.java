import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import models.KhachHang;
import models.NhanVien;
import services.DanhSachKhachHang;
import services.DanhSachNhanVien;
import services.LuuTruFileText;

public class LuuTruKhachHangNhanVienTest {
    private static final String CU = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi\n"
            + "Nv1\tNguyễn An\tSáng\t0901234567\t  Hà Nội\\tNgõ 1\\\\A  \n"
            + "NV2\tTrần Bình\tTối\t0912345678\tHuế\n";

    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("task064-files-");
        try {
            LuuTruFileText store = new LuuTruFileText(dir.toString());
            Path kh = dir.resolve("kh.txt");
            String cuKh = "\uFEFFKHACH_HANG_V1\nK1\tNguyễn Ánh\t0901234567\tHà Nội\\tNgõ 2\n";
            Files.writeString(kh, cuKh);
            DanhSachKhachHang khachHang = store.docKhachHang("kh.txt");
            khachHang.them(new KhachHang("K2", "Trần An", "0912345678", "Huế"));
            store.ghiKhachHang("kh.txt", khachHang);
            check(Files.readString(dir.resolve("kh.txt.bak")).equals(cuKh));
            check(store.docKhachHang("kh.txt").getSoLuong() == 2);
            check(store.docKhachHang("kh.txt").timTheoMa("K1").getDiaChi().equals("Hà Nội\tNgõ 2"));
            store.ghiKhachHang("kh.txt", khachHang);
            Files.writeString(kh, "KHACH_HANG_V1\nKbad\tAn\tbad\tHuế\n");
            String badKh = Files.readString(kh);
            loi(IOException.class, () -> store.ghiKhachHang("kh.txt", khachHang));
            check(Files.readString(kh).equals(badKh));

            Path nv = dir.resolve("nv.txt");
            Files.writeString(nv, CU);
            List<NhanVien> list = store.docNhanVien("nv.txt");
            check(list.get(0).getSoDienThoai().equals("0901234567"));
            check(list.get(0).getCaLamViec().equals("Sáng"));
            check(list.contains(list.get(0)));
            list.get(0).capNhatLienHe("0999999999");
            store.ghiNhanVien("nv.txt", list);
            check(Files.readString(dir.resolve("nv.txt.bak")).equals(CU));
            check(Files.readAllLines(nv).get(0).equals("maNhanVien\thoTen\tsoDienThoai\tcaLamViec\tdiaChiCu"));
            DanhSachNhanVien loaded = DanhSachNhanVien.tuDanhSach(store.docNhanVien("nv.txt"));
            check(loaded.timTheoMa("NV1").getSoDienThoai().equals("0999999999"));
            check(loaded.getDiaChiCu("NV1").equals("  Hà Nội\tNgõ 1\\A  "));
            loaded.them(new NhanVien("NV3", "Mới", "0923456789", "Chiều"));
            loaded.sua("nv1", "An mới", "Tối", "0934567890");
            store.ghiNhanVien("nv.txt", loaded.getTatCa());
            check(Files.readString(dir.resolve("nv.txt.bak")).equals(CU));
            check(DanhSachNhanVien.tuDanhSach(store.docNhanVien("nv.txt")).getDiaChiCu("NV3") == null);

            // A fresh storage instance can export metadata to a different path too.
            LuuTruFileText fresh = new LuuTruFileText(dir.toString());
            fresh.ghiNhanVien("copy.txt", store.docNhanVien("nv.txt"));
            check(DanhSachNhanVien.tuDanhSach(fresh.docNhanVien("copy.txt")).getDiaChiCu("NV1")
                    .equals(loaded.getDiaChiCu("NV1")));
            fresh.ghiNhanVien("subset.txt", list.subList(0, 1));
            check(DanhSachNhanVien.tuDanhSach(fresh.docNhanVien("subset.txt")).getDiaChiCu("NV1") != null);
            List<NhanVien> plain = new ArrayList<>(store.docNhanVien("nv.txt"));
            plain.get(0).capNhatLienHe("0945678901");
            store.ghiNhanVien("nv.txt", plain);
            DanhSachNhanVien recovered = DanhSachNhanVien.tuDanhSach(store.docNhanVien("nv.txt"));
            check(recovered.getDiaChiCu("NV1").equals(loaded.getDiaChiCu("NV1")));
            check(recovered.timTheoMa("NV1").getSoDienThoai().equals("0945678901"));
            String saved = Files.readString(nv);
            loi(IllegalArgumentException.class, () -> store.ghiNhanVien("nv.txt", null));
            loi(IllegalArgumentException.class, () -> store.ghiNhanVien("nv.txt", List.of(plain.get(0), plain.get(0))));
            check(Files.readString(nv).equals(saved));

            // Explicit four-column files have no legacy address; never fill from other sources.
            Files.writeString(dir.resolve("four.txt"), "\uFEFFmaNhanVien\thoTen\tsoDienThoai\tcaLamViec\n"
                    + "Nv1\tKhác\t0987654321\tChiều\n");
            List<NhanVien> four = store.docNhanVien("four.txt");
            check(DanhSachNhanVien.tuDanhSach(four).getDiaChiCu("Nv1") == null);
            store.ghiNhanVien("four.txt", four);
            check(DanhSachNhanVien.tuDanhSach(store.docNhanVien("four.txt")).getDiaChiCu("Nv1") == null);
            check(DanhSachNhanVien.tuDanhSach(store.docNhanVien("nv.txt")).getDiaChiCu("Nv1") != null);

            Files.writeString(dir.resolve("conflict.txt"), CU);
            Files.writeString(dir.resolve("conflict.txt.bak"), "different backup");
            List<NhanVien> conflict = store.docNhanVien("conflict.txt");
            loi(IOException.class, () -> store.ghiNhanVien("conflict.txt", conflict));
            check(Files.readString(dir.resolve("conflict.txt")).equals(CU));
            check(Files.readString(dir.resolve("conflict.txt.bak")).equals("different backup"));
            for (String broken : List.of("", "wrong\n", CU + "nv1\tTrùng\tTối\t0901234567\tHuế\n",
                    CU.replace("0901234567", "bad"), CU.replace("Ngõ 1", "\\q"))) {
                Files.writeString(nv, broken);
                loi(IOException.class, () -> store.docNhanVien("nv.txt"));
                loi(IOException.class, () -> store.ghiNhanVien("nv.txt", list));
                check(Files.readString(nv).equals(broken));
            }
            store.ghiNhanVien("empty.txt", List.of());
            check(store.docNhanVien("empty.txt").isEmpty());
            store.ghiKhachHang("empty-kh.txt", new DanhSachKhachHang());
            check(store.docKhachHang("empty-kh.txt").getTatCa().isEmpty());
            System.out.println("LuuTruKhachHangNhanVienTest OK (8 groups: customer migration, employee migration, edits, export, metadata recovery, formats, backup/errors, empty)");
        } finally {
            try (var paths = Files.walk(dir)) {
                for (Path p : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(p);
                }
            }
        }
    }

    private interface Action {
        void run() throws Exception;
    }

    private static void loi(Class<? extends Exception> type, Action action) throws Exception {
        try {
            action.run();
        } catch (Exception e) {
            if (type.isInstance(e)) {
                return;
            }
            throw e;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}
