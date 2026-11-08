package services;

import java.time.LocalDate;
import java.util.Scanner;
import models.SanPham;

public final class MenuBaoCao {
    private final BaoCaoThongKe baoCao;
    public MenuBaoCao(DanhSachSanPham sp, MenuHoaDon hd, MenuPhieuNhap pn) {
        baoCao = new BaoCaoThongKe(sp, hd.danhSach(), pn.danhSach());
    }
    public void chay(Scanner sc) {
        while (true) {
            System.out.println("\n===== BAO CAO THONG KE =====");
            System.out.println("1. Doanh thu theo khoang ngay");
            System.out.println("2. Gia tri nhap theo khoang ngay");
            System.out.println("3. Top san pham ban chay (toan bo lich su da xac nhan)");
            System.out.println("4. Canh bao ton kho");
            System.out.println("5. Gia tri ton kho theo gia ban hien tai");
            System.out.println("0. Quay lai");
            try {
                String choice = doc(sc, "Nhap lua chon: ");
                switch (choice) {
                    case "0" -> { return; }
                    case "1", "2" -> {
                        LocalDate from = LocalDate.parse(doc(sc,"Tu ngay yyyy-MM-dd: "));
                        LocalDate to = LocalDate.parse(doc(sc,"Den ngay yyyy-MM-dd: "));
                        System.out.println(choice.equals("1")
                                ? "Doanh thu da xac nhan: " + baoCao.doanhThuTheoKhoang(from,to).toPlainString()
                                : "Gia tri da nhap kho: " + baoCao.giaTriNhapTheoKhoang(from,to).toPlainString());
                    }
                    case "3" -> {
                        int n = Integer.parseInt(doc(sc,"Top N: "));
                        var counts = baoCao.soLuongDaBan();
                        var products = baoCao.sanPhamBanChay(n);
                        if (products.isEmpty()) System.out.println("Khong co san pham da ban.");
                        for (SanPham sp : products) System.out.println(sp.getMaSanPham() + " | " + sp.getTenSanPham()
                                + " | Da ban: " + counts.get(sp.getMaSanPham().toUpperCase(java.util.Locale.ROOT)));
                    }
                    case "4" -> {
                        int threshold = Integer.parseInt(doc(sc,"Nguong ton (>=0): "));
                        var products = baoCao.canhBaoTonKho(threshold);
                        if (products.isEmpty()) System.out.println("Khong co san pham duoi/ bang nguong.");
                        for (SanPham sp : products) System.out.println(sp.getMaSanPham() + " | " + sp.getTenSanPham() + " | Ton: " + sp.getSoLuongTon());
                    }
                    case "5" -> System.out.println("Gia tri ton theo gia ban (khong phai loi nhuan): " + baoCao.giaTriTonKhoTheoGiaBan().toPlainString());
                    default -> System.out.println("Lua chon khong hop le.");
                }
            } catch (HuyNhap e) { return; }
            catch (IllegalArgumentException | java.time.DateTimeException | IllegalStateException e) {
                System.out.println("Khong the tinh bao cao: " + e.getMessage());
            }
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
