package services;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import models.*;

/** Console adapter; models and warehouse services contain the business rules. */
public final class MenuPhieuNhap {
    private final DanhSachSanPham sp;
    private final DanhSachNhaCungCap ncc;
    private final DanhSachNhanVien nv;
    private final DanhSachPhieuNhap pn;
    private final LuuTruFileText store;
    private final Path dir;
    private boolean chuaLuu;

    public MenuPhieuNhap(DanhSachSanPham sp, DanhSachNhaCungCap ncc, DanhSachNhanVien nv,
            LuuTruFileText store, Path dir) throws IOException {
        this.sp = sp;
        this.ncc = ncc;
        this.nv = nv;
        this.store = store;
        this.dir = dir;
        pn = Files.exists(dir.resolve("phieunhap.txt")) ? store.docPhieuNhap("phieunhap.txt", sp, ncc, nv.getTatCa()) : new DanhSachPhieuNhap();
        sp.ganPhieuNhap(pn); ncc.ganPhieuNhap(pn); nv.ganPhieuNhap(pn);
    }
    DanhSachPhieuNhap danhSach() { return pn; }
    public void nhacChuaLuu() {
        if (chuaLuu) System.out.println("CON PHIEU NHAP CHUA LUU: dung muc 10 trong menu Phieu nhap truoc khi thoat.");
    }
    public void chay(Scanner sc) {
        while (true) {
            System.out.println("\n===== QUAN LY PHIEU NHAP =====");
            System.out.println("1. Danh sach  2. Lap phieu nhap cho  3. Xem chi tiet");
            System.out.println("4. Them dong  5. Sua dong  6. Xoa dong  7. Xac nhan nhap kho");
            System.out.println("8. Tim/loc nhieu tieu chi  9. Gia tri nhap  10. Luu  11. Doc lai  0. Quay lai");
            try {
                String choice = doc(sc, "Nhap lua chon: ");
                switch (choice) {
                    case "0" -> {
                        nhacChuaLuu();
                        return;
                    }
                    case "1" -> hienThi(pn.getTatCa());
                    case "2" -> tao(sc);
                    case "3" -> hienThi(List.of(chon(sc)));
                    case "4", "5", "6" -> suaDong(sc, choice);
                    case "7" -> {
                        PhieuNhap h = chon(sc);
                        hienThi(List.of(h));
                        if (doc(sc, "Xac nhan va luu phieu nhap/ton kho? (Y/N): ").equalsIgnoreCase("Y")) {
                            kiemTraNguonTrenDia(false);
                            new GiaoDichNhapHang(dir, store).xacNhan(h.getMaPhieuNhap(), pn, sp, ncc, nv);
                            chuaLuu = false;
                            System.out.println("Da xac nhan va luu nhap kho.");
                        }
                    }
                    case "8" -> tim(sc);
                    case "9" -> {
                        LocalDate from = LocalDate.parse(doc(sc, "Tu ngay (yyyy-MM-dd): "));
                        LocalDate to = LocalDate.parse(doc(sc, "Den ngay (yyyy-MM-dd): "));
                        System.out.println("Gia tri nhap da xac nhan: " + pn.tongGiaTriNhap(from, to).toPlainString());
                    }
                    case "10" -> {
                        kiemTraNguonTrenDia(true);
                        if (Files.exists(dir.resolve("phieunhap.txt"))) store.docPhieuNhap("phieunhap.txt", sp, ncc, nv.getTatCa());
                        store.ghiPhieuNhap("phieunhap.txt", pn);
                        chuaLuu = false;
                        System.out.println("Da luu phieu nhap.");
                    }
                    case "11" -> {
                        if (chuaLuu && !doc(sc, "Thay doi chua luu se bi thay the. Tiep tuc? (Y/N): ").equalsIgnoreCase("Y")) break;
                        DanhSachPhieuNhap next = store.docPhieuNhap("phieunhap.txt", sp, ncc, nv.getTatCa());
                        pn.napLai(next);
                        chuaLuu = false;
                        System.out.println("Da doc phieu nhap; khong tang ton lan nua.");
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
        // A failed auto-save of a new supplier/employee must not create dangling disk references.
        DanhSachNhaCungCap diskNCC = store.docNhaCungCap("nhacungcap.txt");
        DanhSachNhanVien diskNV = DanhSachNhanVien.tuDanhSach(store.docNhanVien("nhanvien.txt"));
        for (PhieuNhap h : pn.getTatCa()) {
            if (diskNCC.timTheoMa(h.getMaNhaCungCap()) == null || diskNV.timTheoMa(h.getMaNhanVien()) == null) {
                throw new IOException("Hay luu nha cung cap/nhan vien truoc khi luu phieu nhap.");
            }
            for (ChiTietPhieuNhap ct : h.getDanhSachChiTiet()) {
                if (sp.timTheoMa(ct.getSanPham().getMaSanPham()) == null
                        || (luuCho && diskSP.timTheoMa(ct.getSanPham().getMaSanPham()) == null)) throw new IOException("Thieu san pham cua phieu nhap.");
            }
        }
        // Newly added products are persisted together with inventory at confirmation.
        // A draft alone must only refer to products that already exist on disk.
    }
    private void tao(Scanner sc) {
        String ma = doc(sc, "Ma phieu nhap (Enter de tu dong, /huy de huy): ");
        String maNCC = doc(sc, "Ma nha cung cap: ");
        String maNV = doc(sc, "Ma nhan vien: ");
        String date = doc(sc, "Ngay nhap yyyy-MM-dd (Enter = hom nay): ");
        if (ma.isEmpty()) {
            do { ma = PhieuNhap.taoMaTuDong(); } while (pn.timTheoMa(ma) != null);
        }
        PhieuNhap h = new PhieuNhap(ma, date.isEmpty() ? LocalDate.now() : LocalDate.parse(date), ncc.timTheoMa(maNCC), nv.timTheoMa(maNV));
        pn.them(h);
        chuaLuu = true;
        System.out.println("Da lap phieu nhap cho: " + h.getMaPhieuNhap());
    }
    private PhieuNhap chon(Scanner sc) {
        PhieuNhap h = pn.timTheoMa(doc(sc, "Ma phieu nhap: "));
        if (h == null) throw new IllegalArgumentException("Khong tim thay phieu nhap.");
        return h;
    }
    private void suaDong(Scanner sc, String action) {
        PhieuNhap h = chon(sc);
        if (!h.coTheChinhSua()) throw new IllegalStateException("Phieu nhap da xac nhan, chi duoc xem.");
        String maSP = doc(sc, "Ma san pham: ");
        if (action.equals("6")) {
            if (!h.xoaChiTiet(maSP)) throw new IllegalArgumentException("Khong tim thay dong hang.");
        } else {
            SanPham product = sp.timTheoMa(maSP);
            if (product == null) throw new IllegalArgumentException("Khong tim thay san pham.");
            int n = Integer.parseInt(doc(sc, "So luong: "));
            String price = doc(sc, "Don gia nhap (bat buoc, khong phai gia ban): ");
            ChiTietPhieuNhap ct = new ChiTietPhieuNhap(product, n, new BigDecimal(price));
            if (action.equals("4")) h.themChiTiet(ct);
            else if (!h.suaChiTiet(maSP, ct)) throw new IllegalArgumentException("Khong tim thay dong hang.");
        }
        pn.suaPhieuNhapCho(h);
        chuaLuu = true;
        System.out.println("Da cap nhat chi tiet.");
    }
    private void tim(Scanner sc) {
        String ma = doc(sc, "Ma phieu nhap chua tu khoa (Enter bo qua): ");
        String customer = doc(sc, "Ma NCC (Enter bo qua): ");
        String employee = doc(sc, "Ma NV (Enter bo qua): ");
        String from = doc(sc, "Tu ngay yyyy-MM-dd (Enter bo qua): ");
        String to = doc(sc, "Den ngay yyyy-MM-dd (Enter bo qua): ");
        String state = doc(sc, "Trang thai CHO_XAC_NHAN/DA_NHAP_KHO (Enter bo qua): ");
        LocalDate a = from.isBlank() ? LocalDate.MIN : LocalDate.parse(from);
        LocalDate b = to.isBlank() ? LocalDate.MAX : LocalDate.parse(to);
        TrangThaiPhieuNhap status = state.isBlank() ? null : TrangThaiPhieuNhap.valueOf(state);
        List<PhieuNhap> result = pn.locTheoNgay(a, b).stream()
                .filter(h -> h.getMaPhieuNhap().toLowerCase(java.util.Locale.ROOT).contains(ma.toLowerCase(java.util.Locale.ROOT)))
                .filter(h -> customer.isBlank() || h.getMaNhaCungCap().equalsIgnoreCase(customer))
                .filter(h -> employee.isBlank() || h.getMaNhanVien().equalsIgnoreCase(employee))
                .filter(h -> status == null || h.getTrangThai() == status).toList();
        hienThi(result);
    }
    private static void hienThi(List<PhieuNhap> invoices) {
        if (invoices.isEmpty()) System.out.println("Danh sach phieu nhap rong.");
        for (PhieuNhap h : invoices) {
            System.out.println(h.getMaPhieuNhap() + " | " + h.getNgayNhap() + " | NCC " + h.getMaNhaCungCap()
                    + " | NV " + h.getMaNhanVien() + " | " + h.getTrangThai());
            for (ChiTietPhieuNhap ct : h.getDanhSachChiTiet()) System.out.println("  " + ct.getSanPham().getMaSanPham()
                    + " | " + ct.getSanPham().getTenSanPham() + " | SL " + ct.getSoLuong()
                    + " | Don gia " + ct.getDonGiaNhap().toPlainString() + " | Thanh tien " + ct.thanhTienNhap().toPlainString());
            System.out.println("Tong tien: " + h.tinhTongTienNhap().toPlainString());
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
