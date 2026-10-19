package models;

public class ChiTietPhieuNhap {
    private String maPhieuNhap;
    private SanPham sanPham;
    private double giaNhap;
    private double tongTien;

    public ChiTietPhieuNhap(String maPhieuNhap, SanPham sanPham, double giaNhap, double tongTien) {
        this.maPhieuNhap = maPhieuNhap;
        this.sanPham = sanPham;
        this.giaNhap = giaNhap;
        this.tongTien = tongTien;
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}