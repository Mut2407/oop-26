package models;

import java.math.BigDecimal;

public final class ChiTietPhieuNhap {
    private SanPham sanPham;
    private int soLuong;
    private BigDecimal donGiaNhap = BigDecimal.ZERO;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(SanPham sanPham, int soLuong, BigDecimal donGiaNhap) {
        setSanPham(sanPham);
        setSoLuong(soLuong);
        setDonGiaNhap(donGiaNhap);
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

    public BigDecimal getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(BigDecimal donGiaNhap) {
        this.donGiaNhap = KiemTra.tien(donGiaNhap);
    }

    public BigDecimal tinhThanhTien() {
        return donGiaNhap.multiply(BigDecimal.valueOf(soLuong));
    }

    public ChiTietPhieuNhap saoChep() {
        return new ChiTietPhieuNhap(sanPham, soLuong, donGiaNhap);
    }
}
