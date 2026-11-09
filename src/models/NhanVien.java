package models;

public final class NhanVien {
    private final String maNhanVien;
    private String hoTen;
    private String soDienThoai;
    private String caLamViec;

    public NhanVien() {
        this.maNhanVien = "";
        this.hoTen = "";
        this.soDienThoai = "";
        this.caLamViec = "";
    }

    public NhanVien(String maNhanVien, String hoTen, String soDienThoai, String caLamViec) {
        this.maNhanVien = KiemTra.ma(maNhanVien);
        this.hoTen = KiemTra.batBuoc(hoTen, "hoTen");
        this.soDienThoai = KiemTra.dienThoai(soDienThoai);
        this.caLamViec = KiemTra.batBuoc(caLamViec, "caLamViec");
    }

    public String getMaNhanVien() {
        return maNhanVien;
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

    public String getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(String caLamViec) {
        this.caLamViec = KiemTra.batBuoc(caLamViec, "caLamViec");
    }

    public NhanVien saoChep() {
        return new NhanVien(maNhanVien, hoTen, soDienThoai, caLamViec);
    }

    public String hienThiThongTin() {
        return maNhanVien + " | " + hoTen + " | " + soDienThoai + " | " + caLamViec;
    }
}
