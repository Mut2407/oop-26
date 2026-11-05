import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import services.DanhSachNhanVien;

public class Task061ConsoleTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("task061-console-");
        try {
            Files.createDirectory(dir.resolve("data"));
            Path file = dir.resolve("data/nhanvien.txt");
            Files.writeString(file, "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi\n"
                    + "NVOLD\tNguyễn Văn An\tSáng\t0901000001\tHà Nội\\tNgõ 1\n");

            // Model edits retain the legacy address without prompting for it.
            String first = chay(dir, "first", "sai\n4\n1\n3\nNVOLD\nNguyễn Văn An mới\nTối\n0912345678\n1\n0\n0\n", 0);
            check(first.contains("Vui long nhap mot so!"));
            check(first.contains("Da cap nhat nhan vien."));
            check(first.contains("Da luu file nhan vien."));
            check(first.contains("Nguyen Van An moi"));
            check(first.contains("Dia chi (du lieu chuyen tiep): Ha Noi\tNgo 1"));

            DanhSachNhanVien ds = new DanhSachNhanVien();
            ds.docFile(file.toString());
            check(ds.timTheoMa("NVOLD").getHoTen().equals("Nguyễn Văn An mới"));
            check(ds.timTheoMa("NVOLD").getCaLamViec().equals("Tối"));
            check(ds.timTheoMa("NVOLD").getSoDienThoai().equals("0912345678"));
            check(ds.getDiaChiCu("NVOLD").equals("Hà Nội\tNgõ 1"));

            String second = chay(dir, "second", "4\n1\n2\nNVNEW\nNhân viên mới\nSáng\n0923456789\n0\n3\n1\n0\n1\n0\n2\n0\n5\n0\n0\n", 0);
            check(!second.contains("Dia chi chuyen tiep (Enter"));
            check(second.contains("Nguyen Van An moi") && second.contains("0912345678"));
            check(second.contains("Dia chi (du lieu chuyen tiep): Ha Noi\tNgo 1"));
            check(second.contains("Da them 1 nhan vien."));
            check(second.contains("QUAN LY KHACH HANG"));
            check(second.contains("QUAN LY SAN PHAM"));
            check(second.contains("QUAN LY DANH MUC"));
            check(second.contains("QUAN LY NHA CUNG CAP"));
            check(second.contains("Thoat chuong trinh!"));
            ds.docFile(file.toString());
            check(ds.getSoLuong() == 2 && ds.getDiaChiCu("NVNEW") == null);
            check(ds.getDiaChiCu("NVOLD").equals("Hà Nội\tNgõ 1"));

            String third = chay(dir, "third", "4\n1\n0\n0\n", 0);
            check(third.contains("Nhan vien moi") && third.contains("0923456789"));
            String bad = "maNhanVien\thoTen\tcaLamViec\tsoDienThoai\tdiaChi\nNVOLD\tAn\tSang\tbad\tHà Nội\n";
            Files.writeString(file, bad);
            String failed = chay(dir, "bad-file", "0\n", 1);
            check(failed.contains("Khong the khoi dong"));
            check(Files.readString(file).equals(bad));
            System.out.println("Task061ConsoleTest OK (3 cases: edit, reopen/add, reject corrupt file)");
        } finally {
            try (var paths = Files.walk(dir)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(path);
                }
            }
        }
    }

    private static String chay(Path dir, String name, String input, int expectedExit) throws Exception {
        String classpath = Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
                .map(path -> Path.of(path).toAbsolutePath().toString())
                .collect(Collectors.joining(File.pathSeparator));
        Path output = dir.resolve(name + ".log");
        Process process = new ProcessBuilder(
                Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                "-Dfile.encoding=UTF-8", "-cp", classpath, "Main")
                .directory(dir.toFile()).redirectErrorStream(true).redirectOutput(output.toFile()).start();
        try {
            try (var stdin = process.getOutputStream()) {
                stdin.write(input.getBytes(StandardCharsets.UTF_8));
            }
            if (!process.waitFor(20, TimeUnit.SECONDS)) {
                throw new AssertionError("Console timed out: " + name);
            }
            String text = Files.readString(output);
            if (process.exitValue() != expectedExit) {
                throw new AssertionError("Console exit " + process.exitValue() + ":\n" + text);
            }
            return text;
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly().waitFor();
            }
        }
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
}
