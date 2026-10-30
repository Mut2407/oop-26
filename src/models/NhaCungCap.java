package models;

public class NhaCungCap {
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String diaChi;
    private String soDienThoai;

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String diaChi, String soDienThoai){
        this.maNhaCungCap = maNhaCungCap;
        this.tenNhaCungCap = tenNhaCungCap;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
    }
    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }
    public String getDiaChi() {
        return diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    } 
    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    //method
    public String thongTinNhaCungCap() {
        return "Ma nha cung cap: " + maNhaCungCap
               + "\nTen nha cung cap: " + tenNhaCungCap
               + "\nSo dien thoai: " + soDienThoai
               + "\nDia chi: " + diaChi;
    }
}
