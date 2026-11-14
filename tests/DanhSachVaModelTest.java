import java.math.BigDecimal;
import models.*;
import services.*;

public class DanhSachVaModelTest {
    public static void main(String[] args) {
        kiemTraMang();
        kiemTraCacDanhSach();
        kiemTraDaHinhVaChiTiet();
        kiemTraDuLieuSai();
        System.out.println("DanhSachVaModelTest OK: " + TestSupport.soKiemTra);
    }

    private static void kiemTraMang() {
        DanhSachSanPham ds = new DanhSachSanPham(0);
        for (int i = 0; i < 25; i++) {
            TestSupport.dung(ds.them(TestSupport.giay("SP" + i)));
        }
        TestSupport.dung(ds.getSoLuong() == 25);
        TestSupport.dung(ds.xoa("SP0"));
        TestSupport.dung(ds.xoa("SP12"));
        TestSupport.dung(ds.xoa("SP24"));
        TestSupport.dung(ds.getSoLuong() == 22);
        TestSupport.dung(ds.timTheoMa(" sp13 ") != null);
        TestSupport.dung(ds.timKiem("GIAY").length == 22);
        TestSupport.dung(ds.timKiem("SP1").length == 10);
        TestSupport.dung(ds.timKiem("").length == 22);
        TestSupport.dung(ds.timKiem("khong co").length == 0);
        TestSupport.dung(!ds.them(TestSupport.giay("sp1")));
        TestSupport.dung(!ds.sua("SP1", TestSupport.giay("MA_MOI")));
        TestSupport.dung(ds.locTheoLoaiSP("l1").length == 22);
        TestSupport.dung(ds.locTheoKhoangGia(new BigDecimal("100.1"), new BigDecimal("100.10")).length == 22);
        SanPham[] mang = ds.getTatCa();
        mang[0] = null;
        TestSupport.dung(ds.timTheoMa("SP1") != null);
        QuanLyCuaHang ql = TestSupport.cuaHang();
        TestSupport.dung(!ql.themNhieuSanPham(new SanPham[]{TestSupport.giay("SP3"), TestSupport.giay("SP1")}));
        TestSupport.dung(ql.getSanPham().getSoLuong() == 2);
        TestSupport.dung(ql.themNhieuSanPham(new SanPham[]{TestSupport.giay("SP3"), TestSupport.giay("SP4")}));
        TestSupport.dung(ql.getSanPham().getSoLuong() == 4);
    }

    private static <T> void crud(IQuanLyDanhSach<T> ds, String ma, T goc, T moi) {
        TestSupport.dung(ds.them(goc));
        TestSupport.dung(ds.timTheoMa(ma) != null);
        TestSupport.dung(ds.sua(ma, moi));
        TestSupport.dung(ds.timKiem("").length == 1);
        TestSupport.dung(!ds.them(moi));
        TestSupport.dung(ds.xoa(ma));
        TestSupport.dung(ds.getTatCa().length == 0);
        TestSupport.dung(!ds.xoa(ma));
        TestSupport.dung(!ds.sua(ma, moi));
    }

    private static void kiemTraCacDanhSach() {
        crud(new DanhSachLoaiSP(0), "L1", new LoaiSP("L1", "Loai", ""), new LoaiSP("L1", "Moi", ""));
        crud(new DanhSachKhachHang(0), "KH1", new KhachHang("KH1", "A", "0901234567", "HCM"),
                new KhachHang("KH1", "B", "0901234567", "HN"));
        crud(new DanhSachNhanVien(0), "NV1", new NhanVien("NV1", "A", "0901234567", "Sang"),
                new NhanVien("NV1", "B", "0901234567", "Chieu"));
        crud(new DanhSachNhaCungCap(0), "NCC1", new NhaCungCap("NCC1", "A", "0901234567", "HCM"),
                new NhaCungCap("NCC1", "B", "0901234567", "HN"));
        QuanLyCuaHang ql = TestSupport.cuaHang();
        crud(new DanhSachHoaDon(0), "HD1", TestSupport.hoaDon(ql, "HD1", "SP1", 2), TestSupport.hoaDon(ql, "HD1", "SP2", 3));
        crud(new DanhSachPhieuNhap(0), "PN1", TestSupport.phieuNhap(ql, "PN1", "SP1", 2), TestSupport.phieuNhap(ql, "PN1", "SP2", 3));
    }

    private static void kiemTraDaHinhVaChiTiet() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        HoaDon hd = TestSupport.hoaDon(ql, "HD1", "SP1", 2);
        HoaDon banSao = new HoaDon(hd);
        TestSupport.dung(banSao.suaChiTiet("SP1", new ChiTietHoaDon(TestSupport.giay("SP1"), 3, BigDecimal.TEN)));
        TestSupport.dung(hd.timChiTiet("SP1").getSoLuong() == 2);
        TestSupport.dung(hd.xoaChiTiet("SP1"));
        TestSupport.dung(!hd.xoaChiTiet("SP1"));
        for (int i = 0; i < 20; i++) {
            hd.themChiTiet(new ChiTietHoaDon(TestSupport.giay("X" + i), 1, new BigDecimal("0.1")));
        }
        TestSupport.tien("2.0", hd.tinhTongTien());
        hd.xoaChiTiet("X0");
        hd.xoaChiTiet("X10");
        hd.xoaChiTiet("X19");
        TestSupport.dung(hd.getSoLuongChiTiet() == 17);
        PhieuNhap pn = TestSupport.phieuNhap(ql, "PN1", "SP1", 2);
        pn.suaChiTiet("SP1", new ChiTietPhieuNhap(TestSupport.giay("SP1"), 3, new BigDecimal("1.1")));
        TestSupport.tien("3.3", pn.tinhTongTien());
        LoaiSP loai = ql.getLoaiSP().timTheoMa("L1");
        SanPham[] ds = {TestSupport.giay("G1"),
                new QuanAoTheThao("A1", "Ao", "Nike", BigDecimal.TEN, loai, "XL", "Den", "Cotton"),
                new DungCuTheThao("D1", "Vot", "Yonex", BigDecimal.ONE, loai, "Cau long", "Carbon", 0.085)};
        TestSupport.dung(ds[0].hienThiThongTin().contains("GiayTheThao"));
        TestSupport.dung(ds[1].hienThiThongTin().contains("QuanAoTheThao"));
        TestSupport.dung(ds[2].hienThiThongTin().contains("DungCuTheThao"));
    }

    private static void kiemTraDuLieuSai() {
        try {
            new NhanVien("NV1", "A", "abc", "Sang");
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(true);
        }
        try {
            TestSupport.giay("SP1").setKichCoGiay(Double.NaN);
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(true);
        }
        try {
            new ChiTietHoaDon(TestSupport.giay("SP1"), 0, BigDecimal.ONE);
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(true);
        }
        try {
            new DanhSachKhachHang().them(new KhachHang());
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(true);
        }
    }
}
