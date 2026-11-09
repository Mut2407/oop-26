/*
 * Decompiled with CFR 0.152.
 */
package models;

import java.math.BigDecimal;
//import models.DanhMuc;
//import models.SanPham;

public class DungCuTheThao
extends SanPham {
    private double khoiLuongKg;

    public DungCuTheThao(String string, String string2, String string3, BigDecimal bigDecimal, int n, String string4, String string5, DanhMuc danhMuc, double d) {
        super(string, string2, string3, bigDecimal, n, string4, string5, danhMuc);
        this.capNhatKhoiLuong(d);
    }

    public double getKhoiLuongKg() {
        return this.khoiLuongKg;
    }

    public void capNhatKhoiLuong(double d) {
        if (!Double.isFinite(d) || d < 0.0) {
            throw new IllegalArgumentException("Khoi luong khong hop le.");
        }
        this.khoiLuongKg = d;
    }

    @Override
    public String layLoaiSanPham() {
        return "DUNG_CU";
    }

    @Override
    public String hienThiThongTin() {
        return super.toString() + "\nKhoi luong: " + this.khoiLuongKg + " kg";
    }
}

