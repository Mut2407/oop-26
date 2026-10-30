package models;

public class QuanAoTheThao extends SanPham {
    private String kichCoQuanAo;
    public QuanAoTheThao(String maSanPham, String tenSanPham, double giaban, String thuongHieu, int soLuongTon, String maLoai, String kichCoQuanAo) {
        super(maSanPham, tenSanPham, giaban, thuongHieu, soLuongTon, maLoai);
        this.kichCoQuanAo = kichCoQuanAo;
    }
    public String getKichCoQuanAo() {
        return kichCoQuanAo;
    }

    public void setKichCoQuanAo(String kichCoQuanAo) {
        this.kichCoQuanAo = kichCoQuanAo;
    }

@Override
public String hienThiThongTin() {
    return super.toString() + "\nKich co quan ao: " + kichCoQuanAo;
}
}
