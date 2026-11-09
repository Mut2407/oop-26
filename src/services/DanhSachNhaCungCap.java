package services;

import java.util.Arrays;
import java.util.Locale;
import models.NhaCungCap;
import models.SanPham;

public class DanhSachNhaCungCap implements IQuanLyDanhSach<NhaCungCap> {
    private NhaCungCap[] ds;
    private int soLuong;

    public DanhSachNhaCungCap() {
        this(SanPham.SUC_CHUA_BAN_DAU);
    }

    public DanhSachNhaCungCap(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new NhaCungCap[sucChua];
        soLuong = 0;
    }

    public DanhSachNhaCungCap(NhaCungCap[] banDau) {
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
    public NhaCungCap[] getTatCa() {
        return Arrays.copyOf(ds, soLuong);
    }

    @Override
    public boolean them(NhaCungCap doiTuong) {
        if (doiTuong == null) {
            return false;
        }
        doiTuong.kiemTraHopLe();
        if (!kiemTraMaDuyNhat(doiTuong.getMaNhaCungCap())) {
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
    public boolean sua(String ma, NhaCungCap moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null) {
            return false;
        }
        moi.kiemTraHopLe();
        if (!ds[viTri].getMaNhaCungCap().equals(moi.getMaNhaCungCap())) {
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
    public NhaCungCap timTheoMa(String ma) {
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
            if (ds[i].getMaNhaCungCap().equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public NhaCungCap[] timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        NhaCungCap[] ketQua = new NhaCungCap[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaNhaCungCap().toLowerCase(Locale.ROOT).contains(khoa)
                    || ds[i].getTenNhaCungCap().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua[soKetQua++] = ds[i];
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }
}
