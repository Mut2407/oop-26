package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import services.DanhSachKhachHang;

public final class PhieuNhap {
    private final String maPhieuNhap;
    private final LocalDate ngayNhap;
    private final NhaCungCap nhaCungCap;
    private final NhanVien nhanVien;
    private final List<ChiTietPhieuNhap> danhSachChiTiet = new ArrayList<>();
    private TrangThaiPhieuNhap trangThai = TrangThaiPhieuNhap.CHO_XAC_NHAN;
    private static int soThuTuTiepTheo = 1;

    public PhieuNhap(String ma, LocalDate ngay, NhaCungCap kh, NhanVien nv) {
        if (!DanhSachKhachHang.maHopLe(ma) || ngay == null || kh == null || nv == null) {
            throw new IllegalArgumentException("Ma/ngay/nha cung cap/nhan vien phieu nhap khong hop le.");
        }
        maPhieuNhap = ma.trim();
        ngayNhap = ngay;
        nhaCungCap = new NhaCungCap(kh.getMaNhaCungCap(), kh.getTenNhaCungCap(), kh.getSoDienThoai(), kh.getDiaChi());
        nhanVien = new NhanVien(nv.getMaNhanVien(), nv.getHoTen(), nv.getSoDienThoai(), nv.getCaLamViec());
        dongBoMa(maPhieuNhap);
    }
    public String getMaPhieuNhap() { return maPhieuNhap; }
    public LocalDate getNgayNhap() { return ngayNhap; }
    public TrangThaiPhieuNhap getTrangThai() { return trangThai; }
    public String getMaNhaCungCap() { return nhaCungCap.getMaNhaCungCap(); }
    public String getMaNhanVien() { return nhanVien.getMaNhanVien(); }
    public List<ChiTietPhieuNhap> getDanhSachChiTiet() { return List.copyOf(danhSachChiTiet); }
    public boolean coTheChinhSua() { return trangThai == TrangThaiPhieuNhap.CHO_XAC_NHAN; }

    private void kiemTraCho() {
        if (!coTheChinhSua()) throw new IllegalStateException("Phieu nhap da xac nhan, khong the chinh sua.");
    }
    public boolean themChiTiet(ChiTietPhieuNhap ct) {
        kiemTraCho();
        if (ct == null) throw new IllegalArgumentException("Chi tiet khong duoc null.");
        if (chiSo(ct.getSanPham().getMaSanPham()) >= 0) {
            throw new IllegalArgumentException("San pham da co trong phieu nhap.");
        }
        danhSachChiTiet.add(ct);
        return true;
    }
    public boolean xoaChiTiet(String maSP) {
        kiemTraCho();
        int i = chiSo(maSP);
        if (i < 0) return false;
        danhSachChiTiet.remove(i);
        return true;
    }
    public boolean suaChiTiet(String maSP, ChiTietPhieuNhap moi) {
        kiemTraCho();
        int i = chiSo(maSP);
        if (i < 0) return false;
        if (moi == null || !moi.getSanPham().getMaSanPham().equalsIgnoreCase(maSP.trim())) {
            throw new IllegalArgumentException("Phai giu nguyen ma san pham cua dong.");
        }
        danhSachChiTiet.set(i, moi);
        return true;
    }
    private int chiSo(String ma) {
        for (int i = 0; ma != null && i < danhSachChiTiet.size(); i++) {
            if (danhSachChiTiet.get(i).getSanPham().getMaSanPham().equalsIgnoreCase(ma.trim())) return i;
        }
        return -1;
    }
    public BigDecimal tinhTongTienNhap() {
        return danhSachChiTiet.stream().map(ChiTietPhieuNhap::thanhTienNhap).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    void danhDauDaNhapKho() {
        kiemTraCho();
        if (danhSachChiTiet.isEmpty()) throw new IllegalArgumentException("Phieu nhap rong khong the xac nhan.");
        trangThai = TrangThaiPhieuNhap.DA_NHAP_KHO;
    }
    public PhieuNhap banSao() {
        PhieuNhap copy = new PhieuNhap(maPhieuNhap, ngayNhap, nhaCungCap, nhanVien);
        copy.danhSachChiTiet.addAll(danhSachChiTiet);
        copy.trangThai = trangThai;
        return copy;
    }
    private static synchronized void dongBoMa(String ma) {
        if (ma.toUpperCase(java.util.Locale.ROOT).matches("PN[0-9]+")) {
            java.math.BigInteger n = new java.math.BigInteger(ma.substring(2));
            int next = n.compareTo(java.math.BigInteger.valueOf(Integer.MAX_VALUE - 1L)) >= 0
                    ? Integer.MAX_VALUE : n.intValue() + 1;
            soThuTuTiepTheo = Math.max(soThuTuTiepTheo, next);
        }
    }
    public static synchronized String taoMaTuDong() {
        if (soThuTuTiepTheo == Integer.MAX_VALUE) throw new IllegalStateException("Da het ma phieu nhap tu dong.");
        return "PN" + String.format(java.util.Locale.ROOT, "%06d", soThuTuTiepTheo++);
    }
}
