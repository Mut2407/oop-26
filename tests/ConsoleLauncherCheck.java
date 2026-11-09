import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Optional integration check: pass the PowerShell executable as the first argument. */
public class ConsoleLauncherCheck {
    public static void main(String[] args) throws Exception {
        String powershell = args.length == 0 ? "powershell.exe" : args[0];
        Path temp = Files.createTempDirectory("console-launcher-");
        try {
            Path project = Files.createDirectory(temp.resolve("Cửa hàng thể thao"));
            copyTree(Path.of("src"), project.resolve("src"));
            copyTree(Path.of("data"), project.resolve("data"));
            Files.copy(Path.of("run.ps1"), project.resolve("run.ps1"));
            // Non-public Java classes may live in a file with spaces in its name.
            Files.writeString(project.resolve("src/Kiểm tra đường dẫn.java"), "final class LauncherPathProbe {}\n");
            Map<Path, byte[]> original = new LinkedHashMap<>();
            try (var files = Files.walk(project.resolve("data"))) {
                for (Path path : files.filter(Files::isRegularFile).toList()) {
                    original.put(path, Files.readAllBytes(path));
                }
            }

            String output = run(powershell, project, "", true, "-BuildOnly");
            check(output.contains("Build OK") && !output.contains("Nhap lua chon"), output);
            Path classes = project.resolve("out/console");
            check(Files.exists(classes.resolve("Main.class")), "Main was not compiled");
            check(Files.exists(classes.resolve("LauncherPathProbe.class")), "Quoted UTF-8 source path failed");

            Path stale = classes.resolve("StaleDependency.class");
            Files.writeString(stale, "old build artifact");
            try (var files = Files.walk(project.resolve("src"))) {
                for (Path source : files.filter(Files::isRegularFile).toList()) {
                    Files.setLastModifiedTime(source, FileTime.fromMillis(1000));
                }
            }
            output = run(powershell, project, "1\n4\n\n0\n2\n4\n\n0\n3\n4\n\n0\n4\n4\n\n0\n5\n4\n\n0\n0\n", true);
            for (String menu : List.of("SAN PHAM", "DANH MUC", "KHACH HANG", "NHAN VIEN", "NHA CUNG CAP")) {
                check(output.contains("QUAN LY " + menu), "Missing menu " + menu + "\n" + output);
            }
            check(!output.contains("Exception"), output);
            check(!Files.exists(stale), "Old class survived a clean build");

            // Reproduce mixed source versions even when the edited file has an old timestamp.
            Path storage = project.resolve("src/services/LuuTruFileText.java");
            String valid = Files.readString(storage);
            Files.writeString(storage, valid.replace("docKhachHang(", "docKhachHangMissing("));
            Files.setLastModifiedTime(storage, FileTime.fromMillis(1000));
            output = run(powershell, project, "0\n", false);
            check(output.contains("docKhachHang"), "Missing compiler diagnostic\n" + output);
            check(!output.contains("Nhap lua chon"), "Launched stale Main after compilation failed\n" + output);

            Files.writeString(storage, valid);
            output = run(powershell, project, "3\n4\n\n0\n4\n4\n\n0\n0\n", true);
            check(output.contains("KH01") && output.contains("NV01"), "Records missing after recovery\n" + output);
            check(!output.contains("Unresolved compilation") && !output.contains("Exception"), output);
            for (var entry : original.entrySet()) {
                check(java.util.Arrays.equals(Files.readAllBytes(entry.getKey()), entry.getValue()),
                        "Read-only launch changed data: " + entry.getKey());
            }
            try (var files = Files.walk(project.resolve("data"))) {
                check(files.filter(Files::isRegularFile).count() == original.size(), "Read-only launch added data files");
            }
            System.out.println("ConsoleLauncherCheck OK (4 groups: build/UTF-8 paths, clean build/menus, missing API blocks launch, recovery/data preserved)");
        } finally {
            ConsoleTestSupport.deleteTree(temp);
        }
    }

    private static String run(String powershell, Path project, String input, boolean success, String... options) throws Exception {
        List<String> command = new ArrayList<>(List.of(powershell, "-NoProfile", "-ExecutionPolicy", "Bypass",
                "-File", project.resolve("run.ps1").toString()));
        command.addAll(List.of(options));
        Path log = Files.createTempFile(project.getParent(), "launcher-", ".log");
        ProcessBuilder builder = new ProcessBuilder(command).directory(project.getParent().toFile())
                .redirectErrorStream(true).redirectOutput(log.toFile());
        builder.environment().put("JAVA_HOME", System.getProperty("java.home"));
        Process process = builder.start();
        try {
            process.getOutputStream().write(input.getBytes(StandardCharsets.UTF_8));
            process.getOutputStream().close();
            check(process.waitFor(45, TimeUnit.SECONDS), "PowerShell launcher timed out");
            String output = Files.readString(log);
            check((process.exitValue() == 0) == success, "Unexpected exit " + process.exitValue() + "\n" + output);
            return output;
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly().waitFor();
            }
        }
    }

    private static void copyTree(Path source, Path destination) throws Exception {
        try (var files = Files.walk(source)) {
            for (Path path : files.toList()) {
                Path target = destination.resolve(source.relativize(path));
                if (Files.isDirectory(path)) {
                    Files.createDirectories(target);
                } else {
                    Files.copy(path, target, StandardCopyOption.COPY_ATTRIBUTES);
                }
            }
        }
    }

    private static void check(boolean value, String message) {
        ConsoleTestSupport.check(value, message);
    }
}
