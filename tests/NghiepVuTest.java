import java.math.BigDecimal;
import models.*;
import services.*;

public class NghiepVuTest {
    public static void main(String[] args) {
        capNhatTon();
        thatBaiKhongDoiDuLieu();
        kiemTraThamChieu();
        kiemTraThongKe();
        System.out.println("NghiepVuTest OK: " + TestSupport.soKiemTra);
    }

    private static void capNhatTon() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        TestSupport.dung(ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10)));
        TestSupport.dung(ql.traCuuTon("SP1") == 10);
        TestSupport.dung(ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 3)));
        TestSupport.dung(ql.traCuuTon("SP1") == 7);
        TestSupport.dung(ql.suaHoaDon("HD1", TestSupport.hoaDon(ql, "HD1", "SP1", 5)));
        TestSupport.dung(ql.traCuuTon("SP1") == 5);
        TestSupport.dung(!ql.themHoaDon(TestSupport.hoaDon(ql, "hd1", "SP1", 1)));
        TestSupport.dung(ql.traCuuTon("SP1") == 5);
        TestSupport.dung(!ql.suaHoaDon("HD1", TestSupport.hoaDon(ql, "DOIMA", "SP1", 1)));
        TestSupport.dung(ql.xoaHoaDon("HD1"));
        TestSupport.dung(ql.traCuuTon("SP1") == 10);
        TestSupport.dung(ql.suaPhieuNhap("PN1", TestSupport.phieuNhap(ql, "PN1", "SP2", 4)));
        TestSupport.dung(ql.traCuuTon("SP1") == 0);
        TestSupport.dung(ql.traCuuTon("SP2") == 4);
        TestSupport.dung(ql.xoaPhieuNhap("PN1"));
        TestSupport.dung(ql.traCuuTon("SP2") == 0);
    }

    private static void thatBaiKhongDoiDuLieu() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        HoaDon hd = TestSupport.hoaDon(ql, "HD1", "SP1", 3);
        hd.themChiTiet(new ChiTietHoaDon(ql.getSanPham().timTheoMa("SP2"), 1, BigDecimal.ONE));
        try {
            ql.themHoaDon(hd);
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(ql.traCuuTon("SP1") == 10);
            TestSupport.dung(ql.getHoaDon().getSoLuong() == 0);
        }
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 7));
        try {
            ql.xoaPhieuNhap("PN1");
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(ql.traCuuTon("SP1") == 3);
            TestSupport.dung(ql.getPhieuNhap().getSoLuong() == 1);
        }
        try {
            ql.suaHoaDon("HD1", TestSupport.hoaDon(ql, "HD1", "SP2", 1));
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(ql.traCuuTon("SP1") == 3);
            TestSupport.dung(ql.getHoaDon().timTheoMa("HD1").timChiTiet("SP1").getSoLuong() == 7);
        }
        QuanLyCuaHang lon = TestSupport.cuaHang();
        lon.themPhieuNhap(TestSupport.phieuNhap(lon, "PN1", "SP1", Integer.MAX_VALUE));
        try {
            lon.themPhieuNhap(TestSupport.phieuNhap(lon, "PN2", "SP1", 1));
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(lon.traCuuTon("SP1") == Integer.MAX_VALUE);
            TestSupport.dung(lon.getPhieuNhap().getSoLuong() == 1);
        }
        HoaDon khongCoSP = TestSupport.hoaDon(ql, "HD2", "SP1", 1);
        khongCoSP.themChiTiet(new ChiTietHoaDon(TestSupport.giay("SP404"), 1, BigDecimal.ONE));
        try {
            ql.themHoaDon(khongCoSP);
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(ql.traCuuTon("SP1") == 3);
        }
    }

    private static void kiemTraThamChieu() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        LoaiSP loaiCu = ql.getSanPham().timTheoMa("SP1").getLoaiSP();
        ql.suaLoaiSP("L1", new LoaiSP("L1", "Loai moi", ""));
        TestSupport.dung(loaiCu.getTenLoaiSP().equals("Loai moi"));
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 2));
        ql.suaSanPham("SP1", TestSupport.giay("SP1"));
        TestSupport.dung(ql.traCuuTon("SP1") == 8);
        for (int i = 1; i <= 5; i++) {
            try {
                switch (i) {
                    case 1: ql.xoaLoaiSP("L1"); break;
                    case 2: ql.xoaKhachHang("KH1"); break;
                    case 3: ql.xoaNhanVien("NV1"); break;
                    case 4: ql.xoaNhaCungCap("NCC1"); break;
                    case 5: ql.xoaSanPham("SP1"); break;
                    default: break;
                }
                TestSupport.dung(false);
            } catch (IllegalArgumentException e) {
                TestSupport.dung(true);
            }
        }
        HoaDon banSao = ql.getHoaDon().timTheoMa("HD1");
        banSao.xoaChiTiet("SP1");
        TestSupport.dung(ql.getHoaDon().timTheoMa("HD1").getSoLuongChiTiet() == 1);
        ql.xoaHoaDon("HD1");
        ql.xoaPhieuNhap("PN1");
        TestSupport.dung(ql.xoaSanPham("SP1"));
        TestSupport.dung(ql.xoaSanPham("SP2"));
        TestSupport.dung(ql.xoaLoaiSP("L1"));
        TestSupport.dung(ql.xoaKhachHang("KH1"));
        TestSupport.dung(ql.xoaNhanVien("NV1"));
        TestSupport.dung(ql.xoaNhaCungCap("NCC1"));
    }

    private static void kiemTraThongKe() {
        QuanLyCuaHang ql = TestSupport.cuaHang();
        ql.themPhieuNhap(TestSupport.phieuNhap(ql, "PN1", "SP1", 10));
        ql.themHoaDon(TestSupport.hoaDon(ql, "HD1", "SP1", 3));
        ql.getSanPham().timTheoMa("SP1").setGiaBan(new BigDecimal("999.99"));
        TestSupport.tien("300.30", ql.thongKeTienBan());
        TestSupport.tien("602.50", ql.thongKeTienNhap());
        TestSupport.tien("300.30", ql.thongKeTienBan(TestSupport.NGAY, TestSupport.NGAY));
        TestSupport.tien("0", ql.thongKeTienBan(TestSupport.NGAY.plusDays(1), TestSupport.NGAY.plusDays(2)));
        try {
            ql.thongKeTienBan(TestSupport.NGAY.plusDays(1), TestSupport.NGAY);
            TestSupport.dung(false);
        } catch (IllegalArgumentException e) {
            TestSupport.dung(true);
        }
    }
}
