package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

public class PhieuNhap {
    private final String maPhieuNhap;
    private LocalDate ngayLap;
    private NhaCungCap nhaCungCap;
    private NhanVien nhanVien;
    private ChiTietPhieuNhap[] chiTiet;
    private int soLuongChiTiet;

    public PhieuNhap() {
        maPhieuNhap = "";
        ngayLap = LocalDate.now();
        chiTiet = new ChiTietPhieuNhap[SanPham.SUC_CHUA_BAN_DAU];
    }

    public PhieuNhap(String ma, LocalDate ngay, NhaCungCap doiTac, NhanVien nhanVien) {
        if (ma == null || !ma.trim().matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("Ma chung tu khong hop le.");
        }
        maPhieuNhap = ma.trim().toUpperCase(Locale.ROOT);
        setNgayLap(ngay);
        setNhaCungCap(doiTac);
        setNhanVien(nhanVien);
        chiTiet = new ChiTietPhieuNhap[SanPham.SUC_CHUA_BAN_DAU];
    }

    // Sao chep tung dong de sua ban moi khong lam mat du lieu ban cu.
    public PhieuNhap(PhieuNhap cu) {
        this(cu.maPhieuNhap, cu.ngayLap, cu.nhaCungCap, cu.nhanVien);
        for (int i = 0; i < cu.soLuongChiTiet; i++) {
            themChiTiet(cu.chiTiet[i]);
        }
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
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

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap doiTac) {
        if (doiTac == null) {
            throw new IllegalArgumentException("NhaCungCap khong duoc null.");
        }
        doiTac.kiemTraHopLe();
        nhaCungCap = doiTac;
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

    public ChiTietPhieuNhap[] getChiTiet() {
        ChiTietPhieuNhap[] ketQua = new ChiTietPhieuNhap[soLuongChiTiet];
        for (int i = 0; i < soLuongChiTiet; i++) {
            ketQua[i] = new ChiTietPhieuNhap(chiTiet[i]);
        }
        return ketQua;
    }

    public void themChiTiet(ChiTietPhieuNhap moi) {
        if (moi == null) {
            throw new IllegalArgumentException("Chi tiet khong duoc null.");
        }
        ChiTietPhieuNhap hopLe = new ChiTietPhieuNhap(moi);
        if (timViTri(hopLe.getSanPham().getMaSanPham()) >= 0) {
            throw new IllegalArgumentException("San pham da co trong chung tu.");
        }
        if (soLuongChiTiet == chiTiet.length) {
            chiTiet = Arrays.copyOf(chiTiet, chiTiet.length + SanPham.SUC_CHUA_BAN_DAU);
        }
        chiTiet[soLuongChiTiet++] = hopLe;
    }

    public boolean suaChiTiet(String maSP, ChiTietPhieuNhap moi) {
        int viTri = timViTri(maSP);
        if (viTri < 0) {
            return false;
        }
        if (moi == null) {
            throw new IllegalArgumentException("Chi tiet khong duoc null.");
        }
        ChiTietPhieuNhap hopLe = new ChiTietPhieuNhap(moi);
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

    public ChiTietPhieuNhap timChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        if (viTri < 0) {
            return null;
        }
        return new ChiTietPhieuNhap(chiTiet[viTri]);
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
        new PhieuNhap(this);
    }

    public String hienThiThongTin() {
        return maPhieuNhap + " | " + ngayLap + " | "
                + (nhaCungCap == null ? "" : nhaCungCap.getMaNhaCungCap()) + " | "
                + (nhanVien == null ? "" : nhanVien.getMaNhanVien())
                + " | Tong: " + tinhTongTien().toPlainString();
    }
}
