package models;

import java.math.BigDecimal;
import services.DanhSachSanPham;

/** Immutable sale line. The price is captured at transaction time. */
public final class ChiTietHoaDon {
    private final SanPham sanPham;
    private final int soLuong;
    private final BigDecimal donGiaBan;

    public ChiTietHoaDon(SanPham sp, int soLuong, BigDecimal donGia) {
        if (sp == null || soLuong <= 0 || donGia == null || donGia.signum() < 0) {
            throw new IllegalArgumentException("San pham/so luong/don gia khong hop le.");
        }
        this.sanPham = DanhSachSanPham.saoChep(sp);
        this.soLuong = soLuong;
        this.donGiaBan = donGia;
    }
    public SanPham getSanPham() { return DanhSachSanPham.saoChep(sanPham); }
    public int getSoLuong() { return soLuong; }
    public BigDecimal getDonGiaBan() { return donGiaBan; }
    public BigDecimal thanhTien() { return donGiaBan.multiply(BigDecimal.valueOf(soLuong)); }
}
