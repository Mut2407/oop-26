package services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import models.*;

/** Owns invoices. All public queries return detached snapshots. */
public final class DanhSachHoaDon {
    private final List<HoaDon> ds = new ArrayList<>();
    public DanhSachHoaDon() { }
    public boolean them(HoaDon hoaDon) {
        if (hoaDon == null) throw new IllegalArgumentException("Hoa don khong duoc null.");
        if (noiBo(hoaDon.getMaHoaDon()) != null) throw new IllegalArgumentException("Trung ma hoa don.");
        ds.add(hoaDon.banSao());
        return true;
    }
    private HoaDon noiBo(String ma) {
        return ma == null ? null : ds.stream().filter(h -> h.getMaHoaDon().equalsIgnoreCase(ma.trim())).findFirst().orElse(null);
    }
    public HoaDon timTheoMa(String ma) {
        HoaDon h = noiBo(ma);
        return h == null ? null : h.banSao();
    }
    private List<HoaDon> loc(Predicate<HoaDon> filter) {
        return ds.stream().filter(filter).map(HoaDon::banSao).toList();
    }
    public List<HoaDon> getTatCa() { return loc(h -> true); }
    public List<HoaDon> timTheoKhachHang(String maKH) {
        if (maKH == null || maKH.isBlank()) throw new IllegalArgumentException("Ma KH khong duoc rong.");
        return loc(h -> h.getMaKhachHang().equalsIgnoreCase(maKH.trim()));
    }
    public static void kiemTraNgay(LocalDate tuNgay, LocalDate denNgay) {
        if (tuNgay == null || denNgay == null || tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Khoang ngay khong hop le.");
        }
    }
    public List<HoaDon> locTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
        kiemTraNgay(tuNgay, denNgay);
        return loc(h -> !h.getNgayLap().isBefore(tuNgay) && !h.getNgayLap().isAfter(denNgay));
    }
    public List<HoaDon> locTheoTrangThai(TrangThaiHoaDon tt) {
        if (tt == null) throw new IllegalArgumentException("Trang thai khong duoc null.");
        return loc(h -> h.getTrangThai() == tt);
    }
    public BigDecimal tongDoanhThu(LocalDate tuNgay, LocalDate denNgay) {
        return locTheoNgay(tuNgay, denNgay).stream().filter(h -> !h.coTheChinhSua())
                .map(HoaDon::tinhTongTien).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void suaHoaDonCho(HoaDon moi) {
        if (moi == null) throw new IllegalArgumentException("Hoa don khong duoc null.");
        HoaDon cu = noiBo(moi.getMaHoaDon());
        if (cu == null) throw new IllegalArgumentException("Khong tim thay hoa don.");
        if (!cu.coTheChinhSua() || !moi.coTheChinhSua()) throw new IllegalStateException("Chi sua hoa don cho.");
        if (!cu.getNgayLap().equals(moi.getNgayLap()) || !cu.getMaKhachHang().equals(moi.getMaKhachHang())
                || !cu.getMaNhanVien().equals(moi.getMaNhanVien())) throw new IllegalArgumentException("Khong doi thong tin dinh danh.");
        ds.set(ds.indexOf(cu), moi.banSao());
    }
    public void xacNhanBanHang(String ma, DichVuKho kho) {
        HoaDon hd = noiBo(ma);
        if (hd == null) throw new IllegalArgumentException("Khong tim thay hoa don.");
        kho.xacNhanBanHang(hd);
    }
    public DanhSachHoaDon banSao() {
        DanhSachHoaDon copy = new DanhSachHoaDon();
        for (HoaDon h : ds) copy.them(h);
        return copy;
    }
    public void napLai(DanhSachHoaDon moi) {
        for (HoaDon old : ds) {
            if (old.coTheChinhSua()) continue;
            HoaDon next = moi.timTheoMa(old.getMaHoaDon());
            DanhSachHoaDon a = new DanhSachHoaDon(), b = new DanhSachHoaDon();
            a.them(old);
            if (next != null) b.them(next);
            if (!TepHoaDon.noiDung(a).equals(TepHoaDon.noiDung(b))) {
                throw new IllegalArgumentException("Khong thay/xoa hoa don da xac nhan khi doc lai.");
            }
        }
        thayDuLieu(moi);
    }
    void thayDuLieu(DanhSachHoaDon moi) {
        List<HoaDon> copy = moi.getTatCa();
        ds.clear();
        ds.addAll(copy);
    }
    public boolean thamChieu(String loai, String ma) {
        if (ma == null) return false;
        return ds.stream().anyMatch(h -> switch (loai) {
            case "KH" -> h.getMaKhachHang().equalsIgnoreCase(ma.trim());
            case "NV" -> h.getMaNhanVien().equalsIgnoreCase(ma.trim());
            case "SP" -> h.getDanhSachChiTiet().stream().anyMatch(c -> c.getSanPham().getMaSanPham().equalsIgnoreCase(ma.trim()));
            default -> throw new IllegalArgumentException("Loai tham chieu khong hop le.");
        });
    }
}
