package models;

public class NhaCungCap {
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String diaChi;
    private int soDienThoai;

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String diaChi, int soDienThoai){
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

    public int getsoDienThoai() {
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

    public void setSoDienThoai(int soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    
}
