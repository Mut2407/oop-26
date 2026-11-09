package models;

public final class KhachHang {
    private final String maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;

    public KhachHang() {
        this.maKhachHang = "";
        this.hoTen = "";
        this.soDienThoai = "";
        this.diaChi = "";
    }

    public KhachHang(String maKhachHang, String hoTen, String soDienThoai, String diaChi) {
        this.maKhachHang = KiemTra.ma(maKhachHang);
        this.hoTen = KiemTra.batBuoc(hoTen, "hoTen");
        this.soDienThoai = KiemTra.dienThoai(soDienThoai);
        this.diaChi = KiemTra.batBuoc(diaChi, "diaChi");
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = KiemTra.batBuoc(hoTen, "hoTen");
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = KiemTra.dienThoai(soDienThoai);
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = KiemTra.batBuoc(diaChi, "diaChi");
    }

    public KhachHang saoChep() {
        return new KhachHang(maKhachHang, hoTen, soDienThoai, diaChi);
    }

    public String hienThiThongTin() {
        return maKhachHang + " | " + hoTen + " | " + soDienThoai + " | " + diaChi;
    }
}
