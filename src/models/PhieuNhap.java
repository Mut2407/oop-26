package models;

import java.util.Date;

public class PhieuNhap {
    private String maPhieuNhap;
    private Date ngayLapPhieuNhap;
    private String maNhanVien;
    private String maNhaCungCap;
    private double tongTien;

    public PhieuNhap(String maPhieuNhap, Date ngayLapPhieuNhap, String maNhanVien, String maNhaCungCap, double tongTien){
        this.maPhieuNhap = maPhieuNhap;
        this.ngayLapPhieuNhap = ngayLapPhieuNhap;
        this.maNhanVien = maNhanVien;
        this.maNhaCungCap = maNhaCungCap;
        this.tongTien = tongTien;

    }
    
    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }
    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }
    public Date getNgayLapPhieuNhap() {
        return ngayLapPhieuNhap;
    }
    public void setNgayLapPhieuNhap(Date ngayLapPhieuNhap) {
        this.ngayLapPhieuNhap = ngayLapPhieuNhap;
    }
    public String getMaNhanVien() {
        return maNhanVien;
    }
    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }
    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }
    public double getTongTien() {
        return tongTien;
    }
    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
