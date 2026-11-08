package services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import models.*;

/** Owns invoices. All public queries return detached snapshots. */
public final class DanhSachPhieuNhap {
    private final List<PhieuNhap> ds = new ArrayList<>();
    public DanhSachPhieuNhap() { }
    public boolean them(PhieuNhap hoaDon) {
        if (hoaDon == null) throw new IllegalArgumentException("Phieu nhap khong duoc null.");
        if (noiBo(hoaDon.getMaPhieuNhap()) != null) throw new IllegalArgumentException("Trung ma phieu nhap.");
        ds.add(hoaDon.banSao());
        return true;
    }
    private PhieuNhap noiBo(String ma) {
        return ma == null ? null : ds.stream().filter(h -> h.getMaPhieuNhap().equalsIgnoreCase(ma.trim())).findFirst().orElse(null);
    }
    public PhieuNhap timTheoMa(String ma) {
        PhieuNhap h = noiBo(ma);
        return h == null ? null : h.banSao();
    }
    private List<PhieuNhap> loc(Predicate<PhieuNhap> filter) {
        return ds.stream().filter(filter).map(PhieuNhap::banSao).toList();
    }
    public List<PhieuNhap> getTatCa() { return loc(h -> true); }
    public List<PhieuNhap> timTheoNhaCungCap(String maNCC) {
        if (maNCC == null || maNCC.isBlank()) throw new IllegalArgumentException("Ma KH khong duoc rong.");
        return loc(h -> h.getMaNhaCungCap().equalsIgnoreCase(maNCC.trim()));
    }
    public static void kiemTraNgay(LocalDate tuNgay, LocalDate denNgay) {
        if (tuNgay == null || denNgay == null || tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        }
    }
    public List<PhieuNhap> locTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
        kiemTraNgay(tuNgay, denNgay);
        return loc(h -> !h.getNgayNhap().isBefore(tuNgay) && !h.getNgayNhap().isAfter(denNgay));
    }
    public List<PhieuNhap> locTheoTrangThai(TrangThaiPhieuNhap tt) {
        if (tt == null) throw new IllegalArgumentException("Trang thai khong duoc null.");
        return loc(h -> h.getTrangThai() == tt);
    }
    public BigDecimal tongGiaTriNhap(LocalDate tuNgay, LocalDate denNgay) {
        return locTheoNgay(tuNgay, denNgay).stream().filter(h -> !h.coTheChinhSua())
                .map(PhieuNhap::tinhTongTienNhap).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void suaPhieuNhapCho(PhieuNhap moi) {
        if (moi == null) throw new IllegalArgumentException("Phieu nhap khong duoc null.");
        PhieuNhap cu = noiBo(moi.getMaPhieuNhap());
        if (cu == null) throw new IllegalArgumentException("Khong tim thay phieu nhap.");
        if (!cu.coTheChinhSua() || !moi.coTheChinhSua()) throw new IllegalStateException("Chi sua phieu nhap cho.");
        if (!cu.getNgayNhap().equals(moi.getNgayNhap()) || !cu.getMaNhaCungCap().equals(moi.getMaNhaCungCap())
                || !cu.getMaNhanVien().equals(moi.getMaNhanVien())) throw new IllegalArgumentException("Khong doi thong tin dinh danh.");
        ds.set(ds.indexOf(cu), moi.banSao());
    }
    public void xacNhanNhapHang(String ma, DichVuKho kho) {
        PhieuNhap pn = noiBo(ma);
        if (pn == null) throw new IllegalArgumentException("Khong tim thay phieu nhap.");
        kho.xacNhanNhapHang(pn);
    }
    public DanhSachPhieuNhap banSao() {
        DanhSachPhieuNhap copy = new DanhSachPhieuNhap();
        for (PhieuNhap h : ds) copy.them(h);
        return copy;
    }
    public void napLai(DanhSachPhieuNhap moi) {
        for (PhieuNhap old : ds) {
            if (old.coTheChinhSua()) continue;
            PhieuNhap next = moi.timTheoMa(old.getMaPhieuNhap());
            DanhSachPhieuNhap a = new DanhSachPhieuNhap(), b = new DanhSachPhieuNhap();
            a.them(old);
            if (next != null) b.them(next);
            if (!TepPhieuNhap.noiDung(a).equals(TepPhieuNhap.noiDung(b))) {
                throw new IllegalArgumentException("Khong thay/xoa hoa don da xac nhan khi doc lai.");
            }
        }
        thayDuLieu(moi);
    }
    void thayDuLieu(DanhSachPhieuNhap moi) {
        List<PhieuNhap> copy = moi.getTatCa();
        ds.clear();
        ds.addAll(copy);
    }
    public boolean thamChieu(String loai, String ma) {
        if (ma == null) return false;
        return ds.stream().anyMatch(h -> switch (loai) {
            case "NCC" -> h.getMaNhaCungCap().equalsIgnoreCase(ma.trim());
            case "NV" -> h.getMaNhanVien().equalsIgnoreCase(ma.trim());
            case "SP" -> h.getDanhSachChiTiet().stream().anyMatch(c -> c.getSanPham().getMaSanPham().equalsIgnoreCase(ma.trim()));
            default -> throw new IllegalArgumentException("Loai tham chieu khong hop le.");
        });
    }
}
