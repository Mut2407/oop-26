import java.io.IOException;
import java.nio.file.*;
import java.math.BigDecimal;
import models.*;
import services.*;
public class Task075LuuTruHoaDonTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("hd-store-");
        try {
            HoaDon h = Task072HoaDonTest.hd("HD900"); h.themChiTiet(Task072HoaDonTest.ct(2));
            DanhSachHoaDon hd = new DanhSachHoaDon(); hd.them(h);
            DanhSachSanPham sp = new DanhSachSanPham(DuLieu.taoDanhSachSanPham());
            DanhSachKhachHang kh = new DanhSachKhachHang(DuLieu.taoDanhSachKhachHang());
            DanhSachNhanVien nv = new DanhSachNhanVien(DuLieu.taoDanhSachNhanVien(),DuLieu.taoDiaChiNhanVienCu());
            LuuTruFileText store = new LuuTruFileText(dir.toString());
            store.ghiSanPham("sanpham.txt",sp); store.ghiHoaDon("hoadon.txt", hd);
            assert store.docHoaDon("hoadon.txt",sp,kh,nv.getTatCa()).getTatCa().size()==1;
            String beforeSP=Files.readString(dir.resolve("sanpham.txt")), beforeHD=Files.readString(dir.resolve("hoadon.txt"));
            LuuTruFileText faulty = new LuuTruFileText(dir.toString()) {
                @Override public void ghiHoaDon(String path, DanhSachHoaDon ds) throws IOException {
                    super.ghiHoaDon(path,ds); throw new IOException("simulated second-file failure");
                }
            };
            try { new GiaoDichBanHang(dir,faulty).xacNhan("HD900",hd,sp,kh,nv); throw new AssertionError(); } catch(IOException expected) { }
            assert Files.readString(dir.resolve("sanpham.txt")).equals(beforeSP);
            assert Files.readString(dir.resolve("hoadon.txt")).equals(beforeHD);
            assert hd.timTheoMa("HD900").coTheChinhSua();
            new GiaoDichBanHang(dir,store).xacNhan("HD900",hd,sp,kh,nv);
            assert !hd.timTheoMa("HD900").coTheChinhSua();
            int count=sp.getDanhSach().get(0).getSoLuongTon();
            DanhSachHoaDon loaded=store.docHoaDon("hoadon.txt",sp,kh,nv.getTatCa());
            assert !loaded.timTheoMa("HD900").coTheChinhSua();
            assert sp.getDanhSach().get(0).getSoLuongTon()==count;
            assert loaded.tongDoanhThu(h.getNgayLap(),h.getNgayLap()).compareTo(new BigDecimal("2.50"))==0;
            try { new GiaoDichBanHang(dir,store).xacNhan("HD900",hd,sp,kh,nv); throw new AssertionError(); } catch(IllegalArgumentException expected) { }
            try { store.ghiHoaDon("hoadon.txt",new DanhSachHoaDon()); throw new AssertionError(); } catch(IOException expected) { }
            Files.writeString(dir.resolve("bad.txt"),"HOA_DON_V1\nH\tBAD\t2026-11-01\tMISSING\tNV01\tCHO_XAC_NHAN\t0\n");
            try { store.docHoaDon("bad.txt",sp,kh,nv.getTatCa()); throw new AssertionError(); } catch(IOException expected) { }
            String bad=Files.readString(dir.resolve("bad.txt"));
            Files.writeString(dir.resolve("bad.txt"),bad+"WRONG\n");
            try { store.ghiHoaDon("bad.txt",hd); throw new AssertionError(); } catch(IOException expected) { }
            assert !Files.exists(dir.resolve("banhang.undo"));
            assert Integer.parseInt(HoaDon.taoMaTuDong().substring(2)) > 900;
            System.out.println("Task075LuuTruHoaDonTest OK");
        } finally { ConsoleTestSupport.deleteTree(dir); }
    }
}
