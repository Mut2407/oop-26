/*
 * Decompiled with CFR 0.152.
 */
package models;

import java.math.BigDecimal;
// import models.DanhMuc;
// import models.SanPham;

public class GiayTheThao
extends SanPham {
    private String kichCoGiay;
    private final String loaiDeGiay;

    public GiayTheThao(String string, String string2, String string3, BigDecimal bigDecimal, int n, String string4, String string5, DanhMuc danhMuc, String string6, String string7) {
        super(string, string2, string3, bigDecimal, n, string4, string5, danhMuc);
        this.capNhatKichCo(string6);
        if (string7 == null || string7.isBlank()) {
            throw new IllegalArgumentException("Loai de giay khong duoc rong.");
        }
        this.loaiDeGiay = string7.trim();
    }

    public String getKichCoGiay() {
        return this.kichCoGiay;
    }

    public String getLoaiDeGiay() {
        return this.loaiDeGiay;
    }

    public void capNhatKichCo(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Kich co giay khong duoc rong.");
        }
        this.kichCoGiay = string.trim();
    }

    @Override
    public String layLoaiSanPham() {
        return "GIAY";
    }

    @Override
    public String hienThiThongTin() {
        return super.toString() + "\nKich co giay: " + this.kichCoGiay + "\nLoai de giay: " + this.loaiDeGiay;
    }
}

