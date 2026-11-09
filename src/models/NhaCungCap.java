package models;

public class NhaCungCap {
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoai;
    private String diaChi;

    public NhaCungCap() {
        maNhaCungCap = "";
        tenNhaCungCap = "";
        soDienThoai = "";
        diaChi = "";
    }

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String soDienThoai, String diaChi) {
        kiemTraChuoi(maNhaCungCap);
        this.maNhaCungCap = maNhaCungCap.trim();
        setTenNhaCungCap(tenNhaCungCap);
        setSoDienThoai(soDienThoai);
        setDiaChi(diaChi);
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        kiemTraChuoi(tenNhaCungCap);
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        kiemTraChuoi(soDienThoai);
        if (!soDienThoai.matches("[0-9]{9,11}"))
            throw new IllegalArgumentException("SDT can 9 den 11 chu so.");
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        kiemTraChuoi(diaChi);
        this.diaChi = diaChi;
    }

    public boolean kiemTraHopLe() {
        return !maNhaCungCap.isEmpty() && !tenNhaCungCap.isEmpty();
    }

    public void capNhatTu(NhaCungCap moi) {
        setTenNhaCungCap(moi.getTenNhaCungCap());
        setSoDienThoai(moi.getSoDienThoai());
        setDiaChi(moi.getDiaChi());
    }

    public String hienThiThongTin() {
        return maNhaCungCap + " | " + tenNhaCungCap + " | " + soDienThoai + " | " + diaChi;
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
