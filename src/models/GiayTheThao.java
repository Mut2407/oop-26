package models;

public class GiayTheThao extends SanPham {
    private double kichCoGiay;
    private String mauSac;
    private String loaiDeGiay;

    public GiayTheThao(String maSanPham, String tenSanPham, double giaBan,
            String thuongHieu, int soLuongTon, String maLoai,
            double kichCoGiay, String mauSac, String loaiDeGiay) {
        super(maSanPham, tenSanPham, giaBan, thuongHieu, soLuongTon, maLoai);
        this.kichCoGiay = kichCoGiay;
        this.mauSac = mauSac;
        this.loaiDeGiay = loaiDeGiay;
    }

    public double getKichCoGiay() {
        return kichCoGiay;
    }

    public void setKichCoGiay(double kichCoGiay) {
        this.kichCoGiay = kichCoGiay;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getLoaiDeGiay() {
        return loaiDeGiay;
    }

    public void setLoaiDeGiay(String loaiDeGiay) {
        this.loaiDeGiay = loaiDeGiay;
    }

    @Override
    public String hienThiThongTin() {
        return super.toString()
                + "\nKich co giay: " + kichCoGiay
                + "\nMau sac: " + mauSac
                + "\nLoai de giay: " + loaiDeGiay;
    }
}
