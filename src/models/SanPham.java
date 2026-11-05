/*
 * Decompiled with CFR 0.152.
 */
package models;

import java.math.BigDecimal;
import models.DanhMuc;
import models.DuLieu;

public abstract class SanPham {
    private final String maSanPham;
    private String tenSanPham;
    private String thuongHieu;
    private BigDecimal giaBan;
    private int soLuongTon;
    private String mauSac;
    private String chatLieu;
    private final DanhMuc danhMuc;

    public SanPham(String string, String string2, String string3, BigDecimal bigDecimal, int n, String string4, String string5, DanhMuc danhMuc) {
        if (string == null || !string.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma san pham khong hop le.");
        }
        if (danhMuc == null || DuLieu.timDanhMuc(danhMuc.getMaDanhMuc()) != danhMuc) {
            throw new IllegalArgumentException("Danh muc khong ton tai.");
        }
        if (n < 0) {
            throw new IllegalArgumentException("Ton kho khong duoc am.");
        }
        this.maSanPham = string.trim();
        this.danhMuc = danhMuc;
        this.soLuongTon = n;
        this.capNhatThongTin(string2, string3, string4, string5);
        this.capNhatGiaBan(bigDecimal);
    }

    public String getMaSanPham() {
        return this.maSanPham;
    }

    public String getTenSanPham() {
        return this.tenSanPham;
    }

    public String getThuongHieu() {
        return this.thuongHieu;
    }

    public BigDecimal getGiaBan() {
        return this.giaBan;
    }

    public int getSoLuongTon() {
        return this.soLuongTon;
    }

    public String getMauSac() {
        return this.mauSac;
    }

    public String getChatLieu() {
        return this.chatLieu;
    }

    public DanhMuc getDanhMuc() {
        return this.danhMuc;
    }

    public void capNhatThongTin(String string, String string2, String string3, String string4) {
        if (string == null || string.isBlank() || string2 == null || string2.isBlank()) {
            throw new IllegalArgumentException("Ten va thuong hieu khong duoc rong.");
        }
        this.tenSanPham = string.trim();
        this.thuongHieu = string2.trim();
        this.mauSac = string3 == null || string3.isBlank() ? null : string3.trim();
        this.chatLieu = string4 == null || string4.isBlank() ? null : string4.trim();
    }

    public void capNhatGiaBan(BigDecimal bigDecimal) {
        if (bigDecimal == null || bigDecimal.signum() < 0) {
            throw new IllegalArgumentException("Gia ban khong hop le.");
        }
        this.giaBan = bigDecimal;
    }

    public boolean conDuHang(int n) {
        return n > 0 && this.soLuongTon >= n;
    }

    public void tangTon(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("So luong phai duong.");
        }
        try {
            this.soLuongTon = Math.addExact(this.soLuongTon, n);
        }
        catch (ArithmeticException arithmeticException) {
            throw new IllegalArgumentException("So luong ton vuot gioi han.", arithmeticException);
        }
    }

    public void giamTon(int n) {
        if (n <= 0 || n > this.soLuongTon) {
            throw new IllegalArgumentException("So luong giam khong hop le.");
        }
        this.soLuongTon -= n;
    }

    public BigDecimal tinhGiaTriTonTheoGiaBan() {
        return this.giaBan.multiply(BigDecimal.valueOf(this.soLuongTon));
    }

    public abstract String layLoaiSanPham();

    public abstract String hienThiThongTin();

    public String toString() {
        return "Ma san pham: " + this.maSanPham + "\nTen san pham: " + this.tenSanPham + "\nGia ban: " + String.valueOf(this.giaBan) + "\nThuong hieu: " + this.thuongHieu + "\nSo luong ton: " + this.soLuongTon + "\nDanh muc: " + this.danhMuc.getTenDanhMuc() + "\nMau sac: " + (this.mauSac == null ? "" : this.mauSac) + "\nChat lieu: " + (this.chatLieu == null ? "" : this.chatLieu);
    }
}

