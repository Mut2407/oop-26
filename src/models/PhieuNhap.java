package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class PhieuNhap {
    private String maPhieuNhap;
    private LocalDate ngayLap;
    private NhaCungCap nhaCungCap;
    private NhanVien nhanVien;
    private ChiTietPhieuNhap[] chiTiet;
    private int soLuongChiTiet;

    public PhieuNhap() {
        maPhieuNhap = "";
        ngayLap = LocalDate.now();
        chiTiet = new ChiTietPhieuNhap[5];
    }

    public PhieuNhap(String maPhieuNhap, LocalDate ngayLap, NhaCungCap nhaCungCap, NhanVien nhanVien) {
        this();
        kiemTraChuoi(maPhieuNhap);
        this.maPhieuNhap = maPhieuNhap.trim();
        setNgayLap(ngayLap);
        setNhaCungCap(nhaCungCap);
        setNhanVien(nhanVien);
    }

    public PhieuNhap(PhieuNhap cu) {
        this(cu.maPhieuNhap, cu.ngayLap, cu.nhaCungCap, cu.nhanVien);
        for (int i = 0; i < cu.soLuongChiTiet; i++)
            themChiTiet(cu.chiTiet[i]);
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        if (ngayLap == null)
            throw new IllegalArgumentException("Ngay/doi tac/nhan vien khong duoc rong.");
        this.ngayLap = ngayLap;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        if (nhaCungCap == null)
            throw new IllegalArgumentException("Ngay/doi tac/nhan vien khong duoc rong.");
        this.nhaCungCap = nhaCungCap;
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

    public ChiTietPhieuNhap[] getChiTiet() {
        ChiTietPhieuNhap[] kq = new ChiTietPhieuNhap[soLuongChiTiet];
        for (int i = 0; i < soLuongChiTiet; i++)
            kq[i] = new ChiTietPhieuNhap(chiTiet[i]);
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

    public ChiTietPhieuNhap timChiTiet(String maSP) {
        int viTri = timViTri(maSP);
        if (viTri < 0)
            return null;
        return new ChiTietPhieuNhap(chiTiet[viTri]);
    }

    public boolean themChiTiet(ChiTietPhieuNhap ct) {
        if (ct == null || !ct.kiemTraHopLe())
            return false;
        if (timViTri(ct.getSanPham().getMaSanPham()) >= 0)
            return false;
        if (soLuongChiTiet == chiTiet.length)
            chiTiet = Arrays.copyOf(chiTiet, chiTiet.length + 5);
        chiTiet[soLuongChiTiet++] = new ChiTietPhieuNhap(ct);
        return true;
    }

    public boolean suaChiTiet(String maSP, ChiTietPhieuNhap moi) {
        int viTri = timViTri(maSP);
        if (viTri < 0 || moi == null || !moi.kiemTraHopLe())
            return false;
        if (!maSP.trim().equalsIgnoreCase(moi.getSanPham().getMaSanPham()))
            return false;
        chiTiet[viTri] = new ChiTietPhieuNhap(moi);
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
        return !maPhieuNhap.isEmpty() && ngayLap != null && nhaCungCap != null
                && nhaCungCap.kiemTraHopLe() && nhanVien != null
                && nhanVien.kiemTraHopLe() && soLuongChiTiet > 0;
    }

    public BigDecimal tinhTongTien() {
        BigDecimal tong = BigDecimal.ZERO;
        for (int i = 0; i < soLuongChiTiet; i++)
            tong = tong.add(chiTiet[i].tinhThanhTien());
        return tong;
    }

    public String hienThiThongTin() {
        return maPhieuNhap + " | " + ngayLap + " | " + nhaCungCap.getMaNhaCungCap()
                + " | " + nhanVien.getMaNhanVien() + " | Tong: " + tinhTongTien().toPlainString();
    }

    private static void kiemTraChuoi(String giaTri) {
        if (giaTri == null || giaTri.trim().isEmpty())
            throw new IllegalArgumentException("Thong tin khong duoc rong.");
        if (giaTri.contains("|") || giaTri.contains("\n") || giaTri.contains("\r"))
            throw new IllegalArgumentException("Khong nhap dau | hoac xuong dong.");
    }

}
