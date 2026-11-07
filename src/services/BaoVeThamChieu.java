package services;

import java.util.Collection;

/** Per-list association, deliberately not a process-wide registry. */
final class BaoVeThamChieu {
    private final String loai;
    private DanhSachHoaDon hoaDon;
    BaoVeThamChieu(String loai) { this.loai = loai; }
    void gan(DanhSachHoaDon ds) {
        if (ds == null || (hoaDon != null && hoaDon != ds)) throw new IllegalArgumentException("Khong the thay nguon hoa don bao ve.");
        hoaDon = ds;
    }
    void xoa(String ma) {
        if (hoaDon != null && hoaDon.thamChieu(loai, ma)) throw new IllegalArgumentException("Khong the xoa ma dang duoc hoa don tham chieu: " + ma);
    }
    void thay(Collection<String> cu, Collection<String> moi) {
        for (String ma : cu) {
            if (moi.stream().noneMatch(n -> n.equalsIgnoreCase(ma))) xoa(ma);
        }
    }
}
