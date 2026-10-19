package models;

public class KhachHang{
    private String maKhachHang;
    private String hoTen;
    private String diaChi;
    private int soDienThoai;

    public KhachHang(String maKhachHang, String hoTen, String diaChi, int soDienThoai) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
    }

    public String getMaKhachHang() {
         return maKhachHang; 
    }

    public String getHoTen() {
         return hoTen; 
    }

    public String getDiaChi() {
         return diaChi; 
    }

    public int getSoDienThoai() {
         return soDienThoai; 
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang; 
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    public void setSoDienThoai(int soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}