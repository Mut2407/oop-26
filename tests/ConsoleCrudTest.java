import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import services.LuuTruFileText;

public class ConsoleCrudTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("basic-console-");
        try {
            Path data = Files.createDirectory(dir.resolve("data"));
            Map<String, byte[]> original = new LinkedHashMap<>();
            try (var paths = Files.list(Path.of("data"))) {
                for (Path file : paths.filter(Files::isRegularFile).toList()) {
                    Files.copy(file, data.resolve(file.getFileName()));
                    original.put(file.getFileName().toString(), Files.readAllBytes(file));
                }
            }
            LuuTruFileText store = new LuuTruFileText(data.toString());
            Map<String, String> diaChiCu = store.docNhanVien("nhanvien.txt").getDiaChiCuTheoMa();
            String output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "sai", "1", "4", "", "5", "0", "2", "4", "", "0", "3", "4", "", "0",
                    "4", "4", "", "5", "0", "5", "4", "", "0", "0"), 0);
            for (String menu : new String[]{"SAN PHAM", "DANH MUC", "KHACH HANG", "NHAN VIEN", "NHA CUNG CAP"}) {
                check(output.contains("QUAN LY " + menu), "Missing menu " + menu);
            }
            check(output.contains("Tong so luong ton:") && output.contains("Lua chon khong hop le!"), "Inventory/basic menus");
            for (var entry : original.entrySet()) {
                check(java.util.Arrays.equals(entry.getValue(), Files.readAllBytes(data.resolve(entry.getKey()))),
                        "Search or stock statistics wrote " + entry.getKey());
            }
            output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "2", "1", "DMTEST", "Danh mục thử", "", "2", "DMTEST", "Danh mục mới", "Mô tả", "0",
                    "1", "1", "SPTEST", "Bóng thử", "100", "Hãng", "2", "DMTEST", "Đỏ", "Da", "2", "0.5",
                    "2", "SPTEST", "Bóng mới", "150", "Hãng", "5", "DMTEST", "Xanh", "Da", "2", "0.6",
                    "4", "SPTEST", "5", "0", "2", "3", "DMTEST", "0",
                    "1", "3", "SPTEST", "0", "2", "3", "DMTEST", "0",
                    "3", "1", "KTEST", "Khách thử", "0901234567", "Huế",
                    "2", "KTEST", "Khách mới", "0912345678", "Hà Nội", "4", "khach moi", "0",
                    "4", "1", "NVTEST", "Nhân viên thử", "Sáng", "0901234567",
                    "2", "NVTEST", "Nhân viên mới", "Chiều", "0912345678", "4", "nhan vien moi", "0",
                    "5", "1", "NCCTEST", "Nhà cung cấp thử", "0901234567", "Huế",
                    "2", "NCCTEST", "Nhà cung cấp mới", "0912345678", "Hà Nội", "4", "NCCTEST", "0", "0"), 0);
            for (String expected : new String[]{"Da them san pham.", "Da sua san pham.", "Da xoa san pham.",
                    "Khong the xoa danh muc dang co san pham.", "Da xoa danh muc.", "Da them khach hang.",
                    "Cap nhat khach hang thanh cong!", "Da them nhan vien.", "Da cap nhat nhan vien.",
                    "Da them nha cung cap.", "Da sua nha cung cap."}) {
                check(output.contains(expected), "Missing result: " + expected);
            }
            check(!output.contains("CHUA LUU DUOC"), output);
            check(store.docKhachHang("khachhang.txt").CheckMaTrung("KTEST").getHoTen().equals("Khách mới"), "Customer save");
            check(store.docNhanVien("nhanvien.txt").CheckMaTrung("NVTEST").getCaLamViec().equals("Chiều"), "Employee save");
            check(store.docNhanVien("nhanvien.txt").getDiaChiCuTheoMa().equals(diaChiCu), "Old addresses changed");
            output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "3", "4", "KTEST", "3", "KTEST", "0", "4", "4", "NVTEST", "3", "NVTEST", "0",
                    "5", "4", "NCCTEST", "3", "NCCTEST", "0", "0"), 0);
            check(output.contains("Khach moi") && output.contains("Nhan vien moi"), "Reopen did not restore edits");
            check(store.docKhachHang("khachhang.txt").CheckMaTrung("KTEST") == null, "Customer delete");
            check(store.docNhanVien("nhanvien.txt").CheckMaTrung("NVTEST") == null, "Employee delete");
            check(store.docNhaCungCap("nhacungcap.txt").CheckMaTrung("NCCTEST") == null, "Supplier delete");
            byte[] before = Files.readAllBytes(data.resolve("sanpham.txt"));
            ConsoleTestSupport.run(dir, "1\n1\nPART\nNhap do\n", 0);
            check(java.util.Arrays.equals(before, Files.readAllBytes(data.resolve("sanpham.txt"))), "EOF saved a partial product");
            Path empty = Files.createDirectory(dir.resolve("missing"));
            output = ConsoleTestSupport.run(empty, "0\n", 1);
            check(output.contains("Khong the khoi dong") && !Files.exists(empty.resolve("data")), "Sample data was generated");
            String hong = "KHACH_HANG_V1\nBAD\tSai\t123\tHue\n";
            try (var session = new ConsoleTestSupport.Session(dir)) {
                session.send("3\n");
                session.awaitOutput("QUAN LY KHACH HANG");
                Files.writeString(data.resolve("khachhang.txt"), hong);
                session.send("1\nKFAIL\nKhach\n0901234567\nHue\n0\n0\n");
                output = session.finish(0);
            }
            check(output.contains("CHUA LUU DUOC") && Files.readString(data.resolve("khachhang.txt")).equals(hong), "Corrupt file overwritten");
            output = ConsoleTestSupport.run(dir, "0\n", 1);
            check(output.contains("Khong the khoi dong"), "Corrupt data accepted at startup");
            System.out.println("ConsoleCrudTest OK (5 menus, inventory, CRUD/restart, category references, no sample generation, EOF, file errors)");
        } finally {
            ConsoleTestSupport.deleteTree(dir);
        }
    }
    private static void check(boolean value, String message) { ConsoleTestSupport.check(value, message); }
}
