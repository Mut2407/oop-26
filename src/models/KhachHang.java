package models;

public class KhachHang {
    private final String maKhachHang;
    private String hoTen;
    private String diaChi;
    private String soDienThoai;

    public KhachHang(String ma, String ten, String sdt, String diaChi) {
        if (ma == null || !ma.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma khach hang khong hop le.");
        }
        this.maKhachHang = ma.trim();
        capNhatThongTin(ten, sdt, diaChi);
    }

    public String getMaKhachHang() {
        return this.maKhachHang;
    }

    public String getHoTen() {
        return this.hoTen;
    }

    public String getDiaChi() {
        return this.diaChi;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public void capNhatThongTin(String ten, String sdt, String diaChi) {
        if (ten == null || ten.isBlank() || diaChi == null || diaChi.isBlank()) {
            throw new IllegalArgumentException("Ho ten va dia chi khong duoc de trong.");
        }
        if (sdt == null || !sdt.trim().matches("\\+?[0-9]{9,15}")) {
            throw new IllegalArgumentException("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        // Validate every field before changing any state.
        this.hoTen = ten.trim();
        this.soDienThoai = sdt.trim();
        this.diaChi = diaChi.trim();
    }

    public String hienThiThongTin() {
        return "Ma khach hang: " + this.maKhachHang + "\nHo ten: " + this.hoTen + "\nSo dien thoai: " + this.soDienThoai + "\nDia chi: " + this.diaChi;
    }

    /** Adapter for callers using the old display API. */
    @Deprecated
    public String thongTinKhachHang() {
        return hienThiThongTin();
    }
}

