package models;

import java.math.BigDecimal;
import services.DanhSachSanPham;

/** Immutable receiving line. The price is captured at transaction time. */
public final class ChiTietPhieuNhap {
    private final SanPham sanPham;
    private final int soLuong;
    private final BigDecimal donGiaNhap;

    public ChiTietPhieuNhap(SanPham sp, int soLuong, BigDecimal donGia) {
        if (sp == null || soLuong <= 0 || donGia == null || donGia.signum() < 0) {
            throw new IllegalArgumentException("San pham/so luong/don gia khong hop le.");
        }
        this.sanPham = DanhSachSanPham.saoChep(sp);
        this.soLuong = soLuong;
        this.donGiaNhap = donGia;
    }
    public SanPham getSanPham() { return DanhSachSanPham.saoChep(sanPham); }
    public int getSoLuong() { return soLuong; }
    public BigDecimal getDonGiaNhap() { return donGiaNhap; }
    public BigDecimal thanhTienNhap() { return donGiaNhap.multiply(BigDecimal.valueOf(soLuong)); }
}
