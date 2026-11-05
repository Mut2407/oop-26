import java.math.BigDecimal;
import java.util.List;
import models.*;
import services.*;

public class DanhSachSanPhamTest {
    public static void main(String[] args) {
        DuLieu.khoiTaoDanhMuc();
        DuLieu.dangKyDanhMuc(new DanhMuc("AO", "Ao test", ""));
        SanPham giay = giay("G1", "Giày chạy bộ", "100.25", 2);
        SanPham ao = ao("A1", "Áo bóng đá", "50", 0);
        SanPham dungCu = dungCu("D1", "Vợt cầu lông", "75", 5);

        IQuanLyDanhSach<SanPham> quanLy = new DanhSachSanPham(0);
        DanhSachSanPham ds = (DanhSachSanPham) quanLy;
        check(ds.getSoLuong() == 0 && ds.xuatDanhSach().isEmpty());
        expectError(() -> new DanhSachSanPham(-1));
        check(quanLy.them(giay) && ds.getSoLuong() == 1);
        check(ds.themNhieu(new SanPham[] { ao, dungCu }) == 2 && ds.getSoLuong() == 3);
        check(ds.timTheoMa("g1") instanceof GiayTheThao);
        check(ds.timTheoMa("A1") instanceof QuanAoTheThao);
        check(ds.timTheoMa("D1") instanceof DungCuTheThao);
        check(ds.xuatDanhSach().contains("Kich co giay: 42")
                && ds.xuatDanhSach().contains("Kich co quan ao: M")
                && ds.xuatDanhSach().contains("Khoi luong: 0.1"));
        check(ds.timGanDung("GIAY").size() == 1);
        check(ds.timGanDung("chay bo").size() == 1);
        check(ds.timGanDung("vot cau long").size() == 1);
        check(ds.locTheoDanhMuc("giay").size() == 1);
        check(ds.locTheoLoai("GIAY").size() == 1);
        check(ds.locTheoKhoangGia(new BigDecimal("50"), new BigDecimal("75")).size() == 2);
        check(ds.thongKeTheoDanhMuc().get("GIAY") == 1);
        check(ds.thongKeTheoDanhMuc().get("AO") == 1);
        check(ds.lietKeSapHetHang(2).size() == 2);
        check(ds.getDanhSach().get(0).getMaSanPham().equals("G1"));

        List<SanPham> banSao = ds.getDanhSach();
        expectUnsupported(() -> banSao.clear());
        banSao.get(0).tangTon(5);
        ds.timTheoMa("G1").tangTon(5);
        ds.timGanDung("giay").get(0).tangTon(5);
        check(ds.timTheoMa("G1").getSoLuongTon() == 2);
        giay.tangTon(5);
        check(ds.timTheoMa("G1").getSoLuongTon() == 2);

        expectError(() -> ds.them(giay));
        expectError(() -> ds.them(null));
        expectError(() -> ds.themNhieu(new SanPham[] { giay("G2", "Giay hai", "10", 1), giay }));
        check(ds.getSoLuong() == 3 && ds.timTheoMa("G2") == null);
        expectError(() -> ds.themNhieu(new SanPham[] { ao("A2", "Ao hai", "10", 1), null }));
        check(ds.getSoLuong() == 3 && ds.timTheoMa("A2") == null);
        expectError(() -> ds.themNhieu(new SanPham[] { giay("G2", "Giay hai", "10", 1), giay("g2", "Trung", "10", 1) }));
        check(ds.getSoLuong() == 3);
        expectError(() -> ds.suaTheoMa("G1", giay("G9", "Doi ma", "10", 1)));
        check(ds.suaTheoMa("G1", giay("G1", "Giay moi", "110", 3)));
        check(ds.timTheoMa("G1").getTenSanPham().equals("Giay moi"));
        check(!ds.suaTheoMa("KHONG_CO", giay) && !ds.xoaTheoMa("KHONG_CO"));

        check(ds.tangTonTheoMa("G1", 2) && ds.timTheoMa("G1").getSoLuongTon() == 5);
        check(ds.giamTonTheoMa("G1", 4) && ds.timTheoMa("G1").getSoLuongTon() == 1);
        expectError(() -> ds.giamTonTheoMa("G1", 2));
        expectError(() -> ds.tangTonTheoMa("G1", 0));
        check(!ds.tangTonTheoMa("KHONG_CO", 1));

        check(ds.xoaTheoMa("G1") && ds.getSoLuong() == 2);
        check(ds.xoaTheoMa("D1") && ds.getSoLuong() == 1);
        check(ds.xoaTheoMa("A1") && ds.getSoLuong() == 0);
        check(ds.getDanhSach().isEmpty() && ds.timTheoMa("A1") == null);
        check(ds.them(giay("G3", "Lai", "1", 1)) && ds.getSoLuong() == 1);

        DanhSachSanPham xoaGiua = new DanhSachSanPham(new SanPham[] { giay, ao, dungCu });
        check(xoaGiua.xoaTheoMa("A1") && xoaGiua.getSoLuong() == 2);
        check(xoaGiua.getDanhSach().get(1).getMaSanPham().equals("D1"));

        ds.nhapBanDau(new SanPham[] { giay, ao, dungCu });
        check(ds.getSoLuong() == 3 && ds.timTheoMa("G3") == null);
        expectError(() -> ds.nhapBanDau(new SanPham[] { giay, giay }));
        check(ds.getSoLuong() == 3 && ds.timTheoMa("D1") != null);
        expectError(() -> ds.nhapBanDau(null));
        expectError(() -> ds.timGanDung(" "));
        expectError(() -> ds.locTheoKhoangGia(BigDecimal.TEN, BigDecimal.ONE));
        expectError(() -> ds.lietKeSapHetHang(-1));
        System.out.println("DanhSachSanPhamTest OK");
    }

    private static SanPham giay(String ma, String ten, String gia, int ton) {
        return new GiayTheThao(ma, ten, "Nike", new BigDecimal(gia), ton,
                "Den", null, DuLieu.timDanhMuc("GIAY"), "42", "Cao su");
    }

    private static SanPham ao(String ma, String ten, String gia, int ton) {
        return new QuanAoTheThao(ma, ten, "Nike", new BigDecimal(gia), ton,
                null, null, DuLieu.timDanhMuc("AO"), "M");
    }

    private static SanPham dungCu(String ma, String ten, String gia, int ton) {
        return new DungCuTheThao(ma, ten, "Yonex", new BigDecimal(gia), ton,
                null, "Carbon", DuLieu.timDanhMuc("DC"), 0.1);
    }

    private static void check(boolean dung) {
        if (!dung) throw new AssertionError();
    }

    private static void expectError(Runnable viec) {
        try { viec.run(); } catch (IllegalArgumentException e) { return; }
        throw new AssertionError("Phai tu choi du lieu khong hop le");
    }

    private static void expectUnsupported(Runnable viec) {
        try { viec.run(); } catch (UnsupportedOperationException e) { return; }
        throw new AssertionError("Khong duoc sua danh sach tra ve");
    }
}
