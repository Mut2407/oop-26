package services;

import java.util.Arrays;
import java.util.Locale;
import models.NhanVien;
import models.SanPham;

public class DanhSachNhanVien implements IQuanLyDanhSach<NhanVien> {
    private NhanVien[] ds;
    private int soLuong;

    public DanhSachNhanVien() {
        this(SanPham.SUC_CHUA_BAN_DAU);
    }

    public DanhSachNhanVien(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new NhanVien[sucChua];
        soLuong = 0;
    }

    public DanhSachNhanVien(NhanVien[] banDau) {
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
    public NhanVien[] getTatCa() {
        return Arrays.copyOf(ds, soLuong);
    }

    @Override
    public boolean them(NhanVien doiTuong) {
        if (doiTuong == null) {
            return false;
        }
        doiTuong.kiemTraHopLe();
        if (!kiemTraMaDuyNhat(doiTuong.getMaNhanVien())) {
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
    public boolean sua(String ma, NhanVien moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null) {
            return false;
        }
        moi.kiemTraHopLe();
        if (!ds[viTri].getMaNhanVien().equals(moi.getMaNhanVien())) {
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
    public NhanVien timTheoMa(String ma) {
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
            if (ds[i].getMaNhanVien().equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public NhanVien[] timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        NhanVien[] ketQua = new NhanVien[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaNhanVien().toLowerCase(Locale.ROOT).contains(khoa)
                    || ds[i].getHoTen().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua[soKetQua++] = ds[i];
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }
}
