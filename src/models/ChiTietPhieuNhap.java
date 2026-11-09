package models;

import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private SanPham sanPham;
    private int soLuong;
    private BigDecimal donGiaNhap;

    public ChiTietPhieuNhap() {
        donGiaNhap = BigDecimal.ZERO;
    }

    public ChiTietPhieuNhap(SanPham sanPham, int soLuong, BigDecimal donGiaNhap) {
        setSanPham(sanPham);
        setSoLuong(soLuong);
        setDonGiaNhap(donGiaNhap);
    }

    public ChiTietPhieuNhap(ChiTietPhieuNhap cu) {
        this(cu.sanPham, cu.soLuong, cu.donGiaNhap);
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        if (sanPham == null) {
            throw new IllegalArgumentException("San pham khong duoc null.");
        }
        sanPham.kiemTraHopLe();
        this.sanPham = sanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("So luong phai > 0.");
        }
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(BigDecimal donGiaNhap) {
        if (donGiaNhap == null || donGiaNhap.signum() < 0) {
            throw new IllegalArgumentException("Don gia phai >= 0.");
        }
        this.donGiaNhap = donGiaNhap;
    }

    public BigDecimal tinhThanhTien() {
        return donGiaNhap.multiply(BigDecimal.valueOf(soLuong));
    }
}
