package services;

import java.util.Arrays;
import java.util.Locale;
import models.SanPham;

public class DanhSachSanPham implements IQuanLyDanhSach<SanPham> {
    private SanPham[] ds;
    private int soLuong;

    public DanhSachSanPham() {
        this(SanPham.SUC_CHUA_BAN_DAU);
    }

    public DanhSachSanPham(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new SanPham[sucChua];
        soLuong = 0;
    }

    public DanhSachSanPham(SanPham[] banDau) {
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
    public SanPham[] getTatCa() {
        return Arrays.copyOf(ds, soLuong);
    }

    @Override
    public boolean them(SanPham doiTuong) {
        if (doiTuong == null) {
            return false;
        }
        doiTuong.kiemTraHopLe();
        if (!kiemTraMaDuyNhat(doiTuong.getMaSanPham())) {
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
    public boolean sua(String ma, SanPham moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null) {
            return false;
        }
        moi.kiemTraHopLe();
        if (!ds[viTri].getMaSanPham().equals(moi.getMaSanPham())) {
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
    public SanPham timTheoMa(String ma) {
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
            if (ds[i].getMaSanPham().equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public SanPham[] timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        SanPham[] ketQua = new SanPham[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaSanPham().toLowerCase(Locale.ROOT).contains(khoa)
                    || ds[i].getTenSanPham().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua[soKetQua++] = ds[i];
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }

    public SanPham[] locTheoLoaiSP(String maLoai) {
        SanPham[] ketQua = new SanPham[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getLoaiSP().getMaLoaiSP().equalsIgnoreCase(maLoai.trim())) {
                ketQua[soKetQua++] = ds[i];
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }

    public SanPham[] locTheoKhoangGia(java.math.BigDecimal tu, java.math.BigDecimal den) {
        if (tu == null || den == null || tu.signum() < 0 || tu.compareTo(den) > 0) {
            throw new IllegalArgumentException("Khoang gia khong hop le.");
        }
        SanPham[] ketQua = new SanPham[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getGiaBan().compareTo(tu) >= 0 && ds[i].getGiaBan().compareTo(den) <= 0) {
                ketQua[soKetQua++] = ds[i];
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }

    public void hienThiDanhSach() {
        for (int i = 0; i < soLuong; i++) {
            // Goi ham qua SanPham: Java chay phien ban cua lop con.
            System.out.println(ds[i].hienThiThongTin());
        }
    }
}
