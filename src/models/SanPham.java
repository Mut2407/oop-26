package models;

public abstract class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private double giaban;
    private int soLuongTon;
    private String maLoai;

    public SanPham(String maSanPham, String tenSanPham, double giaban, int soLuongTon, String maLoai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaban = giaban;
        this.soLuongTon = soLuongTon;
        this.maLoai = maLoai;
    }
    public String getMaSanPham() {
        return maSanPham;
    }
    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }
    public String getTenSanPham() {
        return tenSanPham;
    }
    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
    public double getGiaban() {
        return giaban;
    }
    public void setGiaban(double giaban) {
        this.giaban = giaban;
    }
    public int getSoLuongTon() {
        return soLuongTon;
    }
    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
    public String getMaLoai() {
        return maLoai;
    }
    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }
}
