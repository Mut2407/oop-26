package services;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import models.*;

public final class GiaoDichBanHang {
    private final Path thuMuc;
    private final LuuTruFileText luuTru;
    public GiaoDichBanHang(Path dir, LuuTruFileText store) {
        thuMuc = dir.toAbsolutePath().normalize();
        luuTru = Objects.requireNonNull(store);
    }
    public static void phucHoi(Path dir) throws IOException { GiaoDichKho.phucHoi(dir); }
    public void xacNhan(String ma, DanhSachHoaDon hd, DanhSachSanPham sp,
            DanhSachKhachHang kh, DanhSachNhanVien nv) throws IOException {
        DanhSachSanPham nextSP = sp.banSao();
        DanhSachHoaDon nextHD = hd.banSao();
        GiaoDichKho.thucHien(thuMuc, "hoadon.txt", () -> {
            HoaDon invoice = hd.timTheoMa(ma);
            if (invoice == null || kh.timTheoMa(invoice.getMaKhachHang()) == null || nv.timTheoMa(invoice.getMaNhanVien()) == null) {
                throw new IllegalArgumentException("Hoa don/khach hang/nhan vien khong ton tai.");
            }
            // Validate existing data before journal publication.
            if (Files.exists(thuMuc.resolve("sanpham.txt"))) new DanhSachSanPham(0).docFile(thuMuc.resolve("sanpham.txt").toString());
            if (Files.exists(thuMuc.resolve("hoadon.txt"))) luuTru.docHoaDon("hoadon.txt", sp, kh, nv.getTatCa());
            nextHD.xacNhanBanHang(ma, new DichVuKho(nextSP));
        }, () -> {
            luuTru.ghiSanPham("sanpham.txt", nextSP);
            luuTru.ghiHoaDon("hoadon.txt", nextHD);
        }, () -> {
            DanhSachSanPham verifySP = new DanhSachSanPham(0);
            verifySP.docFile(thuMuc.resolve("sanpham.txt").toString());
            luuTru.docHoaDon("hoadon.txt", verifySP, kh, nv.getTatCa());
        }, () -> {
            sp.thayDuLieu(nextSP);
            hd.thayDuLieu(nextHD);
        });
    }
    public static final class PhucHoiBatBuoc extends Error {
        PhucHoiBatBuoc(IOException cause) { super("Khong the phuc hoi. Dung ung dung; giu banhang.undo de phuc hoi.", cause); }
    }
}
