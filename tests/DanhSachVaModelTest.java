import java.math.BigDecimal;
import java.lang.reflect.Modifier;
import models.*;
import services.*;

public final class DanhSachVaModelTest {
    public static void main(String[] args) throws Exception {
        mangVaTimKiem();
        cacDanhSach();
        chiTietVaDaHinh();
        giaTriKhongHopLe();
        yeuCauOop();
        System.out.println("DanhSachVaModelTest OK: " + TestSupport.assertions + " assertions");
    }

    private static void mangVaTimKiem() {
        DanhSachSanPham ds = new DanhSachSanPham(0);
        for (int i = 0; i < 25; i++) {
            ds.them(TestSupport.giay("SP" + i));
        }
        TestSupport.bang(25, ds.getSoLuong());
        TestSupport.check(ds.xoa("SP0"));
        TestSupport.check(ds.xoa("SP12"));
        TestSupport.check(ds.xoa("SP24"));
        TestSupport.bang(22, ds.getSoLuong());
        TestSupport.check(ds.timTheoMa("sp13") != null);
        TestSupport.bang(22, ds.timKiem("giay chay").length);
        TestSupport.bang(10, ds.timKiem("SP1").length);
        TestSupport.bang(22, ds.timKiem("").length);
        TestSupport.bang(0, ds.timKiem("khong thay").length);
        TestSupport.bang(22, ds.locTheoLoaiSP("l1").length);
        TestSupport.bang(22, ds.locTheoKhoangGia(new BigDecimal("100.1"), new BigDecimal("100.10")).length);
        TestSupport.loi(() -> ds.them(TestSupport.giay("Sp1")));
        TestSupport.loi(() -> ds.sua("SP1", TestSupport.giay("DOIMA")));
        TestSupport.loi(() -> ds.themNhieu(new SanPham[]{TestSupport.giay("MOI"), TestSupport.giay("sp1")}));
        TestSupport.check(ds.timTheoMa("MOI") == null);
        SanPham[] mang = ds.getTatCa();
        mang[0].setTenSanPham("Da sua ban sao");
        mang[1] = null;
        TestSupport.bang("Giày chạy bộ", ds.timTheoMa("SP1").getTenSanPham());
        QuanLyCuaHang ql = TestSupport.cuaHang();
        TestSupport.loi(() -> ql.themNhieuSanPham(new SanPham[]{TestSupport.giay("SP3"), TestSupport.giay("sp1")}));
        TestSupport.bang(2, ql.getSanPham().getSoLuong());
        ql.themNhieuSanPham(new SanPham[]{TestSupport.giay("SP3"), TestSupport.giay("SP4")});
        TestSupport.bang(4, ql.getSanPham().getSoLuong());
    }

    private static <T> void crud(IQuanLyDanhSach<T> ds, String ma, T goc, T moi) {
        TestSupport.check(ds.them(goc));
        TestSupport.check(ds.timTheoMa(ma) != null);
        TestSupport.check(ds.sua(ma, moi));
        TestSupport.bang(1, ds.timKiem("").length);
        TestSupport.loi(() -> ds.them(moi));
        TestSupport.check(ds.xoa(ma));
        TestSupport.bang(0, ds.getTatCa().length);
        TestSupport.check(!ds.xoa(ma));
        TestSupport.check(!ds.sua(ma, moi));
    }

