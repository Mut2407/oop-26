package models;

public class QuanAoTheThao extends SanPham {
    private double kichCoGiay;
    private String loaiDeGiay;
    public QuanAoTheThao(String maSanPham, String tenSanPham, double giaban, String thuongHieu, int soLuongTon, String maLoai, double kichCoGiay, String loaiDeGiay) {
        super(maSanPham, tenSanPham, giaban, thuongHieu, soLuongTon, maLoai);
        this.kichCoGiay =kichCoGiay;
        this.loaiDeGiay = loaiDeGiay;
    }
    public double getKichCoGiay() {
        return kichCoGiay;
    }

    public void setKichCoGiay(double kichCoGiay) {
        this.kichCoGiay =kichCoGiay;
    }

    public String getLoaiDeGiay() {
        return loaiDeGiay;
    }

    public void setLoaiDeGiay(String loaiDeGiay) {
        this.loaiDeGiay = loaiDeGiay;
    }

@Override
public String hienThiThongTin() {
    return "Quan ao: " + getTenSanPham()
            + "\nkich co: " + kichCoGiay
            + "\nchat lieu: " + loaiDeGiay;
}
}