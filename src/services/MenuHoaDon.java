package services;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import models.*;

/** Console adapter; models and warehouse services contain the business rules. */
public final class MenuHoaDon {
    private final DanhSachSanPham sp;
    private final DanhSachKhachHang kh;
    private final DanhSachNhanVien nv;
    private final DanhSachHoaDon hd;
    private final LuuTruFileText store;
    private final Path dir;
    private boolean chuaLuu;

    public MenuHoaDon(DanhSachSanPham sp, DanhSachKhachHang kh, DanhSachNhanVien nv,
            LuuTruFileText store, Path dir) throws IOException {
        this.sp = sp;
        this.kh = kh;
        this.nv = nv;
        this.store = store;
        this.dir = dir;
        hd = Files.exists(dir.resolve("hoadon.txt")) ? store.docHoaDon("hoadon.txt", sp, kh, nv.getTatCa()) : new DanhSachHoaDon();
        sp.ganHoaDon(hd); kh.ganHoaDon(hd); nv.ganHoaDon(hd);
    }
    public void nhacChuaLuu() {
        if (chuaLuu) System.out.println("CON HOA DON CHUA LUU: dung muc 10 trong menu Hoa don truoc khi thoat.");
    }
    public void chay(Scanner sc) {
        while (true) {
            System.out.println("\n===== QUAN LY HOA DON =====");
            System.out.println("1. Danh sach  \n2. Lap hoa don cho  \n3. Xem chi tiet");
            System.out.println("4. Them dong  \n5. Sua dong  \n6. Xoa dong  \n7. Xac nhan ban hang");
            System.out.println("8. Tim/loc nhieu tieu chi  \n9. Doanh thu  \n10. Luu  \n11. Doc lai  \n0. Quay lai");
            try {
                String choice = doc(sc, "Nhap lua chon: ");
                switch (choice) {
                    case "0" -> {
                        nhacChuaLuu();
                        return;
                    }
                    case "1" -> hienThi(hd.getTatCa());
                    case "2" -> tao(sc);
                    case "3" -> hienThi(List.of(chon(sc)));
                    case "4", "5", "6" -> suaDong(sc, choice);
                    case "7" -> {
                        HoaDon h = chon(sc);
                        hienThi(List.of(h));
                        if (doc(sc, "Xac nhan va luu hoa don/ton kho? (Y/N): ").equalsIgnoreCase("Y")) {
                            kiemTraNguonTrenDia(false);
                            new GiaoDichBanHang(dir, store).xacNhan(h.getMaHoaDon(), hd, sp, kh, nv);
                            chuaLuu = false;
                            System.out.println("Da xac nhan va luu ban hang.");
                        }
                    }
                    case "8" -> tim(sc);
                    case "9" -> {
                        LocalDate from = LocalDate.parse(doc(sc, "Tu ngay (yyyy-MM-dd): "));
                        LocalDate to = LocalDate.parse(doc(sc, "Den ngay (yyyy-MM-dd): "));
                        System.out.println("Doanh thu da xac nhan: " + hd.tongDoanhThu(from, to).toPlainString());
                    }
                    case "10" -> {
                        kiemTraNguonTrenDia(true);
                        if (Files.exists(dir.resolve("hoadon.txt"))) store.docHoaDon("hoadon.txt", sp, kh, nv.getTatCa());
                        store.ghiHoaDon("hoadon.txt", hd);
                        chuaLuu = false;
                        System.out.println("Da luu hoa don.");
                    }
                    case "11" -> {
                        if (chuaLuu && !doc(sc, "Thay doi chua luu se bi thay the. Tiep tuc? (Y/N): ").equalsIgnoreCase("Y")) break;
                        DanhSachHoaDon next = store.docHoaDon("hoadon.txt", sp, kh, nv.getTatCa());
                        hd.napLai(next);
                        chuaLuu = false;
                        System.out.println("Da doc hoa don; khong tru ton lan nua.");
                    }
                    default -> System.out.println("Lua chon khong hop le.");
                }
            } catch (HuyNhap e) { nhacChuaLuu(); return; }
            catch (IOException | IllegalArgumentException | IllegalStateException | java.time.DateTimeException e) {
                System.out.println("Khong the thuc hien: " + e.getMessage());
            }
        }
    }
    private void kiemTraNguonTrenDia(boolean luuCho) throws IOException {
        DanhSachSanPham diskSP = new DanhSachSanPham(0);
        diskSP.docFile(dir.resolve("sanpham.txt").toString());
        // A failed auto-save of a new customer/employee must not create dangling disk references.
        DanhSachKhachHang diskKH = store.docKhachHang("khachhang.txt");
        DanhSachNhanVien diskNV = DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"));
        for (HoaDon h : hd.getTatCa()) {
            if (diskKH.timTheoMa(h.getMaKhachHang()) == null || diskNV.timTheoMa(h.getMaNhanVien()) == null) {
                throw new IOException("Hay luu khach hang/nhan vien truoc khi luu hoa don.");
            }
            for (ChiTietHoaDon ct : h.getDanhSachChiTiet()) {
                if (sp.timTheoMa(ct.getSanPham().getMaSanPham()) == null
                        || (luuCho && diskSP.timTheoMa(ct.getSanPham().getMaSanPham()) == null)) throw new IOException("Thieu san pham cua hoa don.");
            }
        }
        // Newly added products are persisted together with inventory at confirmation.
        // A draft alone must only refer to products that already exist on disk.
    }
    private void tao(Scanner sc) {
        String ma = doc(sc, "Ma hoa don (Enter de tu dong, /huy de huy): ");
        String maKH = doc(sc, "Ma khach hang: ");
        String maNV = doc(sc, "Ma nhan vien: ");
        String date = doc(sc, "Ngay lap yyyy-MM-dd (Enter = hom nay): ");
        if (ma.isEmpty()) {
            do { ma = HoaDon.taoMaTuDong(); } while (hd.timTheoMa(ma) != null);
        }
        HoaDon h = new HoaDon(ma, date.isEmpty() ? LocalDate.now() : LocalDate.parse(date), kh.timTheoMa(maKH), nv.timTheoMa(maNV));
        hd.them(h);
        chuaLuu = true;
        System.out.println("Da lap hoa don cho: " + h.getMaHoaDon());
    }
    private HoaDon chon(Scanner sc) {
        HoaDon h = hd.timTheoMa(doc(sc, "Ma hoa don: "));
        if (h == null) throw new IllegalArgumentException("Khong tim thay hoa don.");
        return h;
    }
    private void suaDong(Scanner sc, String action) {
        HoaDon h = chon(sc);
        if (!h.coTheChinhSua()) throw new IllegalStateException("Hoa don da xac nhan, chi duoc xem.");
        String maSP = doc(sc, "Ma san pham: ");
        if (action.equals("6")) {
            if (!h.xoaChiTiet(maSP)) throw new IllegalArgumentException("Khong tim thay dong hang.");
        } else {
            SanPham product = sp.timTheoMa(maSP);
            if (product == null) throw new IllegalArgumentException("Khong tim thay san pham.");
            int n = Integer.parseInt(doc(sc, "So luong: "));
            String price = doc(sc, "Don gia (Enter = gia hien tai " + product.getGiaBan().toPlainString() + "): ");
            ChiTietHoaDon ct = new ChiTietHoaDon(product, n, price.isEmpty() ? product.getGiaBan() : new BigDecimal(price));
            if (action.equals("4")) h.themChiTiet(ct);
            else if (!h.suaChiTiet(maSP, ct)) throw new IllegalArgumentException("Khong tim thay dong hang.");
        }
        hd.suaHoaDonCho(h);
        chuaLuu = true;
        System.out.println("Da cap nhat chi tiet.");
    }
    private void tim(Scanner sc) {
        String ma = doc(sc, "Ma hoa don chua tu khoa (Enter bo qua): ");
        String customer = doc(sc, "Ma KH (Enter bo qua): ");
        String employee = doc(sc, "Ma NV (Enter bo qua): ");
        String from = doc(sc, "Tu ngay yyyy-MM-dd (Enter bo qua): ");
        String to = doc(sc, "Den ngay yyyy-MM-dd (Enter bo qua): ");
        String state = doc(sc, "Trang thai CHO_XAC_NHAN/DA_XAC_NHAN (Enter bo qua): ");
        LocalDate a = from.isBlank() ? LocalDate.MIN : LocalDate.parse(from);
        LocalDate b = to.isBlank() ? LocalDate.MAX : LocalDate.parse(to);
        TrangThaiHoaDon status = state.isBlank() ? null : TrangThaiHoaDon.valueOf(state);
        List<HoaDon> result = hd.locTheoNgay(a, b).stream()
                .filter(h -> h.getMaHoaDon().toLowerCase(java.util.Locale.ROOT).contains(ma.toLowerCase(java.util.Locale.ROOT)))
                .filter(h -> customer.isBlank() || h.getMaKhachHang().equalsIgnoreCase(customer))
                .filter(h -> employee.isBlank() || h.getMaNhanVien().equalsIgnoreCase(employee))
                .filter(h -> status == null || h.getTrangThai() == status).toList();
        hienThi(result);
    }
    private static void hienThi(List<HoaDon> invoices) {
        if (invoices.isEmpty()) System.out.println("Danh sach hoa don rong.");
        for (HoaDon h : invoices) {
            System.out.println(h.getMaHoaDon() + " | " + h.getNgayLap() + " | KH " + h.getMaKhachHang()
                    + " | NV " + h.getMaNhanVien() + " | " + h.getTrangThai());
            for (ChiTietHoaDon ct : h.getDanhSachChiTiet()) System.out.println("  " + ct.getSanPham().getMaSanPham()
                    + " | " + ct.getSanPham().getTenSanPham() + " | SL " + ct.getSoLuong()
                    + " | Don gia " + ct.getDonGiaBan().toPlainString() + " | Thanh tien " + ct.thanhTien().toPlainString());
            System.out.println("Tong tien: " + h.tinhTongTien().toPlainString());
        }
    }
    private static String doc(Scanner sc, String prompt) {
        System.out.print(prompt);
        if (!sc.hasNextLine()) throw new HuyNhap();
        String value = sc.nextLine().trim();
        if (value.equalsIgnoreCase("/huy")) throw new HuyNhap();
        return value;
    }
    private static final class HuyNhap extends RuntimeException { }
}
