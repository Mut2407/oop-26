/*
 * Decompiled with CFR 0.152.
 */
package models;

import java.math.BigDecimal;
import models.DanhMuc;
import models.SanPham;

public class QuanAoTheThao
extends SanPham {
    private String kichCoQuanAo;

    public QuanAoTheThao(String string, String string2, String string3, BigDecimal bigDecimal, int n, String string4, String string5, DanhMuc danhMuc, String string6) {
        super(string, string2, string3, bigDecimal, n, string4, string5, danhMuc);
        this.capNhatKichCo(string6);
    }

    public String getKichCoQuanAo() {
        return this.kichCoQuanAo;
    }

    public void capNhatKichCo(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Kich co quan ao khong duoc rong.");
        }
        this.kichCoQuanAo = string.trim();
    }

    @Override
    public String layLoaiSanPham() {
        return "QUAN_AO";
    }

    @Override
    public String hienThiThongTin() {
        return super.toString() + "\nKich co quan ao: " + this.kichCoQuanAo;
    }
}

