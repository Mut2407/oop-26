package models;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import services.*;

/** File adapter in the model package; restores state without executing a sale. */
public final class TepPhieuNhap {
    private static final String HEADER = "PHIEU_NHAP_V1";
    private TepPhieuNhap() { }
    private record Dong(String maSP, int soLuong, BigDecimal gia) { }
    private record BanGhi(String ma, LocalDate ngay, String kh, String nv, TrangThaiPhieuNhap tt, List<Dong> dong) { }

    private static List<BanGhi> docBang(Path file) throws IOException {
        List<String[]> rows = FileText.doc(file.toString(), HEADER);
        List<BanGhi> result = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        int i = 0;
        try {
            while (i < rows.size()) {
                String[] h = rows.get(i++);
                if (h.length != 7 || !h[0].equals("H") || !DanhSachKhachHang.maHopLe(h[1])
                        || !ids.add(h[1].toUpperCase(Locale.ROOT)) || !DanhSachKhachHang.maHopLe(h[3])
                        || !DanhSachKhachHang.maHopLe(h[4])) throw new IllegalArgumentException("Header phieu nhap sai/trung ma.");
                LocalDate date = LocalDate.parse(h[2]);
                TrangThaiPhieuNhap state = TrangThaiPhieuNhap.valueOf(h[5]);
                int count = Integer.parseInt(h[6]);
                if (count < 0 || count > rows.size()-i || (count == 0 && state == TrangThaiPhieuNhap.DA_NHAP_KHO)) {
                    throw new IllegalArgumentException("So dong phieu nhap khong hop le.");
                }
                List<Dong> lines = new ArrayList<>();
                Set<String> products = new HashSet<>();
                for (int j = 0; j < count; j++) {
                    String[] d = rows.get(i++);
                    if (d.length != 4 || !d[0].equals("D") || !DanhSachKhachHang.maHopLe(d[1])
                            || !products.add(d[1].toUpperCase(Locale.ROOT))) throw new IllegalArgumentException("Dong hang sai/trung san pham.");
                    int n = Integer.parseInt(d[2]);
                    BigDecimal price = new BigDecimal(d[3]);
                    if (n <= 0 || price.signum() < 0) throw new IllegalArgumentException("So luong/gia khong hop le.");
                    lines.add(new Dong(d[1], n, price));
                }
                result.add(new BanGhi(h[1], date, h[3], h[4], state, List.copyOf(lines)));
            }
        } catch (RuntimeException e) {
            throw new IOException("Dong " + (i+1) + ": " + e.getMessage(), e);
        }
        return result;
    }
    public static DanhSachPhieuNhap doc(Path file, DanhSachSanPham sp, DanhSachNhaCungCap kh, List<NhanVien> nv) throws IOException {
        if (sp == null || kh == null || nv == null) throw new IllegalArgumentException("Thieu nguon tham chieu.");
        Map<String, NhanVien> employees = new HashMap<>();
        for (NhanVien n : nv) {
            if (n == null || employees.putIfAbsent(n.getMaNhanVien().toUpperCase(Locale.ROOT), n) != null) {
                throw new IllegalArgumentException("Nguon nhan vien sai/trung ma.");
            }
        }
        DanhSachPhieuNhap ds = new DanhSachPhieuNhap();
        for (BanGhi h : docBang(file)) {
            try {
                PhieuNhap invoice = new PhieuNhap(h.ma(), h.ngay(), kh.timTheoMa(h.kh()), employees.get(h.nv().toUpperCase(Locale.ROOT)));
                for (Dong d : h.dong()) invoice.themChiTiet(new ChiTietPhieuNhap(sp.timTheoMa(d.maSP()), d.soLuong(), d.gia()));
                if (h.tt() == TrangThaiPhieuNhap.DA_NHAP_KHO) invoice.danhDauDaNhapKho();
                ds.them(invoice);
            } catch (IllegalArgumentException e) {
                throw new IOException("Hoa don " + h.ma() + ": thieu/sai tham chieu: " + e.getMessage(), e);
            }
        }
        return ds;
    }
    public static List<String> noiDung(DanhSachPhieuNhap ds) {
        if (ds == null) throw new IllegalArgumentException("Danh sach khong duoc null.");
        List<String> rows = new ArrayList<>();
        rows.add(HEADER);
        for (PhieuNhap h : ds.getTatCa()) {
            rows.add(FileText.dong("H", h.getMaPhieuNhap(), h.getNgayNhap().toString(), h.getMaNhaCungCap(), h.getMaNhanVien(),
                    h.getTrangThai().name(), Integer.toString(h.getDanhSachChiTiet().size())));
            for (ChiTietPhieuNhap ct : h.getDanhSachChiTiet()) rows.add(FileText.dong("D", ct.getSanPham().getMaSanPham(),
                    Integer.toString(ct.getSoLuong()), ct.getDonGiaNhap().stripTrailingZeros().toPlainString()));
        }
        return rows;
    }
    public static void ghi(Path file, DanhSachPhieuNhap ds) throws IOException {
        List<String> rows = noiDung(ds);
        if (Files.exists(file)) {
            for (BanGhi old : docBang(file)) {
                if (old.tt() != TrangThaiPhieuNhap.DA_NHAP_KHO) continue;
                PhieuNhap h = ds.timTheoMa(old.ma());
                if (h == null || h.coTheChinhSua() || !h.getNgayNhap().equals(old.ngay())
                        || !h.getMaNhaCungCap().equalsIgnoreCase(old.kh()) || !h.getMaNhanVien().equalsIgnoreCase(old.nv())
                        || h.getDanhSachChiTiet().size() != old.dong().size()) throw new IOException("Khong ghi de phieu nhap da xac nhan: " + old.ma());
                for (int i = 0; i < old.dong().size(); i++) {
                    Dong d = old.dong().get(i);
                    ChiTietPhieuNhap ct = h.getDanhSachChiTiet().get(i);
                    if (!ct.getSanPham().getMaSanPham().equalsIgnoreCase(d.maSP()) || ct.getSoLuong() != d.soLuong()
                            || ct.getDonGiaNhap().compareTo(d.gia()) != 0) throw new IOException("Chi tiet da xac nhan bi thay doi: " + old.ma());
                }
            }
        }
        FileText.ghi(file.toString(), rows);
    }
}
