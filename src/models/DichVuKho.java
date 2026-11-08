package models;

import java.util.LinkedHashMap;
import java.util.Map;
import services.DanhSachSanPham;

/** Co-located with HoaDon to respect its UML package-private transition. */
public final class DichVuKho {
    private final DanhSachSanPham danhSachSanPham;
    public DichVuKho(DanhSachSanPham ds) {
        if (ds == null) throw new IllegalArgumentException("Kho khong duoc null.");
        danhSachSanPham = ds;
    }
    public boolean kiemTraTonKho(HoaDon hd) {
        if (hd == null || !hd.coTheChinhSua() || hd.getDanhSachChiTiet().isEmpty()) return false;
        for (ChiTietHoaDon ct : hd.getDanhSachChiTiet()) {
            SanPham sp = danhSachSanPham.timTheoMa(ct.getSanPham().getMaSanPham());
            if (sp == null || !sp.conDuHang(ct.getSoLuong())) return false;
        }
        return true;
    }
    public void xacNhanBanHang(HoaDon hd) {
        if (!kiemTraTonKho(hd)) throw new IllegalArgumentException("Hoa don rong/da xac nhan hoac khong du ton kho.");
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (ChiTietHoaDon ct : hd.getDanhSachChiTiet()) quantities.put(ct.getSanPham().getMaSanPham(), ct.getSoLuong());
        danhSachSanPham.giamTonDongBo(quantities);
        hd.danhDauDaXacNhan();
    }
    /** Validate and receive every line before transitioning the receipt. */
    public void xacNhanNhapHang(PhieuNhap pn) {
        if (pn == null || !pn.coTheChinhSua() || pn.getDanhSachChiTiet().isEmpty()) {
            throw new IllegalArgumentException("Phieu rong hoac da nhap kho.");
        }
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (ChiTietPhieuNhap ct : pn.getDanhSachChiTiet()) quantities.put(ct.getSanPham().getMaSanPham(), ct.getSoLuong());
        danhSachSanPham.tangTonDongBo(quantities);
        pn.danhDauDaNhapKho();
    }
}
