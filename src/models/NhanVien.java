/*
 * Decompiled with CFR 0.152.
 */
package models;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private String caLamViec;
    private String soDienThoai;
    private String diaChi;

    public NhanVien(String string, String string2, String string3, String string4, String string5) {
        this.maNhanVien = string;
        this.hoTen = string2;
        this.caLamViec = string3;
        this.soDienThoai = string4;
        this.diaChi = string5;
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

    public String getDiaChi() {
        return this.diaChi;
    }

    public void setMaNhanVien(String string) {
        this.maNhanVien = string;
    }

    public void setHoTen(String string) {
        this.hoTen = string;
    }

    public void setCaLamViec(String string) {
        this.caLamViec = string;
    }

    public void setSoDienThoai(String string) {
        this.soDienThoai = string;
    }

    public String thongTinNhanVien() {
        return "Ma Nhan Vien: " + this.maNhanVien + "\nHo ten Nhan Vien: " + this.hoTen + "\nSo dien thoai: " + this.soDienThoai + "\nDia chi: " + this.diaChi;
    }
}

