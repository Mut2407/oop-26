package services;

import java.util.Arrays;
import java.util.Locale;
import models.HoaDon;
import models.SanPham;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DanhSachHoaDon implements IQuanLyDanhSach<HoaDon> {
    private HoaDon[] ds;
    private int soLuong;

    public DanhSachHoaDon() {
        this(SanPham.SUC_CHUA_BAN_DAU);
    }

    public DanhSachHoaDon(int sucChua) {
        if (sucChua < 0) {
            throw new IllegalArgumentException("Suc chua phai >= 0.");
        }
        ds = new HoaDon[sucChua];
        soLuong = 0;
    }

    public DanhSachHoaDon(HoaDon[] banDau) {
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
    public HoaDon[] getTatCa() {
        HoaDon[] ketQua = new HoaDon[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ketQua[i] = new HoaDon(ds[i]);
        }
        return ketQua;
    }

    @Override
    public boolean them(HoaDon doiTuong) {
        if (doiTuong == null) {
            return false;
        }
        doiTuong.kiemTraHopLe();
        if (!kiemTraMaDuyNhat(doiTuong.getMaHoaDon())) {
            return false;
        }
        moRongMang();
        ds[soLuong] = new HoaDon(doiTuong);
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
    public boolean sua(String ma, HoaDon moi) {
        int viTri = timViTri(ma);
        if (viTri < 0 || moi == null) {
            return false;
        }
        moi.kiemTraHopLe();
        if (!ds[viTri].getMaHoaDon().equals(moi.getMaHoaDon())) {
            return false;
        }
        ds[viTri] = new HoaDon(moi);
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
    public HoaDon timTheoMa(String ma) {
        int viTri = timViTri(ma);
        if (viTri < 0) {
            return null;
        }
        return new HoaDon(ds[viTri]);
    }

    private int timViTri(String ma) {
        if (ma == null) {
            return -1;
        }
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaHoaDon().equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public HoaDon[] timKiem(String tuKhoa) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase(Locale.ROOT);
        HoaDon[] ketQua = new HoaDon[soLuong];
        int soKetQua = 0;
        for (int i = 0; i < soLuong; i++) {
            if (ds[i].getMaHoaDon().toLowerCase(Locale.ROOT).contains(khoa)
                    || ds[i].getKhachHang().getMaKhachHang().toLowerCase(Locale.ROOT).contains(khoa)) {
                ketQua[soKetQua++] = new HoaDon(ds[i]);
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
