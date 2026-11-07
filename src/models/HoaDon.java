package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import services.DanhSachKhachHang;

public final class HoaDon {
    private final String maHoaDon;
    private final LocalDate ngayLap;
    private final KhachHang khachHang;
    private final NhanVien nhanVien;
    private final List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();
    private TrangThaiHoaDon trangThai = TrangThaiHoaDon.CHO_XAC_NHAN;
    private static int soThuTuTiepTheo = 1;

    public HoaDon(String ma, LocalDate ngay, KhachHang kh, NhanVien nv) {
        if (!DanhSachKhachHang.maHopLe(ma) || ngay == null || kh == null || nv == null) {
            throw new IllegalArgumentException("Ma/ngay/khach hang/nhan vien hoa don khong hop le.");
        }
        maHoaDon = ma.trim();
        ngayLap = ngay;
        khachHang = new KhachHang(kh.getMaKhachHang(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi());
        nhanVien = new NhanVien(nv.getMaNhanVien(), nv.getHoTen(), nv.getSoDienThoai(), nv.getCaLamViec());
        dongBoMa(maHoaDon);
    }
    public String getMaHoaDon() { return maHoaDon; }
    public LocalDate getNgayLap() { return ngayLap; }
    public TrangThaiHoaDon getTrangThai() { return trangThai; }
    public String getMaKhachHang() { return khachHang.getMaKhachHang(); }
    public String getMaNhanVien() { return nhanVien.getMaNhanVien(); }
    public List<ChiTietHoaDon> getDanhSachChiTiet() { return List.copyOf(danhSachChiTiet); }
    public boolean coTheChinhSua() { return trangThai == TrangThaiHoaDon.CHO_XAC_NHAN; }

    private void kiemTraCho() {
        if (!coTheChinhSua()) throw new IllegalStateException("Hoa don da xac nhan, khong the chinh sua.");
    }
    public boolean themChiTiet(ChiTietHoaDon ct) {
        kiemTraCho();
        if (ct == null) throw new IllegalArgumentException("Chi tiet khong duoc null.");
        if (chiSo(ct.getSanPham().getMaSanPham()) >= 0) {
            throw new IllegalArgumentException("San pham da co trong hoa don.");
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
    public boolean suaChiTiet(String maSP, ChiTietHoaDon moi) {
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
    public BigDecimal tinhTongTien() {
        return danhSachChiTiet.stream().map(ChiTietHoaDon::thanhTien).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    void danhDauDaXacNhan() {
        kiemTraCho();
        if (danhSachChiTiet.isEmpty()) throw new IllegalArgumentException("Hoa don rong khong the xac nhan.");
        trangThai = TrangThaiHoaDon.DA_XAC_NHAN;
    }
    public HoaDon banSao() {
        HoaDon copy = new HoaDon(maHoaDon, ngayLap, khachHang, nhanVien);
        copy.danhSachChiTiet.addAll(danhSachChiTiet);
        copy.trangThai = trangThai;
        return copy;
    }
    private static synchronized void dongBoMa(String ma) {
        if (ma.toUpperCase(java.util.Locale.ROOT).matches("HD[0-9]+")) {
            java.math.BigInteger n = new java.math.BigInteger(ma.substring(2));
            int next = n.compareTo(java.math.BigInteger.valueOf(Integer.MAX_VALUE - 1L)) >= 0
                    ? Integer.MAX_VALUE : n.intValue() + 1;
            soThuTuTiepTheo = Math.max(soThuTuTiepTheo, next);
        }
    }
    public static synchronized String taoMaTuDong() {
        if (soThuTuTiepTheo == Integer.MAX_VALUE) throw new IllegalStateException("Da het ma hoa don tu dong.");
        return "HD" + String.format(java.util.Locale.ROOT, "%06d", soThuTuTiepTheo++);
    }
}
