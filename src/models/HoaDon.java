package models;

import java.util.Date;

public class HoaDon {
    private String maHoaDon;
    private Date ngayLapHoaDon;
    private String maKhachHang;
    private String maNhanVien;
    private double tongTien;
    private int soLuongMua; 

    public HoaDon(String maHoaDon, Date ngayLapHoaDon, String maKhachHang, String maNhanVien, double tongTien, int soLuongMua) {
        this.maHoaDon = maHoaDon;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.maKhachHang = maKhachHang;
        this.maNhanVien = maNhanVien;
        this.tongTien = tongTien;
        this.soLuongMua = soLuongMua;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public Date getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public double getTongTien() {
        return tongTien;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public void setNgayLapHoaDon(Date ngayLapHoaDon) {
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
    }
}
