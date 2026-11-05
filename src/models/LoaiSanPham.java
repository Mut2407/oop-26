/*
 * Decompiled with CFR 0.152.
 */
package models;

import models.DanhMuc;

@Deprecated
public class LoaiSanPham
extends DanhMuc {
    public LoaiSanPham(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public String getMaLoai() {
        return this.getMaDanhMuc();
    }

    public void setMaLoai(String string) {
        if (!this.getMaDanhMuc().equals(string)) {
            throw new IllegalArgumentException("Khong duoc doi ma danh muc.");
        }
    }

    public String getTenLoai() {
        return this.getTenDanhMuc();
    }

    public void setTenLoai(String string) {
        this.capNhatThongTin(string, this.getMoTa());
    }

    public void setMoTa(String string) {
        this.capNhatThongTin(this.getTenDanhMuc(), string);
    }
}

