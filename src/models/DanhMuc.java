/*
 * Decompiled with CFR 0.152.
 */
package models;

public class DanhMuc {
    private final String maDanhMuc;
    private String tenDanhMuc;
    private String moTa;

    public DanhMuc(String string, String string2, String string3) {
        if (string == null || !string.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma danh muc khong hop le.");
        }
        this.maDanhMuc = string.trim();
        this.capNhatThongTin(string2, string3);
    }

    public String getMaDanhMuc() {
        return this.maDanhMuc;
    }

    public String getTenDanhMuc() {
        return this.tenDanhMuc;
    }

    public String getMoTa() {
        return this.moTa;
    }

    public void capNhatThongTin(String string, String string2) {
        if (string == null || string.isBlank() || string2 == null) {
            throw new IllegalArgumentException("Thong tin danh muc khong hop le.");
        }
        this.tenDanhMuc = string.trim();
        this.moTa = string2.trim();
    }

    public String hienThiThongTin() {
        return "Ma danh muc: " + this.maDanhMuc + "\nTen danh muc: " + this.tenDanhMuc + "\nMo ta: " + this.moTa;
    }
}

