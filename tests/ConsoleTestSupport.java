import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

final class ConsoleTestSupport {
    private ConsoleTestSupport() {
    }

    static String run(Path dir, String input, int exitCode) throws Exception {
        try (Session session = new Session(dir)) {
            session.send(input);
            return session.finish(exitCode);
        }
    }

    static String lines(String... lines) {
        return String.join("\n", lines) + "\n";
    }

    static void deleteTree(Path dir) throws Exception {
        try (var paths = Files.walk(dir)) {
            for (Path p : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(p);
            }
        }
    }

    static void check(boolean value, String message) {
        if (!value) {
            throw new AssertionError(message);
        }
    }

    static final class Session implements AutoCloseable {
        private final Process process;
        private final Path log;

        Session(Path dir) throws Exception {
            String classpath = Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
                    .map(path -> Path.of(path).toAbsolutePath().toString())
                    .collect(Collectors.joining(File.pathSeparator));
            log = dir.resolve("console-" + UUID.randomUUID() + ".log");
            process = new ProcessBuilder(Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                    "-Dfile.encoding=UTF-8", "-cp", classpath, "Main")
                    .directory(dir.toFile()).redirectErrorStream(true).redirectOutput(log.toFile()).start();
        }

        void send(String input) throws Exception {
            process.getOutputStream().write(input.getBytes(StandardCharsets.UTF_8));
            process.getOutputStream().flush();
        }

        void awaitOutput(String expected) throws Exception {
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (System.nanoTime() < deadline) {
                if (Files.exists(log) && Files.readString(log).contains(expected)) {
                    return;
                }
                if (!process.isAlive()) {
                    break;
                }
                Thread.sleep(10);
            }
            throw new AssertionError("Missing console output: " + expected + "\n" + Files.readString(log));
        }

        String finish(int exitCode) throws Exception {
            process.getOutputStream().close();
            if (!process.waitFor(20, TimeUnit.SECONDS)) {
                throw new AssertionError("Console timeout\n" + Files.readString(log));
            }
            String text = Files.readString(log);
            check(process.exitValue() == exitCode, "Unexpected exit " + process.exitValue() + "\n" + text);
            return text;
        }

        @Override
        public void close() throws Exception {
            if (process.isAlive()) {
                process.destroyForcibly().waitFor();
            }
        }
    }
}
