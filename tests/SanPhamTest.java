import java.math.BigDecimal;
import models.*;

public class SanPhamTest {
    public static void main(String[] args) {
        DanhMuc giay = new DanhMuc("GIAY", "Giay test", "");
        DanhMuc ao = new DanhMuc("AO", "Ao test", "");
        DanhMuc dungCu = new DanhMuc("DC", "Dung cu test", "");
        DuLieu.dangKyDanhMuc(giay);
        DuLieu.dangKyDanhMuc(ao);
        DuLieu.dangKyDanhMuc(dungCu);

        SanPham sp = new GiayTheThao("G1", "Giay", "Nike", new BigDecimal("0.10"),
                3, "Den", "Vai", giay, "42", "Cao su");
        SanPham quanAo = new QuanAoTheThao("A1", "Ao", "Nike", new BigDecimal("20"),
                1, null, null, ao, "M");
        SanPham dungCuTheThao = new DungCuTheThao("D1", "Vot", "Yonex", new BigDecimal("30"),
                2, null, "Carbon", dungCu, 0.1);
        check(sp.getDanhMuc() == giay);
        check(sp.tinhGiaTriTonTheoGiaBan().equals(new BigDecimal("0.30")));
        check(sp.conDuHang(3) && !sp.conDuHang(4) && !sp.conDuHang(0));
        sp.giamTon(2);
        check(sp.getSoLuongTon() == 1);
        sp.tangTon(3);
        check(sp.getSoLuongTon() == 4);
        sp.capNhatGiaBan(new BigDecimal("0.20"));
        check(sp.tinhGiaTriTonTheoGiaBan().equals(new BigDecimal("0.80")));
        sp.capNhatThongTin("Giay moi", "Adidas", "Trang", "Vai");
        check(sp.getMaSanPham().equals("G1") && sp.getTenSanPham().equals("Giay moi"));
        check(sp.hienThiThongTin().contains("Kich co giay: 42"));
        check(quanAo.hienThiThongTin().contains("Kich co quan ao: M"));
        check(dungCuTheThao.hienThiThongTin().contains("Khoi luong: 0.1"));
        check(sp.layLoaiSanPham().equals("GIAY") && quanAo.layLoaiSanPham().equals("QUAN_AO")
                && dungCuTheThao.layLoaiSanPham().equals("DUNG_CU"));

        expectError(() -> new GiayTheThao("G2", "Giay", "Nike", new BigDecimal("-1"),
                0, null, null, giay, "42", "De"));
        expectError(() -> new QuanAoTheThao("A2", "Ao", "Nike", BigDecimal.ONE,
                -1, null, null, ao, "M"));
        expectError(() -> new GiayTheThao("G3", "Giay", "Nike", BigDecimal.ONE,
                0, null, null, new DanhMuc("GIAY", "Khac", ""), "42", "De"));
        expectError(() -> sp.capNhatGiaBan(null));
        expectError(() -> sp.capNhatGiaBan(new BigDecimal("-0.01")));
        expectError(() -> sp.giamTon(5));
        expectError(() -> sp.giamTon(0));
        expectError(() -> sp.tangTon(0));
        expectError(() -> sp.tangTon(-1));
        expectError(() -> ((GiayTheThao) sp).capNhatKichCo(""));
        expectError(() -> ((DungCuTheThao) dungCuTheThao).capNhatKhoiLuong(Double.NaN));
        expectError(() -> ((DungCuTheThao) dungCuTheThao).capNhatKhoiLuong(Double.POSITIVE_INFINITY));
        expectError(() -> ((DungCuTheThao) dungCuTheThao).capNhatKhoiLuong(-1));
        System.out.println("SanPhamTest OK");
    }

    private static void check(boolean condition) {
        if (!condition) throw new AssertionError();
    }

    private static void expectError(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError("Phai tu choi du lieu khong hop le");
    }
}
