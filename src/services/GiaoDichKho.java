package services;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import models.*;

/** Single-application file transaction. The undo journal is removed at commit. */
public final class GiaoDichKho {
    private static final String JOURNAL = "banhang.undo";

    private GiaoDichKho() { }
    private static String hash(byte[] bytes) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)); }
        catch (java.security.NoSuchAlgorithmException e) { throw new AssertionError(e); }
    }
    public static void phucHoi(Path dir) throws IOException {
        Path journal = dir.resolve(JOURNAL);
        if (!Files.exists(journal)) return;
        List<String[]> rows = FileText.doc(journal.toString(), "BAN_HANG_UNDO_V1");
        if (rows.size() != 2) throw new IOException("Journal ban hang khong hop le; khong the khoi dong.");
        String document = rows.get(1).length == 4 ? rows.get(1)[0] : "";
        if (!List.of("hoadon.txt", "phieunhap.txt").contains(document)) throw new IOException("Loai journal khong hop le.");
        List<String> FILES = List.of("sanpham.txt", document);
        List<byte[]> snapshots = new ArrayList<>();
        try {
            for (int i = 0; i < 2; i++) {
                String[] r = rows.get(i);
                if (r.length != 4 || !r[0].equals(FILES.get(i)) || !(r[1].equals("0") || r[1].equals("1"))) throw new IllegalArgumentException("Sai journal.");
                byte[] bytes = Base64.getDecoder().decode(r[3]);
                if (!hash(bytes).equals(r[2]) || (r[1].equals("0") && bytes.length != 0)) throw new IllegalArgumentException("Journal khong khop checksum.");
                snapshots.add(bytes);
            }
        } catch (IllegalArgumentException e) { throw new IOException("Journal bi loi; can phuc hoi thu cong.", e); }
        // Validate both snapshots before restoring either; keep journal until both succeed.
        for (int i = 0; i < 2; i++) {
            Path target = dir.resolve(FILES.get(i));
            if (rows.get(i)[1].equals("0")) Files.deleteIfExists(target);
            else ghiBytes(target, snapshots.get(i));
        }
        Files.delete(journal);
    }
    private static void ghiBytes(Path target, byte[] bytes) throws IOException {
        Path temp = Files.createTempFile(target.getParent(), "undo-", ".tmp");
        try {
            Files.write(temp, bytes);
            try (FileChannel channel = FileChannel.open(temp, StandardOpenOption.WRITE)) { channel.force(true); }
            try { Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
            catch (AtomicMoveNotSupportedException e) { Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING); }
        } finally { Files.deleteIfExists(temp); }
    }
    @FunctionalInterface interface ViecIO { void chay() throws IOException; }
    static void thucHien(Path dir, String document, ViecIO prepare, ViecIO write, ViecIO verify, Runnable commit) throws IOException {
        Files.createDirectories(dir);
        try (FileChannel channel = FileChannel.open(dir.resolve("banhang.lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                var lock = channel.tryLock()) {
            if (lock == null) throw new IOException("Dang co giao dich kho khac.");
            if (Files.exists(dir.resolve(JOURNAL))) throw new IOException("Can khoi dong lai de phuc hoi giao dich kho.");
            prepare.chay();
            List<String> journal = new ArrayList<>();
            journal.add("BAN_HANG_UNDO_V1");
            for (String file : List.of("sanpham.txt", document)) {
                Path path = dir.resolve(file);
                boolean exists = Files.exists(path);
                byte[] bytes = exists ? Files.readAllBytes(path) : new byte[0];
                journal.add(FileText.dong(file, exists ? "1" : "0", hash(bytes), Base64.getEncoder().encodeToString(bytes)));
            }
            FileText.ghi(dir.resolve(JOURNAL).toString(), journal);
            try {
                write.chay();
                verify.chay();
                Files.delete(dir.resolve(JOURNAL));
            } catch (IOException | RuntimeException e) {
                try { phucHoi(dir); }
                catch (IOException recovery) {
                    recovery.addSuppressed(e);
                    throw new GiaoDichBanHang.PhucHoiBatBuoc(recovery);
                }
                throw e;
            }
            commit.run();
        }
    }
}
