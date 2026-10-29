package models;

public class DungCuTheThao extends SanPham{
    private String monTheThao;
    private String vatLieu;
    private double khoiLuong;
    public DungCuTheThao(String maSanPham, String tenSanPham, double giaban, String thuongHieu, int soLuongTon, String maLoai, String monTheThao, String vatLieu, double khoiLuong) {
        super(maSanPham, tenSanPham, giaban, thuongHieu ,soLuongTon, maLoai);
        this.monTheThao = monTheThao;
        this.vatLieu = vatLieu;
        this.khoiLuong = khoiLuong;
    }
    public String getMonTheThao() {
        return monTheThao;
    }
    public void setMonTheThao(String monTheThao) {
        this.monTheThao = monTheThao;
    }
    public String getVatLieu() {
        return vatLieu;
    }
    public void setVatLieu(String vatLieu) {
        this.vatLieu = vatLieu;
    }
    public double getKhoiLuong() {
        return khoiLuong;
    }
    public void setKhoiLuong(double khoiLuong) {
        this.khoiLuong = khoiLuong;
    }
    @Override
    public String hienThiThongTin() {
    return super.toString()
            + "\nmon the thao: " + monTheThao
            + "\nvat lieu: " + vatLieu
            + "\nkhoi luong: " + khoiLuong + " kg";
    }
}
