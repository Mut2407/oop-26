package models;

public class LoaiSP {
    private final String maLoaiSP;
    private String tenLoaiSP;
    private String moTa;

    public LoaiSP() {
        this.maLoaiSP = "";
        this.tenLoaiSP = "";
        this.moTa = "";
    }

    public LoaiSP(String maLoaiSP, String tenLoaiSP, String moTa) {
        kiemTraMa(maLoaiSP);
        this.maLoaiSP = maLoaiSP.trim().toUpperCase(java.util.Locale.ROOT);
        setTenLoaiSP(tenLoaiSP);
        setMoTa(moTa);
    }

    public LoaiSP(LoaiSP cu) {
        this(cu.maLoaiSP, cu.tenLoaiSP, cu.moTa);
    }

    public String getMaLoaiSP() {
        return maLoaiSP;
    }

    public String getTenLoaiSP() {
        return tenLoaiSP;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
        kiemTraChuoi(tenLoaiSP, "tenLoaiSP");
        this.tenLoaiSP = tenLoaiSP.trim();
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        if (moTa == null) {
            moTa = "";
        }
        this.moTa = moTa.trim();
    }

    public void capNhatThongTin(LoaiSP moi) {
        LoaiSP hopLe = new LoaiSP(moi);
        this.tenLoaiSP = hopLe.tenLoaiSP;
        this.moTa = hopLe.moTa;
    }

    public void kiemTraHopLe() {
        new LoaiSP(maLoaiSP, tenLoaiSP, moTa);
    }

    private static void kiemTraMa(String ma) {
        if (ma == null || !ma.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma chi gom chu, so, _ hoac -.");
        }
    }

    private static void kiemTraChuoi(String giaTri, String ten) {
        if (giaTri == null || giaTri.trim().isEmpty()) {
            throw new IllegalArgumentException(ten + " khong duoc rong.");
        }
    }

    public String hienThiThongTin() {
        return maLoaiSP + " | " + tenLoaiSP + " | " + moTa;
    }
}
