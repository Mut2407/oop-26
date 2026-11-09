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

    public BigDecimal getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(BigDecimal donGiaNhap) {
        if (donGiaNhap == null || donGiaNhap.signum() < 0)
            throw new IllegalArgumentException("Don gia phai >= 0.");
        this.donGiaNhap = donGiaNhap;
    }

    public boolean kiemTraHopLe() {
        return sanPham != null && sanPham.kiemTraHopLe() && soLuong > 0;
    }

    public BigDecimal tinhThanhTien() {
        return donGiaNhap.multiply(BigDecimal.valueOf(soLuong));
    }

    public String hienThiThongTin() {
        return sanPham.getMaSanPham() + " | SL: " + soLuong
                + " | Don gia: " + donGiaNhap.toPlainString() + " | Thanh tien: " + tinhThanhTien().toPlainString();
    }

}
