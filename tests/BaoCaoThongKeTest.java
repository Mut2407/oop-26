import java.math.*;
import java.time.LocalDate;
import models.*;
import services.*;
public class BaoCaoThongKeTest {
    public static void main(String[] args) {
        DuLieu.khoiTaoDanhMuc();
        var sp=new DanhSachSanPham(DuLieu.taoDanhSachSanPham());
        var hd=new DanhSachHoaDon(); var pn=new DanhSachPhieuNhap();
        var bc=new BaoCaoThongKe(sp,hd,pn);
        assert bc.sanPhamBanChay(5).isEmpty();
        var a=sp.getDanhSach().get(0); var b=sp.getDanhSach().get(1);
        var h=new HoaDon("BC1",LocalDate.of(2026,11,1),DuLieu.taoDanhSachKhachHang()[0],DuLieu.taoDanhSachNhanVien()[0]);
        h.themChiTiet(new ChiTietHoaDon(a,2,new BigDecimal("0.10"))); hd.them(h);
        assert bc.doanhThuTheoKhoang(h.getNgayLap(),h.getNgayLap()).signum()==0;
        hd.xacNhanBanHang("BC1",new DichVuKho(sp));
        h=new HoaDon("BC2",LocalDate.of(2026,11,1),DuLieu.taoDanhSachKhachHang()[0],DuLieu.taoDanhSachNhanVien()[0]); h.themChiTiet(new ChiTietHoaDon(b,2,new BigDecimal("0.20"))); hd.them(h);
        hd.xacNhanBanHang("BC2",new DichVuKho(sp));
        assert bc.doanhThuTheoKhoang(h.getNgayLap(),h.getNgayLap()).compareTo(new BigDecimal("0.60"))==0;
        var p=new PhieuNhap("PNBC",LocalDate.of(2026,11,1),DuLieu.taoDanhSachNhaCungCap()[0],DuLieu.taoDanhSachNhanVien()[0]); p.themChiTiet(new ChiTietPhieuNhap(a,5,new BigDecimal("0.05"))); pn.them(p);
        assert bc.giaTriNhapTheoKhoang(p.getNgayNhap(),p.getNgayNhap()).signum()==0;
        pn.xacNhanNhapHang("PNBC",new DichVuKho(sp));
        assert bc.giaTriNhapTheoKhoang(p.getNgayNhap(),p.getNgayNhap()).compareTo(new BigDecimal("0.25"))==0;
        var top=bc.sanPhamBanChay(20);
        assert top.size()==2 && top.get(0).getMaSanPham().compareToIgnoreCase(top.get(1).getMaSanPham())<0;
        assert bc.soLuongDaBan().get(a.getMaSanPham()).equals(BigInteger.TWO);
        int ton=sp.timTheoMa(a.getMaSanPham()).getSoLuongTon();
        top.get(0).capNhatGiaBan(BigDecimal.ZERO);
        assert sp.timTheoMa(a.getMaSanPham()).getSoLuongTon()==ton;
        BigDecimal expected=sp.getDanhSach().stream().map(SanPham::tinhGiaTriTonTheoGiaBan).reduce(BigDecimal.ZERO,BigDecimal::add);
        assert bc.giaTriTonKhoTheoGiaBan().compareTo(expected)==0;
        assert bc.canhBaoTonKho(Integer.MAX_VALUE).size()==sp.getSoLuong();
        assert bc.sanPhamBanChay(0).isEmpty();
        try { bc.sanPhamBanChay(-1); throw new AssertionError(); } catch(IllegalArgumentException ok) { }
        try { bc.canhBaoTonKho(-1); throw new AssertionError(); } catch(IllegalArgumentException ok) { }
        try { bc.doanhThuTheoKhoang(LocalDate.MAX,LocalDate.MIN); throw new AssertionError(); } catch(IllegalArgumentException ok) { }
        // Boundaries and drafts are excluded; all-history ranking does include later confirmed sales.
        var later = new HoaDon("BC_LATER", LocalDate.of(2026,11,2),DuLieu.taoDanhSachKhachHang()[0],DuLieu.taoDanhSachNhanVien()[0]);
        later.themChiTiet(new ChiTietHoaDon(a,1,BigDecimal.ONE)); hd.them(later);
        assert bc.doanhThuTheoKhoang(h.getNgayLap(),h.getNgayLap()).compareTo(new BigDecimal("0.60"))==0;
        hd.xacNhanBanHang("BC_LATER",new DichVuKho(sp));
        assert bc.doanhThuTheoKhoang(h.getNgayLap(),h.getNgayLap()).compareTo(new BigDecimal("0.60"))==0;
        assert bc.doanhThuTheoKhoang(h.getNgayLap(),later.getNgayLap()).compareTo(new BigDecimal("1.60"))==0;
        assert bc.sanPhamBanChay(1).get(0).getMaSanPham().equals(a.getMaSanPham());
        var empty = new DanhSachSanPham(0);
        assert new BaoCaoThongKe(empty,new DanhSachHoaDon(),new DanhSachPhieuNhap()).giaTriTonKhoTheoGiaBan().signum()==0;
        // Accumulated sales may exceed int even though each line and inventory fit int.
        var bigSP = new DanhSachSanPham(DuLieu.taoDanhSachSanPham());
        var max = bigSP.getDanhSach().get(0);
        max.tangTon(Integer.MAX_VALUE-max.getSoLuongTon()); bigSP.suaTheoMa(max.getMaSanPham(),max);
        var many = new DanhSachHoaDon();
        for(int i=0;i<2;i++) {
            var big = new HoaDon("BIG"+i,LocalDate.of(2026,11,1),DuLieu.taoDanhSachKhachHang()[0],DuLieu.taoDanhSachNhanVien()[0]);
            big.themChiTiet(new ChiTietHoaDon(max,Integer.MAX_VALUE,BigDecimal.ZERO)); many.them(big);
            many.xacNhanBanHang(big.getMaHoaDon(),new DichVuKho(bigSP));
            if(i==0) bigSP.tangTonDongBo(java.util.Map.of(max.getMaSanPham(),Integer.MAX_VALUE));
        }
        assert new BaoCaoThongKe(bigSP,many,new DanhSachPhieuNhap()).soLuongDaBan().get(max.getMaSanPham()).equals(BigInteger.valueOf(4294967294L));
        assert new BaoCaoThongKe(bigSP,many,new DanhSachPhieuNhap()).canhBaoTonKho(0).stream().anyMatch(x->x.getMaSanPham().equals(max.getMaSanPham()));
        System.out.println("BaoCaoThongKeTest OK");
    }
}
