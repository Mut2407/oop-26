package models;

public class NhanVien {
    private final String maNhanVien;
    private final String hoTen;
    private String caLamViec;
    private String soDienThoai;

    /** UML order: code, name, phone, shift (unlike the old five-argument constructor). */
    public NhanVien(String ma, String ten, String sdt, String ca) {
        if (ma == null || !ma.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma nhan vien khong hop le.");
        }
        if (ten == null || ten.isBlank()) {
            throw new IllegalArgumentException("Ho ten khong duoc de trong.");
        }
        this.maNhanVien = ma.trim();
        this.hoTen = ten.trim();
        capNhatLienHe(sdt);
        doiCaLamViec(ca);
    }

    public String getMaNhanVien() {
        return this.maNhanVien;
    }

    public String getHoTen() {
        return this.hoTen;
    }

    public String getCaLamViec() {
        return this.caLamViec;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public void doiCaLamViec(String caMoi) {
        if (caMoi == null || caMoi.isBlank()) {
            throw new IllegalArgumentException("Ca lam viec khong duoc de trong.");
        }
        this.caLamViec = caMoi.trim();
    }

    public void capNhatLienHe(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.trim().matches("\\+?[0-9]{9,15}")) {
            throw new IllegalArgumentException("So dien thoai phai co 9-15 chu so, co the bat dau bang +.");
        }
        this.soDienThoai = soDienThoai.trim();
    }

    public String hienThiThongTin() {
        return "Ma Nhan Vien: " + this.maNhanVien + "\nHo ten Nhan Vien: " + this.hoTen + "\nSo dien thoai: " + this.soDienThoai + "\nCa lam viec: " + this.caLamViec;
    }

    /** Adapter for the old display API; legacy addresses belong to list metadata. */
    @Deprecated
    public String thongTinNhanVien() {
        return hienThiThongTin();
    }
}

