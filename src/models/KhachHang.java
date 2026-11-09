package models;

public class KhachHang {
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
        kiemTraMa(maKhachHang);
        this.maKhachHang = maKhachHang.trim().toUpperCase(java.util.Locale.ROOT);
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setDiaChi(diaChi);
    }

    public KhachHang(KhachHang cu) {
        this(cu.maKhachHang, cu.hoTen, cu.soDienThoai, cu.diaChi);
    }

    public String getMaKhachHang() {
        return maKhachHang;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        kiemTraChuoi(diaChi, "diaChi");
        this.diaChi = diaChi.trim();
    }

    public void capNhatThongTin(KhachHang moi) {
        KhachHang hopLe = new KhachHang(moi);
        this.hoTen = hopLe.hoTen;
        this.soDienThoai = hopLe.soDienThoai;
        this.diaChi = hopLe.diaChi;
    }

    public void kiemTraHopLe() {
        new KhachHang(maKhachHang, hoTen, soDienThoai, diaChi);
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
        return maKhachHang + " | " + hoTen + " | " + soDienThoai + " | " + diaChi;
    }
}
