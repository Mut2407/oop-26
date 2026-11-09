package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class HoaDon {
    private String maHoaDon;
    private LocalDate ngayLap;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private ChiTietHoaDon[] chiTiet;
    private int soLuongChiTiet;

    public HoaDon() {
        maHoaDon = "";
        ngayLap = LocalDate.now();
        chiTiet = new ChiTietHoaDon[5];
    }

    public HoaDon(String maHoaDon, LocalDate ngayLap, KhachHang khachHang, NhanVien nhanVien) {
        this();
        kiemTraChuoi(maHoaDon);
        this.maHoaDon = maHoaDon.trim();
        setNgayLap(ngayLap);
        setKhachHang(khachHang);
        setNhanVien(nhanVien);
    }

    public HoaDon(HoaDon cu) {
        this(cu.maHoaDon, cu.ngayLap, cu.khachHang, cu.nhanVien);
        for (int i = 0; i < cu.soLuongChiTiet; i++)
            themChiTiet(cu.chiTiet[i]);
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        if (ngayLap == null)
            throw new IllegalArgumentException("Ngay/doi tac/nhan vien khong duoc rong.");
        this.ngayLap = ngayLap;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        if (khachHang == null)
            throw new IllegalArgumentException("Ngay/doi tac/nhan vien khong duoc rong.");
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null)
            throw new IllegalArgumentException("Ngay/doi tac/nhan vien khong duoc rong.");
        this.nhanVien = nhanVien;
    }

    public int getSoLuongChiTiet() {
        return soLuongChiTiet;
    }

    public ChiTietHoaDon[] getChiTiet() {
        ChiTietHoaDon[] kq = new ChiTietHoaDon[soLuongChiTiet];
        for (int i = 0; i < soLuongChiTiet; i++)
            kq[i] = new ChiTietHoaDon(chiTiet[i]);
        return kq;
    }

    private int timViTri(String maSP) {
        if (maSP == null)
            return -1;
        for (int i = 0; i < soLuongChiTiet; i++)
            if (chiTiet[i].getSanPham().getMaSanPham().equalsIgnoreCase(maSP.trim()))
                return i;
        return -1;
    }

    public ChiTietHoaDon timChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        if (viTri < 0)
            return null;
        return new ChiTietHoaDon(chiTiet[viTri]);
    }

    public boolean themChiTiet(ChiTietHoaDon ct) {
        if (ct == null || !ct.kiemTraHopLe())
            return false;
        if (timViTri(ct.getSanPham().getMaSanPham()) >= 0)
            return false;
        if (soLuongChiTiet == chiTiet.length)
            chiTiet = Arrays.copyOf(chiTiet, chiTiet.length + 5);
        chiTiet[soLuongChiTiet++] = new ChiTietHoaDon(ct);
        return true;
    }

    public boolean suaChiTiet(String maSP, ChiTietHoaDon moi) {
        int viTri = timViTri(maSP);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!maSP.trim().equalsIgnoreCase(moi.getSanPham().getMaSanPham()))
            return false;
        chiTiet[viTri] = new ChiTietHoaDon(moi);
        return true;
    }

    public boolean xoaChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        if (viTri < 0)
            return false;
        for (int i = viTri; i < soLuongChiTiet - 1; i++)
            chiTiet[i] = chiTiet[i + 1];
        chiTiet[--soLuongChiTiet] = null;
        return true;
    }

    public boolean kiemTraHopLe() {
        return !maHoaDon.isEmpty() && ngayLap != null && khachHang != null
                && khachHang.kiemTraHopLe() && nhanVien != null
                && nhanVien.kiemTraHopLe() && soLuongChiTiet > 0;
    }

    public BigDecimal tinhTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuongChiTiet; i++)
            tong = tong.add(chiTiet[i].tinhThanhTien());
        return tong;
    }

    public String hienThiThongTin() {
        return maHoaDon + " | " + ngayLap + " | " + khachHang.getMaKhachHang()
                + " | " + nhanVien.getMaNhanVien() + " | Tong: " + tinhTongTien().toPlainString();
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
