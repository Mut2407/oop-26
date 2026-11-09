package services;

import java.util.Arrays;
import java.util.Locale;
import models.PhieuNhap;
import models.SanPham;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DanhSachPhieuNhap implements IQuanLyDanhSach<PhieuNhap> {
    private PhieuNhap[] ds;
    private int soLuong;

    public DanhSachPhieuNhap() {
        this(SanPham.SUC_CHUA_BAN_DAU);
    }

    public DanhSachPhieuNhap(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new PhieuNhap[sucChua];
        soLuong = 0;
    }

    public DanhSachPhieuNhap(PhieuNhap[] banDau) {
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
    public PhieuNhap[] getTatCa() {
        PhieuNhap[] ketQua = new PhieuNhap[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = new PhieuNhap(ds[i]);
        }
        return ketQua;
    }

    @Override
    public boolean them(PhieuNhap doiTuong) {
        if (doiTuong == null) {
            return false;
        }
        doiTuong.kiemTraHopLe();
        if (!kiemTraMaDuyNhat(doiTuong.getMaPhieuNhap())) {
            return false;
        }
        moRongMang();
        ds[soLuong] = new PhieuNhap(doiTuong);
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
    public boolean sua(String ma, PhieuNhap moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null) {
            return false;
        }
        moi.kiemTraHopLe();
        if (!ds[viTri].getMaPhieuNhap().equals(moi.getMaPhieuNhap())) {
            return false;
        }
        ds[viTri] = new PhieuNhap(moi);
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
    public PhieuNhap timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return null;
        }
        return new PhieuNhap(ds[viTri]);
    }

    private int timViTri(String ma) {
        if (ma == null) {
            return -1;
        }
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaPhieuNhap().equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public PhieuNhap[] timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        PhieuNhap[] ketQua = new PhieuNhap[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaPhieuNhap().toLowerCase(Locale.ROOT).contains(khoa)
                    || ds[i].getNhaCungCap().getMaNhaCungCap().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua[soKetQua++] = new PhieuNhap(ds[i]);
            }
        }
        return Arrays.copyOf(ketQua, soKetQua);
    }

    public BigDecimal thongKeTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuong; i++) {
            tong = tong.add(ds[i].tinhTongTien());
        }
        return tong;
    }

    // Overload: cung ten ham, khac danh sach tham so (chuong 2).
    public BigDecimal thongKeTongTien(LocalDate tu, LocalDate den) {
        if (tu == null || den == null || tu.isAfter(den)) {
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        }
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuong; i++) {
            if (!ds[i].getNgayLap().isBefore(tu) && !ds[i].getNgayLap().isAfter(den)) {
                tong = tong.add(ds[i].tinhTongTien());
            }
        }
        return tong;
    }
}
