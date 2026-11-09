package models;

import java.math.BigDecimal;

public class ChiTietHoaDon {
    private SanPham sanPham;
    private int soLuong;
    private BigDecimal donGiaBan;

    public ChiTietHoaDon() {
        donGiaBan = BigDecimal.ZERO;
    }

    public ChiTietHoaDon(SanPham sanPham, int soLuong, BigDecimal donGiaBan) {
        setSanPham(sanPham);
        setSoLuong(soLuong);
        setDonGiaBan(donGiaBan);
    }

    public ChiTietHoaDon(ChiTietHoaDon cu) {
        this(cu.sanPham, cu.soLuong, cu.donGiaBan);
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

    public BigDecimal getDonGiaBan() {
        return donGiaBan;
    }

    public void setDonGiaBan(BigDecimal donGiaBan) {
        if (donGiaBan == null || donGiaBan.signum() < 0) {
            throw new IllegalArgumentException("Don gia phai >= 0.");
        }
        this.donGiaBan = donGiaBan;
    }

    public BigDecimal tinhThanhTien() {
        return donGiaBan.multiply(BigDecimal.valueOf(soLuong));
    }
}
