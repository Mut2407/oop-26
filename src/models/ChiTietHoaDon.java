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
        if (sanPham == null || !sanPham.kiemTraHopLe())
            throw new IllegalArgumentException("SP khong hop le.");
        this.sanPham = sanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong <= 0)
            throw new IllegalArgumentException("So luong phai > 0.");
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGiaBan() {
        return donGiaBan;
    }

    public void setDonGiaBan(BigDecimal donGiaBan) {
        if (donGiaBan == null || donGiaBan.signum() < 0)
            throw new IllegalArgumentException("Don gia phai >= 0.");
        this.donGiaBan = donGiaBan;
    }

    public boolean kiemTraHopLe() {
        return sanPham != null && sanPham.kiemTraHopLe() && soLuong > 0;
    }

    public BigDecimal tinhThanhTien() {
        return donGiaBan.multiply(BigDecimal.valueOf(soLuong));
    }

    public String hienThiThongTin() {
        return sanPham.getMaSanPham() + " | SL: " + soLuong
                + " | Don gia: " + donGiaBan.toPlainString() + " | Thanh tien: " + tinhThanhTien().toPlainString();
    }

}
