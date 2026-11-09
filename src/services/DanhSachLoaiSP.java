package services;

import java.util.Arrays;
import java.util.Locale;
import models.LoaiSP;
import models.SanPham;

public class DanhSachLoaiSP implements IQuanLyDanhSach<LoaiSP> {
    private LoaiSP[] ds;
    private int soLuong;

    public DanhSachLoaiSP() {
        this(SanPham.SUC_CHUA_BAN_DAU);
    }

    public DanhSachLoaiSP(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new LoaiSP[sucChua];
        soLuong = 0;
    }

    public DanhSachLoaiSP(LoaiSP[] banDau) {
        this(banDau.length);
        for (int i = 0; i < banDau.length; i++) {
            if (!them(banDau[i])) {
                throw new IllegalArgumentException("Ma trung trong mang ban dau.");
            }
        }
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public LoaiSP[] getTatCa() {
        return Arrays.copyOf(ds, soLuong);
    }

    @Override
    public boolean them(LoaiSP doiTuong) {
        if (doiTuong == null) {
            return false;
        }
        doiTuong.kiemTraHopLe();
        if (!kiemTraMaDuyNhat(doiTuong.getMaLoaiSP())) {
            return false;
        }
        moRongMang();
        ds[soLuong] = doiTuong;
        soLuong++;
        return true;
    }

    private boolean kiemTraMaDuyNhat(String ma) {
        return timViTri(ma) == -1;
    }

    private void moRongMang() {
        if (soLuong == ds.length) {
            ds = Arrays.copyOf(ds, ds.length + SanPham.SUC_CHUA_BAN_DAU);
        }
    }

    @Override
    public boolean sua(String ma, LoaiSP moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null) {
            return false;
        }
        moi.kiemTraHopLe();
        if (!ds[viTri].getMaLoaiSP().equals(moi.getMaLoaiSP())) {
            return false;
        }
        ds[viTri].capNhatThongTin(moi);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return false;
        }
        for (int i = viTri; i < soLuong - 1; i++) {
            ds[i] = ds[i + 1];
        }
        ds[--soLuong] = null;
        return true;
    }

    @Override
    public LoaiSP timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return null;
        }
        return ds[viTri];
    }

    private int timViTri(String ma) {
        if (ma == null) {
            return -1;
        }
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaLoaiSP().equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public LoaiSP[] timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        LoaiSP[] ketQua = new LoaiSP[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaLoaiSP().toLowerCase(Locale.ROOT).contains(khoa)
                    || ds[i].getTenLoaiSP().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua[soKetQua++] = ds[i];
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }
}
