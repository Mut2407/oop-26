package models;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String caLamViec;
    private int soDienThoai;
    private String diaChi;

    public NhanVien(String maNhanVien, String hoTen, String caLamViec, int soDienThoai, String diaChi) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.caLamViec = caLamViec;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getCaLamViec() {
        return caLamViec;
    }

    public int getSoDienThoai() {
        return soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setCaLamViec(String caLamViec) {
        this.caLamViec = caLamViec;
    }
    //method
    public String thongTinNhanVien() {
        return "Ma Nhan Vien: " + maNhanVien + 
               "\nHo ten Nhan Vien: " + hoTen + 
               "\nSo dien thoai: " + soDienThoai + 
               "\nDia chi: " + diaChi;
    }
}
