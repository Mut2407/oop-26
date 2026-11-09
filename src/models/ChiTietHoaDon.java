package models;

import java.math.BigDecimal;

public final class ChiTietHoaDon {
    private SanPham sanPham;
    private int soLuong;
    private BigDecimal donGiaBan = BigDecimal.ZERO;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(SanPham sanPham, int soLuong, BigDecimal donGiaBan) {
        setSanPham(sanPham);
        setSoLuong(soLuong);
        setDonGiaBan(donGiaBan);
    }

    public SanPham getSanPham() {
        return sanPham == null ? null : sanPham.saoChep();
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = KiemTra.khacNull(sanPham, "San pham").saoChep();
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = KiemTra.soDuong(soLuong);
    }

    public BigDecimal getDonGiaBan() {
        return donGiaBan;
    }

    public void setDonGiaBan(BigDecimal donGiaBan) {
        this.donGiaBan = KiemTra.tien(donGiaBan);
    }

    public BigDecimal tinhThanhTien() {
        return donGiaBan.multiply(BigDecimal.valueOf(soLuong));
    }

    public ChiTietHoaDon saoChep() {
        return new ChiTietHoaDon(sanPham, soLuong, donGiaBan);
    }
}
