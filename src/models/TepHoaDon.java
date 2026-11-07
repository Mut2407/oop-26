package models;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import services.*;

/** File adapter in the model package; restores state without executing a sale. */
public final class TepHoaDon {
    private static final String HEADER = "HOA_DON_V1";
    private TepHoaDon() { }
    private record Dong(String maSP, int soLuong, BigDecimal gia) { }
    private record BanGhi(String ma, LocalDate ngay, String kh, String nv, TrangThaiHoaDon tt, List<Dong> dong) { }

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
                        || !DanhSachKhachHang.maHopLe(h[4])) throw new IllegalArgumentException("Header hoa don sai/trung ma.");
                LocalDate date = LocalDate.parse(h[2]);
                TrangThaiHoaDon state = TrangThaiHoaDon.valueOf(h[5]);
                int count = Integer.parseInt(h[6]);
                if (count < 0 || count > rows.size()-i || (count == 0 && state == TrangThaiHoaDon.DA_XAC_NHAN)) {
                    throw new IllegalArgumentException("So dong hoa don khong hop le.");
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
    public static DanhSachHoaDon doc(Path file, DanhSachSanPham sp, DanhSachKhachHang kh, List<NhanVien> nv) throws IOException {
        if (sp == null || kh == null || nv == null) throw new IllegalArgumentException("Thieu nguon tham chieu.");
        Map<String, NhanVien> employees = new HashMap<>();
        for (NhanVien n : nv) {
            if (n == null || employees.putIfAbsent(n.getMaNhanVien().toUpperCase(Locale.ROOT), n) != null) {
                throw new IllegalArgumentException("Nguon nhan vien sai/trung ma.");
            }
        }
        DanhSachHoaDon ds = new DanhSachHoaDon();
        for (BanGhi h : docBang(file)) {
            try {
                HoaDon invoice = new HoaDon(h.ma(), h.ngay(), kh.timTheoMa(h.kh()), employees.get(h.nv().toUpperCase(Locale.ROOT)));
                for (Dong d : h.dong()) invoice.themChiTiet(new ChiTietHoaDon(sp.timTheoMa(d.maSP()), d.soLuong(), d.gia()));
                if (h.tt() == TrangThaiHoaDon.DA_XAC_NHAN) invoice.danhDauDaXacNhan();
                ds.them(invoice);
            } catch (IllegalArgumentException e) {
                throw new IOException("Hoa don " + h.ma() + ": thieu/sai tham chieu: " + e.getMessage(), e);
            }
        }
        return ds;
    }
    public static List<String> noiDung(DanhSachHoaDon ds) {
        if (ds == null) throw new IllegalArgumentException("Danh sach khong duoc null.");
        List<String> rows = new ArrayList<>();
        rows.add(HEADER);
        for (HoaDon h : ds.getTatCa()) {
            rows.add(FileText.dong("H", h.getMaHoaDon(), h.getNgayLap().toString(), h.getMaKhachHang(), h.getMaNhanVien(),
                    h.getTrangThai().name(), Integer.toString(h.getDanhSachChiTiet().size())));
            for (ChiTietHoaDon ct : h.getDanhSachChiTiet()) rows.add(FileText.dong("D", ct.getSanPham().getMaSanPham(),
                    Integer.toString(ct.getSoLuong()), ct.getDonGiaBan().stripTrailingZeros().toPlainString()));
        }
        return rows;
    }
    public static void ghi(Path file, DanhSachHoaDon ds) throws IOException {
        List<String> rows = noiDung(ds);
        if (Files.exists(file)) {
            for (BanGhi old : docBang(file)) {
                if (old.tt() != TrangThaiHoaDon.DA_XAC_NHAN) continue;
                HoaDon h = ds.timTheoMa(old.ma());
                if (h == null || h.coTheChinhSua() || !h.getNgayLap().equals(old.ngay())
                        || !h.getMaKhachHang().equalsIgnoreCase(old.kh()) || !h.getMaNhanVien().equalsIgnoreCase(old.nv())
                        || h.getDanhSachChiTiet().size() != old.dong().size()) throw new IOException("Khong ghi de hoa don da xac nhan: " + old.ma());
                for (int i = 0; i < old.dong().size(); i++) {
                    Dong d = old.dong().get(i);
                    ChiTietHoaDon ct = h.getDanhSachChiTiet().get(i);
                    if (!ct.getSanPham().getMaSanPham().equalsIgnoreCase(d.maSP()) || ct.getSoLuong() != d.soLuong()
                            || ct.getDonGiaBan().compareTo(d.gia()) != 0) throw new IOException("Chi tiet da xac nhan bi thay doi: " + old.ma());
                }
            }
        }
        FileText.ghi(file.toString(), rows);
    }
}
