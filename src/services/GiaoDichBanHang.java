package services;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import models.*;

/** Single-application file transaction. The undo journal is removed at commit. */
public final class GiaoDichBanHang {
    private final Path thuMuc;
    private final LuuTruFileText luuTru;
    private static final String JOURNAL = "banhang.undo";
    private static final List<String> FILES = List.of("sanpham.txt", "hoadon.txt");
    public GiaoDichBanHang(Path dir, LuuTruFileText store) {
        thuMuc = dir.toAbsolutePath().normalize();
        luuTru = Objects.requireNonNull(store);
    }
    private static String hash(byte[] bytes) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)); }
        catch (java.security.NoSuchAlgorithmException e) { throw new AssertionError(e); }
    }
    public static void phucHoi(Path dir) throws IOException {
        Path journal = dir.resolve(JOURNAL);
        if (!Files.exists(journal)) return;
        List<String[]> rows = FileText.doc(journal.toString(), "BAN_HANG_UNDO_V1");
        if (rows.size() != 2) throw new IOException("Journal ban hang khong hop le; khong the khoi dong.");
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
    public void xacNhan(String ma, DanhSachHoaDon hd, DanhSachSanPham sp,
            DanhSachKhachHang kh, DanhSachNhanVien nv) throws IOException {
        Files.createDirectories(thuMuc);
        try (FileChannel channel = FileChannel.open(thuMuc.resolve("banhang.lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                var lock = channel.tryLock()) {
            if (lock == null) throw new IOException("Dang co giao dich ban hang khac.");
            if (Files.exists(thuMuc.resolve(JOURNAL))) throw new IOException("Can khoi dong lai de phuc hoi giao dich.");
            HoaDon invoice = hd.timTheoMa(ma);
            if (invoice == null || kh.timTheoMa(invoice.getMaKhachHang()) == null || nv.timTheoMa(invoice.getMaNhanVien()) == null) {
                throw new IllegalArgumentException("Hoa don/khach hang/nhan vien khong ton tai.");
            }
            // Validate existing files before publishing an undo journal.
            if (Files.exists(thuMuc.resolve("sanpham.txt"))) new DanhSachSanPham(0).docFile(thuMuc.resolve("sanpham.txt").toString());
            if (Files.exists(thuMuc.resolve("hoadon.txt"))) luuTru.docHoaDon("hoadon.txt", sp, kh, nv.getTatCa());
            DanhSachSanPham nextSP = sp.banSao();
            DanhSachHoaDon nextHD = hd.banSao();
            nextHD.xacNhanBanHang(ma, new DichVuKho(nextSP));
            List<String> journal = new ArrayList<>();
            journal.add("BAN_HANG_UNDO_V1");
            for (String file : FILES) {
                Path path = thuMuc.resolve(file);
                boolean exists = Files.exists(path);
                byte[] bytes = exists ? Files.readAllBytes(path) : new byte[0];
                journal.add(FileText.dong(file, exists ? "1" : "0", hash(bytes), Base64.getEncoder().encodeToString(bytes)));
            }
            FileText.ghi(thuMuc.resolve(JOURNAL).toString(), journal);
            try {
                luuTru.ghiSanPham("sanpham.txt", nextSP);
                luuTru.ghiHoaDon("hoadon.txt", nextHD);
                // Verify both files before committing. Reading never applies inventory changes.
                DanhSachSanPham verifySP = new DanhSachSanPham(0);
                verifySP.docFile(thuMuc.resolve("sanpham.txt").toString());
                luuTru.docHoaDon("hoadon.txt", verifySP, kh, nv.getTatCa());
                Files.delete(thuMuc.resolve(JOURNAL));
            } catch (IOException | RuntimeException e) {
                try { phucHoi(thuMuc); }
                catch (IOException recovery) {
                    recovery.addSuppressed(e);
                    throw new PhucHoiBatBuoc(recovery);
                }
                throw e;
            }
            sp.thayDuLieu(nextSP);
            hd.thayDuLieu(nextHD);
        }
    }
    /** An unrecoverable write stops the application rather than allowing more writes. */
    public static final class PhucHoiBatBuoc extends Error {
        PhucHoiBatBuoc(IOException cause) { super("Khong the phuc hoi. Dung ung dung; giu banhang.undo va xu ly loi file truoc khi mo lai.", cause); }
    }
}
