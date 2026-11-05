/*
 * Decompiled with CFR 0.152.
 */
package models;

public class NhaCungCap {
    private final String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoai;
    private String diaChi;

    public NhaCungCap(String string, String string2, String string3, String string4) {
        if (string == null || !string.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma nha cung cap khong hop le.");
        }
        this.maNhaCungCap = string.trim();
        this.capNhatThongTin(string2, string3, string4);
    }

    public String getMaNhaCungCap() {
        return this.maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return this.tenNhaCungCap;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public String getDiaChi() {
        return this.diaChi;
    }

    public void capNhatThongTin(String string, String string2, String string3) {
        if (string == null || string.isBlank() || string3 == null || string3.isBlank() || string2 == null || !string2.trim().matches("\\+?[0-9]{9,15}")) {
            throw new IllegalArgumentException("Thong tin nha cung cap khong hop le.");
        }
        this.tenNhaCungCap = string.trim();
        this.soDienThoai = string2.trim();
        this.diaChi = string3.trim();
    }

    public String hienThiThongTin() {
        return "Ma nha cung cap: " + this.maNhaCungCap + "\nTen nha cung cap: " + this.tenNhaCungCap + "\nSo dien thoai: " + this.soDienThoai + "\nDia chi: " + this.diaChi;
    }
}

