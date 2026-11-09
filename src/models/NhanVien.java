package models;

public class NhanVien {
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
        kiemTraMa(maNhanVien);
        this.maNhanVien = maNhanVien.trim().toUpperCase(java.util.Locale.ROOT);
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setCaLamViec(caLamViec);
    }

    public NhanVien(NhanVien cu) {
        this(cu.maNhanVien, cu.hoTen, cu.soDienThoai, cu.caLamViec);
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        kiemTraChuoi(hoTen, "hoTen");
        this.hoTen = hoTen.trim();
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

    public String getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(String caLamViec) {
        kiemTraChuoi(caLamViec, "caLamViec");
        this.caLamViec = caLamViec.trim();
    }

    public void capNhatThongTin(NhanVien moi) {
        NhanVien hopLe = new NhanVien(moi);
        this.hoTen = hopLe.hoTen;
        this.soDienThoai = hopLe.soDienThoai;
        this.caLamViec = hopLe.caLamViec;
    }

    public void kiemTraHopLe() {
        new NhanVien(maNhanVien, hoTen, soDienThoai, caLamViec);
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
        return maNhanVien + " | " + hoTen + " | " + soDienThoai + " | " + caLamViec;
    }
}
