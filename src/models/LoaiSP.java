package models;

public final class LoaiSP {
    private final String maLoaiSP;
    private String tenLoaiSP;
    private String moTa;

    public LoaiSP() {
        this.maLoaiSP = "";
        this.tenLoaiSP = "";
        this.moTa = "";
    }

    public LoaiSP(String maLoaiSP, String tenLoaiSP, String moTa) {
        this.maLoaiSP = KiemTra.ma(maLoaiSP);
        this.tenLoaiSP = KiemTra.batBuoc(tenLoaiSP, "tenLoaiSP");
        this.moTa = moTa == null ? "" : moTa.trim();
    }

    public String getMaLoaiSP() {
        return maLoaiSP;
    }

    public String getTenLoaiSP() {
        return tenLoaiSP;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
        this.tenLoaiSP = KiemTra.batBuoc(tenLoaiSP, "tenLoaiSP");
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa == null ? "" : moTa.trim();
    }

    public LoaiSP saoChep() {
        return new LoaiSP(maLoaiSP, tenLoaiSP, moTa);
    }

    public String hienThiThongTin() {
        return maLoaiSP + " | " + tenLoaiSP + " | " + moTa;
    }
}
