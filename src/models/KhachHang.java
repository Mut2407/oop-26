package models;

public class KhachHang{
    private String maKhachHang;
    private String hoTen;
    private String diaChi;
    private String soDienThoai;

    public KhachHang(String maKhachHang, String hoTen, String soDienThoai, String diaChi) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
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

    public String getSoDienThoai() {
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
    
    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
    //method
    public String hienThiThongTin() {
        return "Ma khach hang: " + maKhachHang + 
               "\nHo ten: " + hoTen + 
               "\nSo dien thoai: " + soDienThoai + 
               "\nDia chi: " + diaChi;
    }
}