    private static void cacDanhSach() {
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

    private static void chiTietVaDaHinh() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        HoaDon hd = TestSupport.hoaDon(ql, "HD1", "SP1", 2);
        TestSupport.loi(() -> hd.themChiTiet(hd.timChiTiet("SP1")));
        ChiTietHoaDon ct = hd.getChiTiet()[0];
        ct.setSoLuong(99);
        TestSupport.bang(2, hd.timChiTiet("SP1").getSoLuong());
        TestSupport.check(hd.xoaChiTiet("SP1"));
        TestSupport.check(!hd.xoaChiTiet("SP1"));
        TestSupport.tien("0", hd.tinhTongTien());
        for (int i = 0; i < 20; i++) {
            hd.themChiTiet(new ChiTietHoaDon(TestSupport.giay("X" + i), 1, new BigDecimal("0.1")));
        }
        TestSupport.tien("2.0", hd.tinhTongTien());
        hd.xoaChiTiet("X0"); hd.xoaChiTiet("X10"); hd.xoaChiTiet("X19");
        TestSupport.bang(17, hd.getSoLuongChiTiet());
        PhieuNhap pn = TestSupport.phieuNhap(ql, "PN1", "SP1", 2);
        TestSupport.loi(() -> pn.themChiTiet(pn.timChiTiet("SP1")));
        pn.suaChiTiet("SP1", new ChiTietPhieuNhap(TestSupport.giay("SP1"), 3, new BigDecimal("1.1")));
        TestSupport.tien("3.3", pn.tinhTongTien());
        TestSupport.check(pn.xoaChiTiet("SP1"));
        LoaiSP loai = new LoaiSP("L1", "The thao", "");
        SanPham[] mang = {TestSupport.giay("G1"),
                new QuanAoTheThao("A1", "Ao", "Nike", BigDecimal.TEN, loai, "XL", "Den", "Cotton"),
                new DungCuTheThao("D1", "Vot", "Yonex", BigDecimal.ONE, loai, "Cau long", "Carbon", 0.085)};
        for (SanPham sp : mang) {
            TestSupport.check(sp.hienThiThongTin().contains(sp.getClass().getSimpleName()));
            TestSupport.bang(sp.getClass(), sp.saoChep().getClass());
        }
    }

    private static void giaTriKhongHopLe() {
        TestSupport.loi(() -> new KhachHang(" ", "A", "0901234567", "HCM"));
        TestSupport.loi(() -> new NhanVien("NV1", "A", "abc", "Sang"));
        TestSupport.loi(() -> new DanhSachSanPham(-1));
        TestSupport.loi(() -> TestSupport.giay("SP1").setKichCoGiay(Double.NaN));
        TestSupport.loi(() -> TestSupport.giay("SP1").setKichCoGiay(0));
        TestSupport.loi(() -> TestSupport.giay("SP1").setGiaBan(new BigDecimal("-1")));
        TestSupport.loi(() -> new ChiTietHoaDon(TestSupport.giay("SP1"), 0, BigDecimal.ONE));
        TestSupport.loi(() -> new ChiTietPhieuNhap(TestSupport.giay("SP1"), -1, BigDecimal.ONE));
        TestSupport.loi(() -> new DanhSachKhachHang().them(new KhachHang()));
        TestSupport.loi(() -> new DanhSachHoaDon().them(new HoaDon()));
    }

    private static void yeuCauOop() throws Exception {
        TestSupport.check(Modifier.isAbstract(SanPham.class.getModifiers()));
        TestSupport.check(Modifier.isAbstract(SanPham.class.getMethod("hienThiThongTin").getModifiers()));
        Class<?>[] danhSach = {DanhSachSanPham.class, DanhSachLoaiSP.class, DanhSachKhachHang.class,
                DanhSachNhanVien.class, DanhSachNhaCungCap.class, DanhSachHoaDon.class, DanhSachPhieuNhap.class};
        for (Class<?> lop : danhSach) {
            TestSupport.check(IQuanLyDanhSach.class.isAssignableFrom(lop));
            TestSupport.check(lop.getDeclaredField("ds").getType().isArray());
            TestSupport.check(lop.getConstructor().newInstance() != null);
        }
        TestSupport.check(Modifier.isStatic(KiemTra.class.getDeclaredField("SUC_CHUA_MAC_DINH").getModifiers()));
        String main = java.nio.file.Files.readString(java.nio.file.Path.of("src/Main.java"));
        int dau = main.indexOf("public static void main");
        int cuoi = main.indexOf("\n    }", dau);
        TestSupport.check(main.substring(dau, cuoi).lines().count() + 1 <= 10);
    }
}
