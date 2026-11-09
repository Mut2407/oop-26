import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import services.DanhSachNhanVien;
import services.LuuTruFileText;

public class LuuTruRegressionTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("task065-regression-");
        try {
            Path data = Files.createDirectory(dir.resolve("data"));
            Map<String, String> original = new LinkedHashMap<>();
            try (var paths = Files.list(Path.of("data"))) {
                for (Path source : paths.filter(Files::isRegularFile).toList()) {
                    Files.copy(source, data.resolve(source.getFileName()));
                    original.put(source.getFileName().toString(), Files.readString(source));
                }
            }
            String output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "garbage", "1", "1", "7", "SP01", "8", "vot", "9", "DC",
                    "10", "oops", "-1", "0", "2000000", "11", "12", "-1", "2", "0",
                    "2", "1", "4", "DC", "0", "3", "1", "11", "KH01", "0",
                    "4", "1", "11", "NV01", "0", "5", "1", "5", "an phat", "6", "NCC01", "0", "0"), 0);
            for (String expected : new String[]{"QUAN LY SAN PHAM", "QUAN LY DANH MUC", "QUAN LY KHACH HANG",
                    "QUAN LY NHAN VIEN", "QUAN LY NHA CUNG CAP", "SP01", "KH01", "NV01", "NCC01"}) {
                check(output.contains(expected), "Menu/record missing: " + expected);
            }
            check(!output.contains("Exception"), output);
            for (var entry : original.entrySet()) {
                check(Files.readString(data.resolve(entry.getKey())).equals(entry.getValue()), "Read-only menu wrote " + entry.getKey());
            }

            Path kh = data.resolve("khachhang.txt");
            Path nv = data.resolve("nhanvien.txt");
            String badKh = "KHACH_HANG_V1\nBAD\tSai\t123\tHuế\n";
            try (var session = new ConsoleTestSupport.Session(dir)) {
                session.send("3\n");
                session.awaitOutput("===== QUAN LY KHACH HANG =====");
                Files.writeString(kh, badKh);
                session.send("9\n1\n10\n0\n0\n");
                output = session.finish(0);
            }
            check(output.contains("Khong doc duoc file") && output.contains("Khong luu duoc file"), output);
            check(output.contains("Ma khach hang: KH01"), "Failed reload replaced customer memory");
            check(Files.readString(kh).equals(badKh), "Failed customer read/save changed corrupt file");
            Files.writeString(kh, original.get("khachhang.txt"));
            String badNv = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi\nBAD\tSai\tSáng\t123\tHuế\n";
            try (var session = new ConsoleTestSupport.Session(dir)) {
                session.send("4\n");
                session.awaitOutput("===== QUAN LY NHAN VIEN =====");
                Files.writeString(nv, badNv);
                session.send("9\n1\n10\n0\n0\n");
                output = session.finish(0);
            }
            check(output.contains("CHUA LUU DUOC") && output.contains("Dong 2"), output);
            check(output.contains("Ma Nhan Vien: NV01"), "Failed reload replaced employee memory");
            check(output.contains("Dia chi (du lieu chuyen tiep): Quan 1, TP.HCM"), "Failed reload lost legacy metadata");
            check(Files.readString(nv).equals(badNv), "Failed employee read/save changed corrupt file");

            output = ConsoleTestSupport.run(dir, "0\n", 1);
            check(output.contains("Khong the khoi dong"), output);
            check(Files.readString(nv).equals(badNv), "Startup overwrote bad employee data");
            Files.writeString(nv, original.get("nhanvien.txt"));
            Files.writeString(kh, badKh);
            output = ConsoleTestSupport.run(dir, "0\n", 1);
            check(output.contains("Khong the khoi dong"), output);
            check(Files.readString(kh).equals(badKh), "Startup overwrote bad customer data");
            Files.writeString(kh, original.get("khachhang.txt"));
            check(!Files.exists(data.resolve("nhanvien.txt.bak")) && !Files.exists(data.resolve("khachhang.txt.bak")),
                    "Read failure created migration backups");

            // Representative CRUD in existing modules, all against copied data.
            output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "2", "2", "QA", "Danh mục QA", "", "3", "QA", "Danh mục QA mới", "Mô tả", "4", "QA", "0",
                    "1", "3", "SPQA", "Bóng QA", "125000", "Hãng QA", "3", "DC", "Xanh", "Cao su", "2", "0.5",
                    "5", "SPQA", "Bóng QA mới", "150000", "Hãng QA", "4", "DC", "Đỏ", "Cao su", "2", "0.6",
                    "7", "SPQA", "6", "SPQA", "0",
                    "5", "2", "NCCQA", "Nhà cung cấp QA", "0901234567", "Huế",
                    "3", "NCCQA", "Nhà cung cấp QA mới", "0912345678", "Hà Nội",
                    "6", "NCCQA", "4", "NCCQA", "0", "0"), 0);
            for (String expected : new String[]{"Da them danh muc.", "Da sua danh muc.", "Danh muc QA moi",
                    "Da them san pham.", "Da sua san pham.", "Da xoa san pham.", "Da them nha cung cap.",
                    "Da sua nha cung cap.", "Da xoa."}) {
                check(output.contains(expected), expected + "\n" + output);
            }
            check(!output.contains("Khong the thuc hien"), output);
            check(!Files.readString(data.resolve("sanpham.txt")).contains("SPQA"), "Product delete not persisted");
            check(!Files.readString(data.resolve("nhacungcap.txt")).contains("NCCQA"), "Supplier delete not persisted");
            LuuTruFileText store = new LuuTruFileText(data.toString());
            check(store.docKhachHang("khachhang.txt").timTheoMa("kh01").getMaKhachHang().equals("KH01"), "Stable customer ID");
            check(DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"))
                    .timTheoMa("nv01").getMaNhanVien().equals("NV01"), "Stable employee ID");

            Path empty = Files.createDirectory(dir.resolve("empty"));
            Path emptyData = Files.createDirectory(empty.resolve("data"));
            Files.writeString(emptyData.resolve("khachhang.txt"), "maKhachHang\thoTen\tsoDienThoai\tdiaChi\n");
            Files.writeString(emptyData.resolve("nhanvien.txt"), "maNhanVien\thoTen\tsoDienThoai\tcaLamViec\tdiaChiCu\n");
            output = ConsoleTestSupport.run(empty, ConsoleTestSupport.lines(
                    "3", "1", "8", "11", "NONE", "0", "4", "1", "8", "11", "NONE", "0", "0"), 0);
            check(output.contains("Danh sach khach hang dang trong") && output.contains("Danh sach nhan vien dang trong"), output);
            check(!output.contains("Exception"), output);
            System.out.println("LuuTruRegressionTest OK (6 groups: copied data/menus, live reload failures, startup failures, existing-module CRUD, stable IDs, empty data)");
        } finally {
            ConsoleTestSupport.deleteTree(dir);
        }
    }

    private static void check(boolean value, String message) {
        ConsoleTestSupport.check(value, message);
    }
}
