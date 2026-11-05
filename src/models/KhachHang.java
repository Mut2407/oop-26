/*
 * Decompiled with CFR 0.152.
 */
package models;

public class KhachHang {
    private String maKhachHang;
    private String hoTen;
    private String diaChi;
    private String soDienThoai;

    public KhachHang(String string, String string2, String string3, String string4) {
        this.maKhachHang = string;
        this.hoTen = string2;
        this.soDienThoai = string3;
        this.diaChi = string4;
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

    public void setMaKhachHang(String string) {
        this.maKhachHang = string;
    }

    public void setHoTen(String string) {
        this.hoTen = string;
    }

    public void setDiaChi(String string) {
        this.diaChi = string;
    }

    public void setSoDienThoai(String string) {
        this.soDienThoai = string;
    }

    public String thongTinKhachHang() {
        return "Ma khach hang: " + this.maKhachHang + "\nHo ten: " + this.hoTen + "\nSo dien thoai: " + this.soDienThoai + "\nDia chi: " + this.diaChi;
    }
}

