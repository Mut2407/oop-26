package models;

public class NhaCungCap {
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
        kiemTraMa(maNhaCungCap);
        this.maNhaCungCap = maNhaCungCap.trim().toUpperCase(java.util.Locale.ROOT);
        setTenNhaCungCap(tenNhaCungCap);
        setSoDienThoai(soDienThoai);
        setDiaChi(diaChi);
    }

    public NhaCungCap(NhaCungCap cu) {
        this(cu.maNhaCungCap, cu.tenNhaCungCap, cu.soDienThoai, cu.diaChi);
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        kiemTraChuoi(tenNhaCungCap, "tenNhaCungCap");
        this.tenNhaCungCap = tenNhaCungCap.trim();
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.trim().matches("\\+?[0-9]{9,15}")) {
            throw new IllegalArgumentException("So dien thoai phai co 9-15 chu so.");
        }
        this.soDienThoai = soDienThoai.trim();
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        kiemTraChuoi(diaChi, "diaChi");
        this.diaChi = diaChi.trim();
    }

    public void capNhatThongTin(NhaCungCap moi) {
        NhaCungCap hopLe = new NhaCungCap(moi);
        this.tenNhaCungCap = hopLe.tenNhaCungCap;
        this.soDienThoai = hopLe.soDienThoai;
        this.diaChi = hopLe.diaChi;
    }

    public void kiemTraHopLe() {
        new NhaCungCap(maNhaCungCap, tenNhaCungCap, soDienThoai, diaChi);
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
        return maNhaCungCap + " | " + tenNhaCungCap + " | " + soDienThoai + " | " + diaChi;
    }
}
