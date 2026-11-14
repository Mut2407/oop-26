package models;

public class LoaiSP {
    private String maLoaiSP;
    private String tenLoaiSP;
    private String moTa;

    //không tham số
    public LoaiSP() {
        maLoaiSP = "";
        tenLoaiSP = "";
        moTa = "";
    }
    
    //có tham số
    public LoaiSP(String maLoaiSP, String tenLoaiSP, String moTa) {
        kiemTraChuoi(maLoaiSP);
        this.maLoaiSP = maLoaiSP.trim();
        setTenLoaiSP(tenLoaiSP);
        setMoTa(moTa);
    }

    public String getMaLoaiSP() {
        return maLoaiSP;
    }

    public String getTenLoaiSP() {
        return tenLoaiSP;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
        kiemTraChuoi(tenLoaiSP);
        this.tenLoaiSP = tenLoaiSP;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        kiemTraChuoi(moTa);
        this.moTa = moTa;
    }

    public boolean kiemTraHopLe() {
        return !maLoaiSP.isEmpty() && !tenLoaiSP.isEmpty();
    }

    public void capNhatTu(LoaiSP moi) {
        setTenLoaiSP(moi.getTenLoaiSP());
        setMoTa(moi.getMoTa());
    }

    public String hienThiThongTin() {
        return maLoaiSP + " | " + tenLoaiSP + " | " + moTa;
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
