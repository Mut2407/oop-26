package models;

public final class NhaCungCap {
    private final String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoai;
    private String diaChi;

    public NhaCungCap() {
        this.maNhaCungCap = "";
        this.tenNhaCungCap = "";
        this.soDienThoai = "";
        this.diaChi = "";
    }

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String soDienThoai, String diaChi) {
        this.maNhaCungCap = KiemTra.ma(maNhaCungCap);
        this.tenNhaCungCap = KiemTra.batBuoc(tenNhaCungCap, "tenNhaCungCap");
        this.soDienThoai = KiemTra.dienThoai(soDienThoai);
        this.diaChi = KiemTra.batBuoc(diaChi, "diaChi");
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = KiemTra.batBuoc(tenNhaCungCap, "tenNhaCungCap");
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

    public NhaCungCap saoChep() {
        return new NhaCungCap(maNhaCungCap, tenNhaCungCap, soDienThoai, diaChi);
    }

    public String hienThiThongTin() {
        return maNhaCungCap + " | " + tenNhaCungCap + " | " + soDienThoai + " | " + diaChi;
    }
}
