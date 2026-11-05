import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import services.DanhSachKhachHang;
import services.DanhSachNhanVien;
import services.LuuTruFileText;

public class Task064ConsoleTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("task064-console-");
        try {
            Path data = Files.createDirectory(dir.resolve("data"));
            Path kh = data.resolve("khachhang.txt");
            Path nv = data.resolve("nhanvien.txt");
            String khCu = "KHACH_HANG_V1\nKLEG\tKhách cũ\t0987654321\tHuế\n";
            String nvCu = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi\n"
                    + "NVLEG\tTrần An\tSáng\t0987654321\tHà Nội\\tNgõ 1\n"
                    + "NVSECOND\tBình cũ\tTối\t0976543210\tHuế\n";
            Files.writeString(kh, khCu);
            Files.writeString(nv, nvCu);
            String output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "3", "1", "2", "K1", "Đặng Ánh", "0901234567", "Hà Nội",
                    "6", "2", "K2", "Nguyễn Bình", "0912345678", "Huế",
                    "K3", "Trần Chi", "0923456789", "Đà Nẵng",
                    "3", "K1", "Đặng Ánh mới", "0934567890", "Hồ Chí Minh",
                    "11", "k1", "12", "dang anh", "13", "0934567890",
                    "7", "2", "", "bình", "091", "hue", "8", "4", "K3", "10", "9", "1", "0",
                    "4", "1", "2", "NV1", "Nguyễn Long", "Sáng", "0901234567",
                    "6", "2", "NV2", "Nguyễn Minh", "Chiều", "0912345678",
                    "NV3", "Trần Chi", "Tối", "0923456789",
                    "3", "NVLEG", "Trần An mới", "Chiều", "0956789012",
                    "11", "nvleg", "12", "nguyen long",
                    "7", "2", "", "long", "sang", "090", "", "8",
                    "4", "NV3", "10", "9", "1", "0", "0"), 0);
            check(output.contains("Cap nhat khach hang thanh cong!") && output.contains("Da cap nhat nhan vien."), output);
            check(output.contains("Dang Anh moi") && output.contains("Nguyen Long"), output);
            check(!output.contains("Dia chi chuyen tiep (Enter") && !output.contains("CHUA LUU DUOC"), output);
            check(!output.contains("Lua chon khong hop le!") && !output.contains("Exception"), output);
            LuuTruFileText store = new LuuTruFileText(data.toString());
            DanhSachKhachHang khDs = store.docKhachHang("khachhang.txt");
            DanhSachNhanVien nvDs = DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"));
            check(khDs.getSoLuong() == 3 && khDs.timTheoMa("K3") == null, "Customer CRUD count");
            check(khDs.timTheoMa("K1").getDiaChi().equals("Hồ Chí Minh"), "Customer edit");
            check(nvDs.getSoLuong() == 4 && nvDs.timTheoMa("NV3") == null, "Employee CRUD count");
            check(nvDs.getDiaChiCu("NVLEG").equals("Hà Nội\tNgõ 1"), "Legacy address lost");
            check(nvDs.getDiaChiCu("NVSECOND").equals("Huế") && nvDs.getDiaChiCu("NV1") == null, "Metadata mixed");
            check(nvDs.timTheoMa("NVLEG").getCaLamViec().equals("Chiều"), "Employee shift");
            check(Files.readString(data.resolve("khachhang.txt.bak")).equals(khCu), "Customer backup");
            check(Files.readString(data.resolve("nhanvien.txt.bak")).equals(nvCu), "Employee backup");

            // Reads, searches and failed edits/deletes must not autosave even identical content.
            FileTime stamp = FileTime.fromMillis(946684800000L);
            Files.setLastModifiedTime(kh, stamp);
            Files.setLastModifiedTime(nv, stamp);
            String khBefore = Files.readString(kh);
            String nvBefore = Files.readString(nv);
            output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "3", "1", "11", "K1", "12", "dang", "13", "0934567890",
                    "3", "MISSING", "4", "MISSING", "oops", "999", "0",
                    "4", "1", "11", "nvleg", "12", "long", "3", "MISSING", "4", "MISSING",
                    "oops", "999", "0", "0"), 0);
            check(output.contains("Dang Anh moi") && output.contains("Tran An moi"), "Reopen did not restore names");
            check(output.contains("Khong tim thay khach hang!") && output.contains("Khong tim thay nhan vien!"), output);
            unchanged(kh, khBefore, stamp);
            unchanged(nv, nvBefore, stamp);

            // EOF halfway through either batch must leave both memory/file intact.
            ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "3", "6", "2", "KPART", "Nhập dở", "0901234567", "Huế"), 0);
            unchanged(kh, khBefore, stamp);
            ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "4", "6", "2", "NVPART", "Nhập dở", "Sáng", "0901234567"), 0);
            unchanged(nv, nvBefore, stamp);

            output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "3", "5", "1", "KNEW", "Khách mới", "0901234567", "Huế", "0",
                    "4", "5", "2", "NVLEG", "Nhân viên còn lại", "Tối", "0967890123",
                    "NVX", "Nhân viên mới", "Sáng", "0978901234", "0", "0"), 0);
            check(output.contains("se thay the danh sach hien tai"), "Replacement warning absent");
            check(store.docKhachHang("khachhang.txt").getSoLuong() == 1, "Customer replacement");
            nvDs = DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"));
            check(nvDs.getSoLuong() == 2 && nvDs.getDiaChiCuTheoMa().size() == 1, "Employee replacement");
            check(nvDs.getDiaChiCu("NVLEG").equals("Hà Nội\tNgõ 1"), "Replacement lost legacy metadata");

            output = ConsoleTestSupport.run(dir, ConsoleTestSupport.lines(
                    "3", "2", "KNEW", "bad code", "KVALID", "", "Tên hợp lệ", "bad", "0989012345", "", "Huế", "0",
                    "4", "2", "NVX", "NVGOOD", "", "Tên hợp lệ", "", "Chiều", "bad", "0990123456", "0", "0"), 0);
            check(output.contains("Ma khach hang da ton tai") && output.contains("Ma nhan vien da ton tai"), output);
            check(output.contains("9-15 chu so") && output.contains("Thoat chuong trinh!"), output);
            check(store.docKhachHang("khachhang.txt").timTheoMa("KVALID") != null, "Corrected customer input");
            check(DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt")).timTheoMa("NVGOOD") != null, "Corrected employee input");
            System.out.println("Task064ConsoleTest OK (7 groups: CRUD/search, persistence/backup, restart, no unintended saves, cancelled batches, replace, invalid input)");
        } finally {
            ConsoleTestSupport.deleteTree(dir);
        }
    }

    private static void unchanged(Path file, String text, FileTime stamp) throws Exception {
        check(Files.readString(file).equals(text), "File content changed: " + file);
        check(Files.getLastModifiedTime(file).equals(stamp), "Unexpected save: " + file);
    }

    private static void check(boolean value, String message) {
        ConsoleTestSupport.check(value, message);
    }
}
