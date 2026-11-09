package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

public class HoaDon {
    private final String maHoaDon;
    private LocalDate ngayLap;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private ChiTietHoaDon[] chiTiet;
    private int soLuongChiTiet;

    public HoaDon() {
        maHoaDon = "";
        ngayLap = LocalDate.now();
        chiTiet = new ChiTietHoaDon[SanPham.SUC_CHUA_BAN_DAU];
    }

    public HoaDon(String ma, LocalDate ngay, KhachHang doiTac, NhanVien nhanVien) {
        if (ma == null || !ma.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma chung tu khong hop le.");
        }
        maHoaDon = ma.trim().toUpperCase(Locale.ROOT);
        setNgayLap(ngay);
        setKhachHang(doiTac);
        setNhanVien(nhanVien);
        chiTiet = new ChiTietHoaDon[SanPham.SUC_CHUA_BAN_DAU];
    }

    // Sao chep tung dong de sua ban moi khong lam mat du lieu ban cu.
    public HoaDon(HoaDon cu) {
        this(cu.maHoaDon, cu.ngayLap, cu.khachHang, cu.nhanVien);
        for (int i = 0; i < cu.soLuongChiTiet; i++) {
            themChiTiet(cu.chiTiet[i]);
        }
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngay) {
        if (ngay == null) {
            throw new IllegalArgumentException("Ngay lap khong duoc null.");
        }
        ngayLap = ngay;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang doiTac) {
        if (doiTac == null) {
            throw new IllegalArgumentException("KhachHang khong duoc null.");
        }
        doiTac.kiemTraHopLe();
        khachHang = doiTac;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("Nhan vien khong duoc null.");
        }
        nhanVien.kiemTraHopLe();
        this.nhanVien = nhanVien;
    }

    public int getSoLuongChiTiet() {
        return soLuongChiTiet;
    }

    public ChiTietHoaDon[] getChiTiet() {
        ChiTietHoaDon[] ketQua = new ChiTietHoaDon[soLuongChiTiet];
        for (int i = 0; i < soLuongChiTiet; i++) {
            ketQua[i] = new ChiTietHoaDon(chiTiet[i]);
        }
        return ketQua;
    }

    public void themChiTiet(ChiTietHoaDon moi) {
        if (moi == null) {
            throw new IllegalArgumentException("Chi tiet khong duoc null.");
        }
        ChiTietHoaDon hopLe = new ChiTietHoaDon(moi);
        if (timViTri(hopLe.getSanPham().getMaSanPham()) >= 0) {
            throw new IllegalArgumentException("San pham da co trong chung tu.");
        }
        if (soLuongChiTiet == chiTiet.length) {
            chiTiet = Arrays.copyOf(chiTiet, chiTiet.length + SanPham.SUC_CHUA_BAN_DAU);
        }
        chiTiet[soLuongChiTiet++] = hopLe;
    }

    public boolean suaChiTiet(String maSP, ChiTietHoaDon moi) {
        int viTri = timViTri(maSP);
        if (viTri < 0) {
            return false;
        }
        if (moi == null) {
            throw new IllegalArgumentException("Chi tiet khong duoc null.");
        }
        ChiTietHoaDon hopLe = new ChiTietHoaDon(moi);
        if (!maSP.trim().equalsIgnoreCase(hopLe.getSanPham().getMaSanPham())) {
            throw new IllegalArgumentException("Khong duoc doi ma san pham cua dong.");
        }
        chiTiet[viTri] = hopLe;
        return true;
    }

    public boolean xoaChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        if (viTri < 0) {
            return false;
        }
        for (int i = viTri; i < soLuongChiTiet - 1; i++) {
            chiTiet[i] = chiTiet[i + 1];
        }
        chiTiet[--soLuongChiTiet] = null;
        return true;
    }

    public ChiTietHoaDon timChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        if (viTri < 0) {
            return null;
        }
        return new ChiTietHoaDon(chiTiet[viTri]);
    }

    private int timViTri(String maSP) {
        if (maSP == null) {
            return -1;
        }
        for (int i = 0; i < soLuongChiTiet; i++) {
            if (chiTiet[i].getSanPham().getMaSanPham().equalsIgnoreCase(maSP.trim())) {
                return i;
            }
        }
        return -1;
    }

    public BigDecimal tinhTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuongChiTiet; i++) {
            tong = tong.add(chiTiet[i].tinhThanhTien());
        }
        return tong;
    }

    public void kiemTraHopLe() {
        new HoaDon(this);
    }

    public String hienThiThongTin() {
        return maHoaDon + " | " + ngayLap + " | "
                + (khachHang == null ? "" : khachHang.getMaKhachHang()) + " | "
                + (nhanVien == null ? "" : nhanVien.getMaNhanVien())
                + " | Tong: " + tinhTongTien().toPlainString();
    }
}
