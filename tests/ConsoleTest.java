import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import services.*;

public final class ConsoleTest {
    public static void main(String[] args) throws Exception {
        chungTuQuaMenu();
        crudDoiTacQuaMenu();
        eofVaFileLoi();
        System.out.println("ConsoleTest OK: " + TestSupport.assertions + " assertions");
    }

    private static void chungTuQuaMenu() throws Exception {
        Path data = Files.createTempDirectory("oop-console-");
        LuuTruFileText luu = new LuuTruFileText(data.toString());
        luu.ghiDuLieu(TestSupport.cuaHang());
        String dau = String.join("\n", "7", "1", "PN1", "sai ngay", "2026-11-09", "NCC1", "NV1",
                "1", "SP1", "abc", "0", "10", "-1", "60.25", "0", "0",
                "6", "1", "HD1", "2026-11-09", "KH1", "NV1", "1", "SP1", "3", "100.10", "0",
                "5", "6", "hd1", "4", "kh1", "0", "9", "2", "2026-11-09", "2026-11-09", "0", "");
        String output = chay(data, dau, 0);
        TestSupport.check(output.contains("Du lieu khong hop le"));
        TestSupport.check(output.contains("Tong tien ban: 300.30"));
        TestSupport.check(output.contains("Tong tien nhap: 602.50"));
        TestSupport.bang(7, luu.docDuLieu().traCuuTon("SP1"));
        String sua = String.join("\n", "6", "2", "HD1", "2026-11-09", "KH1", "NV1",
                "2", "SP1", "SP1", "5", "100.10", "0", "0", "7", "3", "PN1", "0", "0", "");
        TestSupport.check(chay(data, sua, 0).contains("Ton kho khong hop le"));
        TestSupport.bang(5, luu.docDuLieu().traCuuTon("SP1"));
        TestSupport.bang(1, luu.docDuLieu().getPhieuNhap().getSoLuong());
        chay(data, "6\n3\nHD1\n0\n7\n3\nPN1\n0\n0\n", 0);
        TestSupport.bang(0, luu.docDuLieu().traCuuTon("SP1"));
        TestSupport.bang(0, luu.docDuLieu().getHoaDon().getSoLuong());
        TestSupport.bang(0, luu.docDuLieu().getPhieuNhap().getSoLuong());
        // Product entry cannot enter or overwrite stock; covers all three product types.
        chay(data, String.join("\n", "1", "1", "SP3", "Áo thể thao", "Nike", "100", "L1", "2", "XL", "Đen", "Cotton",
                "1", "SP4", "Vợt", "Yonex", "200", "L1", "3", "Cầu lông", "Carbon", "0.085",
                "1", "SP5", "Giày", "Nike", "300", "L1", "1", "42", "Đen", "Vải", "Cao su", "0",
                "11", "2", "100", "200", "0", ""), 0);
        TestSupport.bang(0, luu.docDuLieu().traCuuTon("SP3"));
        TestSupport.bang(5, luu.docDuLieu().getSanPham().getSoLuong());
    }

    private static void crudDoiTacQuaMenu() throws Exception {
        Path data = Files.createTempDirectory("oop-metadata-");
        LuuTruFileText luu = new LuuTruFileText(data.toString());
        luu.ghiDuLieu(TestSupport.cuaHang());
        String input = String.join("\n", "2", "1", "L2", "Loại mới", "Mô tả",
                "2", "L2", "Loại sửa", "Mô tả sửa", "4", "loai", "3", "L2", "0",
                "3", "1", "KH2", "Khách mới", "0901234567", "HCM",
                "2", "KH2", "Khách sửa", "0901234568", "HN", "5", "3", "KH2", "0",
                "4", "1", "NV2", "Nhân viên mới", "0901234567", "Sáng",
                "2", "NV2", "Nhân viên sửa", "0901234568", "Chiều", "3", "NV2", "0",
                "5", "1", "NCC2", "Nhà cung cấp mới", "0901234567", "HCM",
                "2", "NCC2", "Nhà cung cấp sửa", "0901234568", "HN", "3", "NCC2", "0", "0", "");
        TestSupport.check(chay(data, input, 0).contains("Da cap nhat va luu"));
        QuanLyCuaHang ql = luu.docDuLieu();
        TestSupport.bang(1, ql.getLoaiSP().getSoLuong());
        TestSupport.bang(1, ql.getKhachHang().getSoLuong());
        TestSupport.bang(1, ql.getNhanVien().getSoLuong());
        TestSupport.bang(1, ql.getNhaCungCap().getSoLuong());
    }

    private static void eofVaFileLoi() throws Exception {
        Path data = Files.createTempDirectory("oop-eof-");
        LuuTruFileText luu = new LuuTruFileText(data.toString());
        luu.ghiDuLieu(TestSupport.cuaHang());
        chay(data, "1\n2\nSP1\nTen moi\n", 0);
        TestSupport.bang("Giày chạy bộ", luu.docDuLieu().getSanPham().timTheoMa("SP1").getTenSanPham());
        chay(data, "7\n1\nPN1\n2026-11-09\nNCC1\nNV1\n1\nSP1\n10\n", 0);
        TestSupport.bang(0, luu.docDuLieu().getPhieuNhap().getSoLuong());
        TestSupport.bang(0, luu.docDuLieu().traCuuTon("SP1"));
        Path snapshot = data.resolve(LuuTruFileText.TEN_FILE);
        String goc = Files.readString(snapshot);
        // Force a write failure after a successful in-memory change.
        Path bak = data.resolve(LuuTruFileText.TEN_FILE + ".bak");
        Files.deleteIfExists(bak);
        Files.createDirectory(bak);
        Files.writeString(bak.resolve("block"), "block");
        String output = chay(data, "2\n1\nL2\nMoi\n\n", 1);
        TestSupport.check(output.contains("CHUA LUU DUOC"));
        TestSupport.bang(goc, Files.readString(snapshot));
        Files.writeString(snapshot, "FILE_HONG\n");
        TestSupport.check(chay(data, "0\n", 1).contains("Sai dinh dang"));
        TestSupport.bang("FILE_HONG\n", Files.readString(snapshot));
    }

    private static String chay(Path data, String input, int exitCode) throws Exception {
        String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Path output = Files.createTempFile("oop-console-output-", ".txt");
        Process p = new ProcessBuilder(javaBin, "-Dfile.encoding=UTF-8", "-cp",
                System.getProperty("java.class.path"), "Main", data.toString())
                .redirectErrorStream(true).redirectOutput(output.toFile()).start();
        try (var stdin = p.getOutputStream()) {
            stdin.write(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        if (!p.waitFor(15, TimeUnit.SECONDS)) {
            p.destroyForcibly();
            throw new AssertionError("Console process timed out: " + Files.readString(output));
        }
        String text = Files.readString(output);
        if (p.exitValue() != exitCode) {
            throw new AssertionError("Expected exit " + exitCode + ", got " + p.exitValue() + ": " + text);
        }
        Files.delete(output);
        return text;
    }
}
