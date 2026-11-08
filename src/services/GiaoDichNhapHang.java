package services;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import models.*;

public final class GiaoDichNhapHang {
    private final Path thuMuc;
    private final LuuTruFileText luuTru;
    public GiaoDichNhapHang(Path dir, LuuTruFileText store) {
        thuMuc = dir.toAbsolutePath().normalize();
        luuTru = Objects.requireNonNull(store);
    }
    public static void phucHoi(Path dir) throws IOException { GiaoDichKho.phucHoi(dir); }
    public void xacNhan(String ma, DanhSachPhieuNhap pn, DanhSachSanPham sp,
            DanhSachNhaCungCap ncc, DanhSachNhanVien nv) throws IOException {
        DanhSachSanPham nextSP = sp.banSao();
        DanhSachPhieuNhap nextPN = pn.banSao();
        GiaoDichKho.thucHien(thuMuc, "phieunhap.txt", () -> {
            PhieuNhap receipt = pn.timTheoMa(ma);
            if (receipt == null || ncc.timTheoMa(receipt.getMaNhaCungCap()) == null || nv.timTheoMa(receipt.getMaNhanVien()) == null) {
                throw new IllegalArgumentException("Phieu nhap/nha cung cap/nhan vien khong ton tai.");
            }
            // Validate existing data before journal publication.
            if (Files.exists(thuMuc.resolve("sanpham.txt"))) new DanhSachSanPham(0).docFile(thuMuc.resolve("sanpham.txt").toString());
            if (Files.exists(thuMuc.resolve("phieunhap.txt"))) luuTru.docPhieuNhap("phieunhap.txt", sp, ncc, nv.getTatCa());
            nextPN.xacNhanNhapHang(ma, new DichVuKho(nextSP));
        }, () -> {
            luuTru.ghiSanPham("sanpham.txt", nextSP);
            luuTru.ghiPhieuNhap("phieunhap.txt", nextPN);
        }, () -> {
            DanhSachSanPham verifySP = new DanhSachSanPham(0);
            verifySP.docFile(thuMuc.resolve("sanpham.txt").toString());
            luuTru.docPhieuNhap("phieunhap.txt", verifySP, ncc, nv.getTatCa());
        }, () -> {
            sp.thayDuLieu(nextSP);
            pn.thayDuLieu(nextPN);
        });
    }
}
