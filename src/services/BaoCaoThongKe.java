package services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import models.*;

public final class BaoCaoThongKe {
    private final DanhSachSanPham danhSachSanPham;
    private final DanhSachHoaDon danhSachHoaDon;
    private final DanhSachPhieuNhap danhSachPhieuNhap;
    public BaoCaoThongKe(DanhSachSanPham sp, DanhSachHoaDon hd, DanhSachPhieuNhap pn) {
        danhSachSanPham = Objects.requireNonNull(sp);
        danhSachHoaDon = Objects.requireNonNull(hd);
        danhSachPhieuNhap = Objects.requireNonNull(pn);
    }
    public BigDecimal doanhThuTheoKhoang(LocalDate tuNgay, LocalDate denNgay) {
        return danhSachHoaDon.tongDoanhThu(tuNgay, denNgay);
    }
    public BigDecimal giaTriNhapTheoKhoang(LocalDate tuNgay, LocalDate denNgay) {
        return danhSachPhieuNhap.tongGiaTriNhap(tuNgay, denNgay);
    }
    public Map<String, BigInteger> soLuongDaBan() {
        Map<String, BigInteger> counts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (HoaDon h : danhSachHoaDon.locTheoTrangThai(TrangThaiHoaDon.DA_XAC_NHAN)) {
            for (ChiTietHoaDon ct : h.getDanhSachChiTiet()) {
                String ma = ct.getSanPham().getMaSanPham().toUpperCase(Locale.ROOT);
                counts.merge(ma, BigInteger.valueOf(ct.getSoLuong()), BigInteger::add);
            }
        }
        return Collections.unmodifiableMap(counts);
    }
    public List<SanPham> sanPhamBanChay(int topN) {
        if (topN < 0) throw new IllegalArgumentException("Top N khong duoc am.");
        return soLuongDaBan().entrySet().stream()
                .sorted(Map.Entry.<String, BigInteger>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
                .limit(topN).map(entry -> {
                    SanPham sp = danhSachSanPham.timTheoMa(entry.getKey());
                    if (sp == null) throw new IllegalStateException("Thieu san pham duoc hoa don tham chieu: " + entry.getKey());
                    return sp;
                }).toList();
    }
    public List<SanPham> canhBaoTonKho(int nguong) {
        if (nguong < 0) throw new IllegalArgumentException("Nguong khong duoc am.");
        return danhSachSanPham.getDanhSach().stream().filter(sp -> sp.getSoLuongTon() <= nguong).toList();
    }
    public BigDecimal giaTriTonKhoTheoGiaBan() {
        return danhSachSanPham.getDanhSach().stream().map(SanPham::tinhGiaTriTonTheoGiaBan)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
